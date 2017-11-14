package com.hehe.common.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Date: 2017/9/17
 * Time: 2:55
 * email: qinghe101@qq.com
 * Author: hehe
 */
public class  Response<T> implements Serializable {
    private static final long serialVersionUID = 4569349385519196012L;

    private boolean success;
    private T result;
    private String error;

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("success", this.success).add("result", this.result).add("error", this.error).omitNullValues().toString();
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> ok() {
        return ok((T)null);
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        return resp;
    }

}
