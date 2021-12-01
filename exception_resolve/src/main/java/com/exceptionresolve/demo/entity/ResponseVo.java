package com.exceptionresolve.demo.entity;

/**
 * 返回实体
 *
 * @author lafe
 */
public class ResponseVo {

    private boolean success = true;

    private String errorCode;

    private String msg;

    private Object data;

    public static ResponseVo createResponse() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);

        return responseVo;
    }

    public static ResponseVo createNormalResponse(String data, String message) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setMsg(message);
        responseVo.setData(data);

        return responseVo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVo{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
