package com.wushengju.study.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串分割工具类
 *
 * @author Sunny
 * @version 1.0
 * @className StringSplitUtil
 * @date 2019-11-21 15:24
 */
public class StringSplitUtil {

    /**
     * 逗号分隔符
     **/
    public static final String COMMA_SEPARATE = ",";

    /**
     * 按照逗号分隔符对字符串进行分割，并以列表的形式返回
     *
     * @param value
     * @return
     */
    public static List<String> split(String value) {
        return split(value, COMMA_SEPARATE);
    }

    /**
     * 按照逗号分隔符对字符串进行分割，并以数组的形式返回
     *
     * @param value
     * @return
     */
    public static String[] splitToArray(String value) {
        return splitToArray(value, COMMA_SEPARATE);
    }

    /**
     * 按照指定的分隔符对字符串进行分割，并以列表的形式返回
     *
     * @param value
     * @param separate
     * @return
     */
    public static List<String> split(String value, String separate) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(separate)) {
            return new ArrayList<>();
        }
        return Arrays.asList(StringUtils.split(value, separate));
    }

    /**
     * 按照指定的分隔符对字符串进行分割，并以数组的形式返回
     *
     * @param value
     * @param separate
     * @return
     */
    public static final String[] splitToArray(String value, String separate) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(separate)) {
            return new String[0];
        }
        return StringUtils.split(value, separate);
    }
}
