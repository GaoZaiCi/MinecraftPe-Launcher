package com.mojang.minecraftpe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

@SuppressWarnings("JavaJniMissingFunction")
public class NotificationListenerService  {
    public static String getDeviceRegistrationToken() {
        return "";
    }

    public static void retrieveDeviceToken() {
    }

    public native void nativePushNotificationReceived(int i, String str, String str2, String str3);

}
