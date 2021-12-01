package com.exceptionresolve.demo.util.rest;


import com.exceptionresolve.demo.util.constants.SystemConstants;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;

import java.io.IOException;

/**
 * @author fangzhongwei
 * 
 */
@SuppressWarnings("deprecation")
public class RestClient {

    private String serverUrl;
    private String httpMethod;
    private String clientData;
    private int connectTimeout;
    private int socketTimeout;

    // 请求参数是否要加解密
    private boolean encryptFlag = false;

    public RestClient(String serverUrl) {
        this(serverUrl, SystemConstants.HTTP_GET, null, -1, -1);
    }

    public RestClient(String serverUrl, String clientData) {
        this(serverUrl, SystemConstants.HTTP_GET, clientData, -1, -1);
    }

    public RestClient(String serverUrl, String httpMethod, String clientData) {
        this(serverUrl, httpMethod, clientData, -1, -1);
    }

    public RestClient(String serverUrl, String httpMethod, String clientData, int connectTimeout, int socketTimeout) {
        this(serverUrl, httpMethod, clientData, connectTimeout, socketTimeout, false);
    }

    public RestClient(String serverUrl, String httpMethod, String clientData, int connectTimeout, int socketTimeout,
            boolean encryptFlag) {
        this.serverUrl = serverUrl;
        this.httpMethod = httpMethod;
        this.clientData = clientData;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.encryptFlag = encryptFlag;
    }

    /**
     * 发送rest请求到server端处理并返回结果
     * 
     * @return 请求返回的json字符串
     * @throws
     */
    public String execute() throws RestException {
        if (null == this.serverUrl) {
            return null;
        }
        if (encryptFlag) {
            this.clientData = RestCodec.encodeBase64(this.clientData);
            String ret = httpExecute();
            return RestCodec.decodeBase64(ret);
        } else {
            return httpExecute();
        }

    }

    private String httpExecute() throws RestException {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String ret = null;

        try {
            if (SystemConstants.HTTP_GET.equalsIgnoreCase(this.httpMethod)) {
                HttpGet httpGet = new HttpGet(getQueryUrl());
                setContentType(httpGet);
                ret = httpClient.execute(httpGet, responseHandler);

            } else if (SystemConstants.HTTP_POST.equalsIgnoreCase(this.httpMethod)) {
                HttpPost httpPost = new HttpPost(this.serverUrl);
                setContentType(httpPost);
                if (null != this.clientData) {
                    if (encryptFlag) {
                        httpPost.setEntity(new StringEntity(this.clientData));
                    } else {
                        httpPost.setEntity(new StringEntity(this.clientData, ContentType.APPLICATION_JSON));
                    }
                }
                ret = httpClient.execute(httpPost, responseHandler);

            } else if (SystemConstants.HTTP_PUT.equalsIgnoreCase(this.httpMethod)) {
                HttpPut httpPut = new HttpPut(this.serverUrl);
                setContentType(httpPut);
                httpPut.setEntity(new StringEntity(this.clientData));
                ret = httpClient.execute(httpPut, responseHandler);
            } else if (SystemConstants.HTTP_DELETE.equalsIgnoreCase(this.httpMethod)) {
                HttpDelete httpDelete = new HttpDelete(getQueryUrl());
                setContentType(httpDelete);
                ret = httpClient.execute(httpDelete, responseHandler);
            }
        } catch (ClientProtocolException e) {
            throw RestException.withError(e);
        } catch (IOException e) {
            throw RestException.withError(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return ret;
    }

    private String getQueryUrl() {
        String url = serverUrl;
        if (null != clientData) {
            if (serverUrl.endsWith(SystemConstants.INTERROGATION_MARK)) {
                url += clientData;
            } else {
                url += SystemConstants.INTERROGATION_MARK + clientData;
            }
        }
        return url;
    }

    private DefaultHttpClient getDefaultHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.protocol.content-charset", SystemConstants.UTF8_ENCODING);
        if (this.connectTimeout > 0) {
            httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(this.connectTimeout * 1000));
        }
        if (this.socketTimeout > 0) {
            httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.socketTimeout * 1000));
        }
        return httpClient;
    }

    private void setContentType(AbstractHttpMessage httpMsg) {
        if (!encryptFlag) {
            httpMsg.setHeader("Content-Type", "application/json;charset=UTF-8");
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isEncryptFlag() {
        return encryptFlag;
    }

    public void setEncryptFlag(boolean encryptFlag) {
        this.encryptFlag = encryptFlag;
    }

}
