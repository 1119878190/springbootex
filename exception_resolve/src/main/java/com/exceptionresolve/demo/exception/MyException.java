package com.exceptionresolve.demo.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自定义异常
 *
 * @author lafe
 */
public class MyException extends RuntimeException {

    /**
     * 唯一标识异常
     */
    private String id;

    /**
     * 异常堆栈信息的字符串表示
     */
    private String causeStackTrace;

    public MyException() {
        super();
        this.genId();
    }

    public MyException(String message) {
        super(message);
        this.genId();
    }

    public MyException(Throwable cause) {
        super(cause);
        this.genId();
        this.setCauseStackTrace(cause);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
        this.genId();
        this.setCauseStackTrace(cause);
    }

    private void genId() {
        setId(Long.valueOf(System.currentTimeMillis() % Long.MAX_VALUE).toString());
    }

    private void setCauseStackTrace(Throwable cause) {
        if (null != cause) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            cause.printStackTrace(pw);
            setCauseStackTrace(sw.getBuffer().toString());
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCauseStackTrace() {
        return causeStackTrace;
    }

    public void setCauseStackTrace(String causeStackTrace) {
        this.causeStackTrace = causeStackTrace;
    }

    @Override
    public String toString() {
        return "EXCEPTIONID  " + getId() + " :" + super.getMessage();
    }
}
