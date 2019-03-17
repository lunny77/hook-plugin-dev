package com.dev.lunny.plugin1;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.dev.lunny.basecomponent.ResHelper;

public class Plugin1ResHelper implements ResHelper {
    @Override
    public String getWelcome() {
        return "Welcome to plugin1.";
    }

    @Override
    public Drawable getSplashImage(Context context) {
        return context.getResources().getDrawable(R.drawable.gakki);
    }
}
