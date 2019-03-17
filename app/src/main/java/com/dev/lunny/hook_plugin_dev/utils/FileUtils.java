package com.dev.lunny.hook_plugin_dev.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileUtils {
    public static String[] plugins = {"plugin1-debug.apk"};

    public static void copyAssetPluginToCache(final Context context, final Callback callback) {
        Executor executor = Executors.newFixedThreadPool(plugins.length);
        for (final String plugin : plugins) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    writeFileStream(context, plugin);
                    callback.onComplete();
                }
            });
        }
    }

    private static void writeFileStream(Context context, String pluginName) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            inputStream = assetManager.open(pluginName);
            File file = new File(getPluginRepoPath(context), pluginName);
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 5];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getPluginFile(Context context) {
        return new File(getPluginRepoPath(context), plugins[0]);
    }

    public static File getPluginRepoPath(Context context) {
        File file = context.getCacheDir();
        File pluginRepo = new File(file, "plugin-repo");
        if (!pluginRepo.exists()) {
            pluginRepo.mkdir();
        }
        return pluginRepo;
    }

    public interface Callback {
        void onComplete();
    }
}
