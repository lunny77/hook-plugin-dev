package com.dev.lunny.hook_plugin_dev;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

public class HookManager {

    public static void hookStartActivity(Context context) {
        try {
            Field singletonField = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            Class singleClass = Class.forName("android.util.Singleton");
            Field instanceFile = singleClass.getDeclaredField("mInstance");
            instanceFile.setAccessible(true);
            Object instance = instanceFile.get(singleton);

            Class classInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{classInterface}, new ProxyActivityManagerHandler(context, instance));

            //set proxy object
            instanceFile.set(singleton, proxy);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void hookActivityThread() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
            Object activityThread = currentActivityThreadField.get(null);

            Field handlerField = activityThreadClass.getDeclaredField("mH");
            handlerField.setAccessible(true);
            Handler handler = (Handler) handlerField.get(activityThread);

            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            callbackField.set(handler, new HandlerCallbackProxy(handler));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setupPluginDex(Context context, File dexFile) {
        try {
            Field pathListField = DexClassLoader.class.getSuperclass().getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(context.getClassLoader());
            Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsField.get(pathList);
            Class elementClass = dexElements.getClass().getComponentType();
//            public Element(File dir, boolean isDirectory, File zip, DexFile dexFile) {
//            Constructor constructor = elementClass.getConstructor(new Class[]{File.class, boolean.class, File.class, DexFile.class});
            Constructor constructor = elementClass.getConstructor(new Class[]{DexFile.class});
            Object ele = constructor.newInstance(new Object[]{new DexFile(dexFile)});
            Object[] newDexElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);
            System.arraycopy(dexElements, 0, newDexElements, 0, dexElements.length);
            newDexElements[newDexElements.length - 1] = ele;

            dexElementsField.set(pathList, newDexElements);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getOptimizedPath(Context context) {
        File cacheDir = context.getCacheDir();
        return cacheDir.getAbsolutePath() + File.separator + "plugin-optimized";
    }

}
