package com.wglink.minecraftpe;


import androidx.annotation.Keep;

public interface JNICallResult {
    @Keep
    Object onCallResult(Object... args);
}
