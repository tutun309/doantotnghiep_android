package com.nmt.minhtu.doan.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePOST {
    @SerializedName("errCode")
    @Expose
    private int errCode;
    @SerializedName("mess")
    @Expose
    private String mess;

    public ResponsePOST(int errCode, String mess) {
        this.errCode = errCode;
        this.mess = mess;
    }

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

    @Override
    public String toString() {
        return "ResponsePOST{" +
                "errCode=" + errCode +
                ", mess='" + mess + '\'' +
                '}';
    }
}
