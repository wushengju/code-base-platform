package com.wushengju.study.analysis.ip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * IP查找类
 *
 * @author Sunny
 * @version 1.0
 * @className IpFinder
 * @date 2019-11-19 11:01
 */
@Slf4j
public class IpFinder {

    private static final String UNKNOWN_COUNTRY = "未知国家";
    private static final String filePath = "dat/ipipfree.ipdb";
    private static final IpFinder ipFinder = new IpFinder();

    private IpReader ipReader;

    private IpFinder() {
        if (StringUtils.isNotBlank(filePath)) {
            ipReader = new IpReader(filePath);
        }
    }

    public static IpFinder getInstance() {
        return ipFinder;
    }

    /**
     * 根据ip获取地址
     *
     * @param ip
     * @return
     */
    public IpLocation getIPLocation(String ip) {
        IpLocation location = new IpLocation();
        String[] result = null;
        try {
            result = ipReader.find(ip);
        } catch (IpFormatException e) {
            log.error("getIPLocation error,", e);
        } catch (InvalidIpDataException e) {
            log.error("getIPLocation error,", e);
        }
        if (result == null || result.length == 0) {
            return null;
        }
        location.setCountry(result[0]);
        location.setProvince(result[1]);
        location.setArea(result[2]);
        return location;
    }

    /**
     * 获取ip对应的地区
     * 若城市为空则返回省份
     * 若省份为空则返回国家
     *
     * @param ip
     * @return
     */
    public String getCountry(String ip) {
        IpLocation location = getIPLocation(ip);
        if (null == location) {
            return UNKNOWN_COUNTRY;
        }
        if (StringUtils.isNotBlank(location.getArea())) {
            return location.getArea();
        }
        if (StringUtils.isNotBlank(location.getProvince())) {
            return location.getProvince();
        }
        if (StringUtils.isNotBlank(location.getCountry())) {
            return location.getCountry();
        }
        return UNKNOWN_COUNTRY;
    }

    public static void main(String[] args) {
        String ip = "171.255.0.32";
        IpFinder ipFinder = IpFinder.getInstance();
        System.out.println(ipFinder.getIPLocation(ip));

    }
}
