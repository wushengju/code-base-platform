package com.wushengju.study.utils;

import com.wushengju.study.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密工具类
 *
 * @author Sunny
 * @version 1.0
 * @className Md5Util
 * @date 2019-11-21 20:02
 */
@Slf4j
public class Md5Util {

    public static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            log.error("init md5 instance error, ", ex);
        }
    }

    /**
     * 获取md5值,32位的md5值
     *
     * @param message
     * @return
     */
    public static String getMd5(String message) {
        String md5 = "";
        try {
            byte[] messageByte = message.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
            // 获得MD5字节数组,16*8=128位
            byte[] md5Byte = messageDigest.digest(messageByte);
            // 转换为16进制字符串
            md5 = bytesToHex(md5Byte);
        } catch (Exception e) {
            log.error("get md5 error ", e);
        }
        return md5;
    }

    /**
     * 二进制转换成十六进制
     *
     * @param bytes
     * @return
     */
    public static final String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        if (null == bytes || bytes.length == 0) {
            return hexStr.toString();
        }
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(getMd5("aaa"));
        System.out.println(getMd5("aaa").length());
    }
}
