package com.nmt.minhtu.doan;

import android.app.Application;

import com.nmt.minhtu.doan.data_local.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
