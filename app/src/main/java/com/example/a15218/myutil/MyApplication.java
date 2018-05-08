package com.example.a15218.myutil;

import android.app.Application;
import android.content.Context;

/**
 * Created by 15218 on 2018/4/5.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
