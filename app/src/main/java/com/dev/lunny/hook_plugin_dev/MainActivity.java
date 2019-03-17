package com.dev.lunny.hook_plugin_dev;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.lunny.basecomponent.ResHelper;
import com.dev.lunny.hook_plugin_dev.utils.Constants;
import com.dev.lunny.hook_plugin_dev.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class MainActivity extends BaseHostActivity implements FileUtils.Callback {

    private ImageView imageView;

    private String mDexPath;

    private Resources mResource;
    private AssetManager mAssetManager;
    private Resources.Theme mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView_main);
        FileUtils.copyAssetPluginToCache(this, this);

        findViewById(R.id.button_load_plugin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.dev.lunny.plugin1", "com.dev.lunny.plugin1.Plugin1MainActivity");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
    }

    /**
     * add plugin resource to assert manager.
     *
     * @param dexPath plugin's path
     */
    private void setupAssetManager(String dexPath) {
        Resources resources = super.getResources();
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
            mResource = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            mTheme = mResource.newTheme();
            mTheme.setTo(super.getTheme());

            Field themeField = ContextThemeWrapper.class.getDeclaredField("mTheme");
            themeField.setAccessible(true);
            themeField.set(this, mTheme);

            Field resourceField = ContextThemeWrapper.class.getDeclaredField("mResources");
            resourceField.setAccessible(true);
            resourceField.set(this, mResource);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        Log.d(Constants.TAG, "download plugin success!");
        HookManager.setupPluginDex(this, FileUtils.getPluginFile(this));
        HookManager.hookStartActivity(this);
        HookManager.hookActivityThread();
    }
}
