package com.wushengju.study.analysis.ip;

import java.util.Map;

/**
 * IP地址元数据
 *
 * @author Sunny
 * @version 1.0
 * @className IpMetaData
 * @date 2019-11-19 10:42
 */
public class IpMetaData {

    public int build;
    public int ip_version;
    public int node_count;
    public Map<String, Integer> languages;
    public String[] fields;
    public int total_size;
}
