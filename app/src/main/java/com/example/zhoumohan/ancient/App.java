package com.example.zhoumohan.ancient;

import android.app.Application;

import com.zhoumohan.common_library.globalNetWorkChange.NetworkManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkManager.getDefault().init(this);
    }
            }
