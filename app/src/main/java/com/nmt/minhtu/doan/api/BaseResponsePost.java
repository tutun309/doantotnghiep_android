package com.nmt.minhtu.doan.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponsePost<T> {
    @SerializedName("errCode")
    @Expose
    private int errCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("mess")
    @Expose
    private String mess;
    @SerializedName("data")
    private T data;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}