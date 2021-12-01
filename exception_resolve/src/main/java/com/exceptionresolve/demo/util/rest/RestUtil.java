package com.exceptionresolve.demo.util.rest;

import com.alibaba.fastjson.JSONObject;
import com.exceptionresolve.demo.entity.ResponseVo;
import com.exceptionresolve.demo.util.constants.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author fangzhongwei
 * 
 */
public final class RestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RestUtil.class);

    private static Boolean supporteForJsCrossDomain = Boolean.TRUE;

    private RestUtil() {
    }

    public static void write(HttpServletResponse response, String data) {
        setRespHeader(response);
        try {
            PrintWriter writer = getWriter(response);
            writer.write(RestCodec.encodeBase64(data));
            flush(writer);
        } catch (IOException e) {
            LOG.error("Write response data failed.", e);
        }
    }

    public static void write(HttpServletResponse response, ResponseVo responseVo) {
        write(response, responseVo, false);
    }

    public static void write(HttpServletResponse response, ResponseVo responseVo, boolean isAjaxFileUpload) {
        if (isAjaxFileUpload) {
            setRespHeaderForAjaxFileUpload(response);
        } else {
            setRespHeader(response);
        }
        try {
            PrintWriter writer = getWriter(response);
            writer.write(JSONObject.toJSONString(responseVo));
            flush(writer);
        } catch (IOException e) {
            LOG.error("Write response data failed.", e);
        }
    }

    public static void print(HttpServletResponse response, String data) {
        setRespHeader(response);
        try {
            PrintWriter writer = getWriter(response);
            writer.print(data);
            flush(writer);
        } catch (IOException e) {
            LOG.error("Write response data failed.", e);
        }
    }

    public static String getRestData(HttpServletRequest request, HttpServletResponse response) {
        try {
            return new RestServer(request, response).getRestData();
        } catch (RestException e) {
            LOG.error("Get request rest data failed.", e);
            return null;
        }
    }

    public static String getRestDataForUrlEncodedContentType(HttpServletRequest request, HttpServletResponse response) {
        return new  RestServer(request, response).getRestDataForUrlEncodedContentType();
    }

    public static void print(HttpServletResponse response, ResponseVo responseVo) {
        setRespHeader(response);
        try {
            PrintWriter writer = getWriter(response);
            writer.print(JSONObject.toJSONString(responseVo));
            flush(writer);
        } catch (IOException e) {
            LOG.error("Write response data failed.", e);
        }
    }

    public static String execute(String serverUrl) {
        return execute(serverUrl, null);
    }

    public static String execute(String serverUrl, String clientData) {
        return execute(serverUrl, SystemConstants.HTTP_GET, clientData);
    }

    public static String execute(String serverUrl, String httpMethod, String clientData) {
        return execute(serverUrl, httpMethod, clientData, SystemConstants.REST_CONNCTION_TIMEOUT,
                SystemConstants.REST_SOCKET_TIMEOUT);
    }

    public static String execute(String serverUrl, String httpMethod, String clientData, boolean encryptFlag) {
        return execute(serverUrl, httpMethod, clientData, SystemConstants.REST_CONNCTION_TIMEOUT,
                SystemConstants.REST_SOCKET_TIMEOUT, encryptFlag);
    }

    /**
     * http client调用，默认对参数不加密
     * 
     * @param serverUrl
     *            请求url
     * @param httpMethod
     *            请求方法
     * @param clientData
     *            请求参数
     * @param connectTimeout
     *            请求连接超时时间
     * @param socketTimeout
     *            响应超时时间
     * @return String http请求响应结果
     */
    public static String execute(String serverUrl, String httpMethod, String clientData, int connectTimeout,
            int socketTimeout) {
        return execute(serverUrl, httpMethod, clientData, connectTimeout, socketTimeout, false);
    }

    public static String execute(String serverUrl, String httpMethod, String clientData, int connectTimeout, int socketTimeout,
            boolean encryptFlag) {
        LOG.info(
                "###=>Out Request Url:[{}], Http Method:[{}], Client Data:[{}], Connection Timeout:[{}], Socket Timeout:[{}], encryptFlag:[{}]###",
                serverUrl, httpMethod, clientData, Integer.valueOf(connectTimeout), Integer.valueOf(socketTimeout),
                Boolean.valueOf(encryptFlag));
        try {
            String value = new  RestClient(serverUrl, httpMethod, clientData, connectTimeout, socketTimeout, encryptFlag)
                    .execute();
            LOG.info("###<=Out Response value:[{}]###", value);
            return value;
        } catch ( RestException e) {
            LOG.error("Call Url: [" + serverUrl + "] failed.", e);
            return null;
        }
    }

    private static PrintWriter getWriter(HttpServletResponse response) throws IOException {
        return response.getWriter();
    }

    private static void flush(PrintWriter writer) {
        writer.flush();
    }

    private static void setRespHeader(HttpServletResponse response) {
        if (supporteForJsCrossDomain.booleanValue()) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }

        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
    }

    private static void setRespHeaderForAjaxFileUpload(HttpServletResponse response) {
        if (supporteForJsCrossDomain.booleanValue()) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }

        response.setHeader("Content-Type", "application/json;charset=UTF-8");
    }
}
