package com.dev.lunny.hook_plugin_dev;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dev.lunny.hook_plugin_dev.utils.Constants;

import java.io.File;

import dalvik.system.DexClassLoader;

public class BaseHostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadPluginActivity(String dexPath, String activityName) {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, getOptimizedDirectory(this), null, getClassLoader());
        try {
            Class activityClass = dexClassLoader.loadClass(activityName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getOptimizedDirectory(Context context) {
        File cacheFile = context.getCacheDir();
        File optimizedFile = new File(cacheFile, "optimized_dir");
        return optimizedFile.getAbsolutePath();
    }
}
