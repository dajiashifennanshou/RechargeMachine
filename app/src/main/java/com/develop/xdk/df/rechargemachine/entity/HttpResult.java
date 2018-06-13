package com.develop.xdk.df.rechargemachine.entity;

/**
 * 返回接口统一数据格式
 */
public class HttpResult<T> {

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" code=" + code + " msg=" + msg);
        if (null != data) {
            sb.append(" data:" + data.toString());
        }
        return sb.toString();
    }
}
