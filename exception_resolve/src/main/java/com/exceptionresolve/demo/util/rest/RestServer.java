package com.exceptionresolve.demo.util.rest;

import com.alibaba.fastjson.JSONObject;
import com.exceptionresolve.demo.util.constants.SystemConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author fangzhongwei
 * 
 */
public class RestServer {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    public RestServer(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String getRestData() throws RestException {
        if (null == request) {
            return null;
        }

        String ret = getBodyData();
        return ret;
    }

    public String getRestDataForUrlEncodedContentType() {
        if (null == request) {
            return null;
        }

        return getReqParam();
    }

    public String getReqParam() {
        Map reqParamMap = request.getParameterMap();

        return JSONObject.toJSONString(reqParamMap);
    }

    private String getBodyData() throws RestException {
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        // String line;
        char[] inputBuff = new char[1024];
        int len = -1;
        try {
            reader = request.getReader();
            while (-1 != (len = reader.read(inputBuff))) {
                buffer.append(inputBuff, 0, len);
            }
            // 解决Ajax的post请求参数长度大于reader的缓冲, 获取参数错误
            // while (null != (line = reader.readLine())) {
            // buffer.append(line);
            // }
            return buffer.toString();
        } catch (IOException e) {
            throw RestException.withError(e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw RestException.withError(e);
                }
            }
        }
    }

    public boolean sendRestData(String data) throws RestException {
        if (null == response) {
            return false;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding(SystemConstants.UTF8_ENCODING);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(data);
            return true;
        } catch (IOException e) {
            throw RestException.withError(e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

}
