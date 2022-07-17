package com.wglink.minecraftpe.launcher;


import android.app.Application;

import com.wglink.minecraftpe.util.Patcher;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Patcher.installPlugin(this, getClassLoader());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
