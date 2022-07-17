package com.mojang.minecraftpe;

public class CrashManager {
    private static CrashManager mCrashManager;

    public static CrashManager getInstance() {
        if (mCrashManager==null){
            mCrashManager = new CrashManager();
        }
        return mCrashManager;
    }
    public String getCrashUploadURI() {
        return "http://localhost";
    }

    public String getExceptionUploadURI() {
        return "http://localhost";
    }

    private String uploadCrashFile(String var1, String var2, String var3) {
        return null;
    }

}
