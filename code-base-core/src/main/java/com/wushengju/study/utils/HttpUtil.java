package com.wushengju.study.utils;

import com.wushengju.study.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Http工具类，发送get或post请求
 *
 * @author Sunny
 * @version 1.0
 * @className HttpUtil
 * @date 2019-11-21 16:23
 */
@Slf4j
public class HttpUtil {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * form data的Content-Type
     */
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    /**
     * XML的Content-Type
     */
    private static final String XML_CONTENT_TYPE = "text/xml; charset=UTF-8";
    /**
     * Json的Content-Type
     */
    private static final String JSON_CONTENT_TYPE = "application/json";
    /**
     * 参数分割符
     */
    private static final String[] PARAMETER_SEPARATOR = {"?", "=", "&"};
    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIME_OUT = 30000;
    /**
     * 读取超时时间
     */
    private static final int READ_TIME_OUT = 30000;

    /**
     * 设置默认配置,设置连接超时时间、读取超时时间
     */
    private static RequestConfig getDefaultConfigure() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECT_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(READ_TIME_OUT).build();
        return requestConfig;
    }

    /**
     * 发送get请求,不带参数
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 发送get请求,带参数
     *
     * @param url
     * @param param
     * @return
     */
    public static String get(String url, Map<String, Object> param) {
        String result = null;
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            url = url + spliceParameters(param);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity(), CommonConstant.DEFAULT_CHARSET_NAME);
            log.info("get url:{}, param:{}, result:{}", url, param, result);
        } catch (Exception e) {
            log.error("do get error, ", e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }

    /**
     * 发送post请求(不带参数)
     *
     * @param url
     * @return
     */
    public static String post(String url) {
        return post(url, null, null);
    }

    /**
     * 发送post请求(带参数)
     *
     * @param url   请求的URL
     * @param param 请求的参数
     * @return
     */
    public static String post(String url, Map<String, Object> param) {
        return post(url, param, null);
    }

    /**
     * 发送post请求(带参数且带header的)
     *
     * @param url   请求的URL
     * @param param 请求的参数
     * @return
     */
    public static String post(String url, Map<String, Object> param, Header header) {
        CloseableHttpResponse response = null;
        String result = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if (null != header) {
                httpPost.setHeader(header);
            }
            List<NameValuePair> params = conversionParameter(param);
            httpPost.setEntity(new UrlEncodedFormEntity(params, CommonConstant.DEFAULT_CHARSET_NAME));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), CommonConstant.DEFAULT_CHARSET_NAME);
            log.info("post url:{},param:{},result:{}", url, param);
        } catch (IOException e) {
            log.error("do post error:{}", e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }

    /**
     * 发送post请求,提交格式为json
     *
     * @param url
     * @param json
     * @return
     */
    public static String postJson(String url, String json) {
        log.info("post json url:{},json:{}", url, json);
        return postByType(url, JSON_CONTENT_TYPE, json);
    }

    /**
     * 发送post请求,提交格式为xml
     *
     * @param url
     * @param xml
     * @return
     */
    public static String postXml(String url, String xml) {
        log.info("post xml url:{},xml:{}", url, xml);
        return postByType(url, XML_CONTENT_TYPE, xml);
    }

    /**
     * 发送post请求,提交格式为x-www-form-urlencoded
     *
     * @param url
     * @param formData
     * @return
     */
    public static String postForm(String url, String formData) {
        log.info("post form url:{},formData:{}", url, formData);
        return postByType(url, FORM_CONTENT_TYPE, formData);
    }

    /**
     * 按照类型发送post请求
     *
     * @param url      请求地址
     * @param postType 请求类型
     * @param content
     * @return
     */
    private static String postByType(String url, String postType, String content) {
        log.info("post by type url:{},postType:{},content:{}", url, postType, content);
        Header header = new BasicHeader(HTTP.CONTENT_TYPE, postType);
        return postByType(url, header, content);
    }

    /**
     * 按照类型发送post请求
     *
     * @param url     请求地址
     * @param content
     * @return
     */
    private static String postByType(String url, Header header, String content) {
        log.info("post by type url:{},content:{}", url, url, content);
        CloseableHttpResponse response = null;
        String result = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(content, CommonConstant.DEFAULT_CHARSET_NAME);
            httpPost.setEntity(entity);
            httpPost.setHeader(header);
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            log.info("post by type result:{}", result);
        } catch (IOException e) {
            log.error("do post by type error:{}", e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }

    /**
     * 参数格式转换
     *
     * @param param
     * @return
     */
    private static List<NameValuePair> conversionParameter(Map<String, Object> param) {
        List<NameValuePair> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(param)) {
            return list;
        }
        try {
            param.forEach((key, value) -> {
                NameValuePair nameValuePair = new BasicNameValuePair(key, value.toString());
                list.add(nameValuePair);
            });
        } catch (Exception e) {
            log.error("conversion post parameter, ", e);
        }
        return list;
    }


    /**
     * get方法拼接参数使用
     *
     * @param param 参数
     * @return
     */
    private static String spliceParameters(Map<String, Object> param) {
        String result = "";
        if (CollectionUtils.isEmpty(param)) {
            return result;
        }

        StringBuilder builder = new StringBuilder(PARAMETER_SEPARATOR[0]);
        param.forEach((key, value) -> {
            String encodeValue = null;
            try {
                encodeValue = URLEncoder.encode(value.toString(), CommonConstant.DEFAULT_CHARSET_NAME);
                builder.append(key)
                        .append(PARAMETER_SEPARATOR[1])
                        .append(encodeValue)
                        .append(PARAMETER_SEPARATOR[2]);
            } catch (UnsupportedEncodingException e) {
                log.error("urlEncode error, ", e);
            }

        });
        result = builder.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 关闭资源方法
     *
     * @param response
     */
    private static void closeResource(CloseableHttpResponse response) {
        if (null == response) {
            return;
        }
        try {
            response.close();
        } catch (IOException e) {
            log.error("response close error,", e);
        }
    }

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("licence_id", 54);
        map.put("page_size", 1);
        map.put("page_num", 10);
        String url = "http://hwmedia-launcher-o.api.leiniao.com/overseas-media/luancher/v1/search";
        System.out.println(get(url, map));
    }
}
