package com.wushengju.study.analysis.ip;


import com.wushengju.study.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import sun.net.util.IPAddressUtil;

import java.io.*;
import java.util.Arrays;

/**
 * IP地址读取器
 *
 * @author Sunny
 * @version 1.0
 * @className IpReader
 * @date 2019-11-19 10:41
 */
@Slf4j
public class IpReader {
    private static final String DEFAULT_LANGUAGE = "CN";
    private int fileSize;
    private int nodeCount;
    private IpMetaData meta;
    private byte[] data;
    private int v4offset;

    public IpReader(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            String realPath = resource.getClassLoader().getResource(filePath).getPath();
            InputStream in = new FileInputStream(new File(realPath));
            init(this.readAllAsStream(in));
        } catch (Exception e) {
            log.error("read ip data from file error", e);
        }
    }

    public IpReader(InputStream in) {
        try {
            init(this.readAllAsStream(in));
        } catch (Exception e) {
            log.error("read ip data from file error", e);
        }
    }

    protected byte[] readAllAsStream(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        try {
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            log.error("read ip data from file error", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("close input stream error", e);
            }
        }
        return out.toByteArray();
    }

    protected void init(byte[] data) throws InvalidIpDataException {

        this.data = data;
        this.fileSize = data.length;

        long metaLength = bytesToLong(
                this.data[0],
                this.data[1],
                this.data[2],
                this.data[3]
        );

        byte[] metaBytes = Arrays.copyOfRange(this.data, 4, Long.valueOf(metaLength).intValue() + 4);

        IpMetaData meta = JsonUtil.toBean(new String(metaBytes), IpMetaData.class);
        this.nodeCount = meta.node_count;
        this.meta = meta;

        if ((meta.total_size + Long.valueOf(metaLength).intValue() + 4) != this.data.length) {
            throw new InvalidIpDataException("database file size error");
        }

        this.data = Arrays.copyOfRange(this.data, Long.valueOf(metaLength).intValue() + 4, this.fileSize);

        /** for ipv4 */
        if (0x01 == (this.meta.ip_version & 0x01)) {
            int node = 0;
            for (int i = 0; i < 96 && node < this.nodeCount; i++) {
                if (i >= 80) {
                    node = this.readNode(node, 1);
                } else {
                    node = this.readNode(node, 0);
                }
            }

            this.v4offset = node;
        }
    }
    public String[] find(String addr) throws IpFormatException, InvalidIpDataException{
        return find(addr, DEFAULT_LANGUAGE);
    }
    public String[] find(String addr, String language) throws IpFormatException, InvalidIpDataException {

        int off;
        try {
            off = this.meta.languages.get(language);
        } catch (NullPointerException e) {
            return null;
        }
        byte[] ipv;
        if (addr.indexOf(":") > 0) {
            ipv = IPAddressUtil.textToNumericFormatV6(addr);
            if (ipv == null) {
                throw new IpFormatException("ipv6 format error");
            }
            if ((this.meta.ip_version & 0x02) != 0x02) {
                throw new IpFormatException("no support ipv6");
            }

        } else if (addr.indexOf(".") > 0) {
            ipv = IPAddressUtil.textToNumericFormatV4(addr);
            if (ipv == null) {
                throw new IpFormatException("ipv4 format error");
            }
            if ((this.meta.ip_version & 0x01) != 0x01) {
                throw new IpFormatException("no support ipv4");
            }
        } else {
            throw new IpFormatException("ip format error");
        }
        int node = 0;
        try {
            node = this.findNode(ipv);
        } catch (NotFoundException nfe) {
            return null;
        }
        final String data = this.resolve(node);
        return Arrays.copyOfRange(data.split("\t", this.meta.fields.length * this.meta.languages.size()), off, off + this.meta.fields.length);
    }

    private int findNode(byte[] binary) throws NotFoundException {
        int node = 0;
        final int bit = binary.length * 8;
        if (bit == 32) {
            node = this.v4offset;
        }
        for (int i = 0; i < bit; i++) {
            if (node > this.nodeCount) {
                break;
            }
            node = this.readNode(node, 1 & ((0xFF & binary[i / 8]) >> 7 - (i % 8)));
        }
        if (node > this.nodeCount) {
            return node;
        }
        throw new NotFoundException("ip not found");
    }

    private String resolve(int node) throws InvalidIpDataException {
        final int resoloved = node - this.nodeCount + this.nodeCount * 8;
        if (resoloved >= this.fileSize) {
            throw new InvalidIpDataException("database resolve error");
        }

        byte b = 0;
        int size = Long.valueOf(bytesToLong(
                b,
                b,
                this.data[resoloved],
                this.data[resoloved + 1]
        )).intValue();

        if (this.data.length < (resoloved + 2 + size)) {
            throw new InvalidIpDataException("database resolve error");
        }

        try {
            return new String(this.data, resoloved + 2, size, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidIpDataException("database resolve error");
        }
    }

    private int readNode(int node, int index) {
        int off = node * 8 + index * 4;

        return Long.valueOf(bytesToLong(
                this.data[off],
                this.data[off + 1],
                this.data[off + 2],
                this.data[off + 3]
        )).intValue();
    }

    private static long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff)));
    }

    private static long int2long(int i) {
        long l = i & 0x7fffffffL;
        if (i < 0) {
            l |= 0x080000000L;
        }
        return l;
    }

    public boolean isIPv4() {
        return (this.meta.ip_version & 0x01) == 0x01;
    }

    public boolean isIPv6() {
        return (this.meta.ip_version & 0x02) == 0x02;
    }

    public int getBuildUTCTime() {
        return this.meta.build;
    }

    public String[] getSupportFields() {
        return this.meta.fields;
    }

    public String getSupportLanguages() {
        return this.meta.languages.keySet().toString();
    }
}
