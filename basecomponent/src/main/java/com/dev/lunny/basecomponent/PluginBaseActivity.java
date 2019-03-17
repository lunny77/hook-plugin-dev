package com.dev.lunny.basecomponent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PluginBaseActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hook_plugin",TAG + "  onCreate():" + savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("hook_plugin",TAG + "  onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("hook_plugin",TAG + "  onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hook_plugin",TAG + "  onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("hook_plugin",TAG + "  onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("hook_plugin",TAG + "  onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("hook_plugin",TAG + "  onDestroy");
    }
}
