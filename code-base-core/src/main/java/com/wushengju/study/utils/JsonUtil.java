package com.wushengju.study.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于Jackson的Json工具类
 *
 * @author Sunny
 * @version 1.0
 * @className JsonUtil
 * @date 2019-11-21 15:19
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper;

    static {
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jf.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS);
        jf.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        jf.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        jf.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper = new ObjectMapper(jf);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * 将对象转化为json串
     *
     * @param value
     * @return
     */
    public static String toJson(Object value) {
        if (null == value) {
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Json write bean to string exception, ", e);
        }
        return null;
    }

    /**
     * 将json串转化为对象
     *
     * @param json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, TypeReference<T> obj) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, obj);
        } catch (Exception e) {
            log.error("Json string write to obj exception, ", e);
        }
        return null;
    }

    /**
     * 将json串转化为对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);

        } catch (Exception e) {
            log.error("Json string write to bean exception, ", e);
        }
        return null;
    }

    public static void main(String[] args) {
        log.error("1213{}", 1122);
        JsonUtil.toBean("ss", new TypeReference<String>() {
        });
    }
}
