package com.dev.lunny.hook_plugin_dev;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dev.lunny.hook_plugin_dev.utils.Constants;

import java.lang.reflect.Field;

public class HandlerCallbackProxy implements Handler.Callback {
    private Handler mH;

    public HandlerCallbackProxy(Handler h) {
        this.mH = h;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 100: //LAUNCH_ACTIVITY
                Log.d(Constants.TAG, "mH handleMessage(LAUNCH_ACTIVITY) has be hooked!");
                parseTargetIntent(msg.obj);
                break;
        }
        mH.handleMessage(msg);
        return true;
    }

    private void parseTargetIntent(Object activityClientRecord) {
        try {
            Field intentField = activityClientRecord.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent intent = (Intent) intentField.get(activityClientRecord);
            Intent target = intent.getParcelableExtra(Constants.TAG_TARGET_ACTIVITY);
            intent.setComponent(target.getComponent());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
