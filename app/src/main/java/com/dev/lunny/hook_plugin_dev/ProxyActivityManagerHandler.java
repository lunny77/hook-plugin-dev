package com.dev.lunny.hook_plugin_dev;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dev.lunny.hook_plugin_dev.utils.Constants;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyActivityManagerHandler implements InvocationHandler {
    private Context context;
    private Object activityManger;

    public ProxyActivityManagerHandler(Context context, Object activityManger) {
        this.context = context;
        this.activityManger = activityManger;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Log.d(Constants.TAG, "method startActivity() be hooked!");
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                }
            }
            Intent rawIntent = (Intent) args[index];
            Intent stubIntent = new Intent(context, StubActivity.class);
            stubIntent.putExtra(Constants.TAG_TARGET_ACTIVITY, rawIntent);
            args[index] = stubIntent;
        }
        return method.invoke(activityManger, args);
    }
}
