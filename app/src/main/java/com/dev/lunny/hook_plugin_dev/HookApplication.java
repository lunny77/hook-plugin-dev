package com.dev.lunny.hook_plugin_dev;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class HookApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
