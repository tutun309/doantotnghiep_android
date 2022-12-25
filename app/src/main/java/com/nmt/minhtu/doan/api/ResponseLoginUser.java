package com.nmt.minhtu.doan.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nmt.minhtu.doan.model.User;

public class ResponseLoginUser {
    @SerializedName("errCode")
    @Expose
    private int errCode;
    @SerializedName("mess")
    @Expose
    private String mess;
    @SerializedName("user")
    @Expose
    private User user;

    public ResponseLoginUser(int errCode, String mess, User user) {
        this.errCode = errCode;
        this.mess = mess;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
