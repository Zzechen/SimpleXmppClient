package com.zzc.sample.simplexmppclient;

import android.app.Application;

/**
 * Created : zzc
 * Time : 2017/6/7
 * Email : zzc1259@163.com
 * Description : ${desc}
 */

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
