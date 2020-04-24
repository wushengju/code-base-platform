package com.wushengju.study.analysis.ip;

import lombok.Data;

/**
 * IP地址相关信息
 *
 * @author Sunny
 * @version 1.0
 * @className IpLocation
 * @date 2019-09-20 16:46
 */
@Data
public class IpLocation {
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 地区
     */
    private String area;


    public IpLocation() {
    }

    @Override
    public String toString() {
        return "IpLocation{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
