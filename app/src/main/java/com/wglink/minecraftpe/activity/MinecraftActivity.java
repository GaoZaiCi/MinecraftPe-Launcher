package com.wglink.minecraftpe.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mojang.minecraftpe.MainActivity;
import com.wglink.minecraftpe.launcher.ImGuiManager;
import com.wglink.minecraftpe.util.SoLibraryLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MinecraftActivity extends MainActivity {
    private static final String MINECRAFT_PACKAGE_NAME = "com.mojang.minecraftpe";
    private Context mcContext;
    private ImGuiManager imGuiManager;

    @Override
    public void onCreate(Bundle bundle) {
        try {
            this.mcContext = createPackageContext(MINECRAFT_PACKAGE_NAME, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            ApplicationInfo info = getPackageManager().getApplicationInfo(MINECRAFT_PACKAGE_NAME, 0);
            if (info.splitSourceDirs != null) {
                Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
                for (String path : info.splitSourceDirs) {
                    method.invoke(mcContext.getAssets(), path);
                }
            }
            new SoLibraryLoader.Builder(this)
                    .addSearchPackage(MINECRAFT_PACKAGE_NAME)
                    .addLibrary("c++_shared")
                    .addLibrary("fmod")
                    .addLibrary("minecraftpe")
                    .load();
        } catch (PackageManager.NameNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "游戏未安装", Toast.LENGTH_LONG).show();
            finish();
        }
        super.onCreate(bundle);
        /*this.imGuiManager = new ImGuiManager(MinecraftActivity.this);
        this.textInputWidget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imGuiManager.addInputCharacter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/
    }

    @Override
    public Resources getResources() {
        if (mcContext != null) {
            return mcContext.getResources();
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (mcContext != null) {
            return mcContext.getAssets();
        }
        return super.getAssets();
    }

    @Override
    protected void onDestroy() {
        if (this.imGuiManager != null) {
            this.imGuiManager.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.imGuiManager != null) {
            this.imGuiManager.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}