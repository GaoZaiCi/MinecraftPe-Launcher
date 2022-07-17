package com.wglink.minecraftpe.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SoLibraryLoader {
    private static final String TAG = "Launcher";
    private final Context context;
    private final List<String> packages = new ArrayList<>();
    private final List<String> libraries = new ArrayList<>();
    private final File libraryDir;

    public SoLibraryLoader(Context context) {
        this.context = context;
        this.libraryDir = context.getDir("lib", Context.MODE_PRIVATE);
        if (!this.libraryDir.exists()) {
            this.libraryDir.mkdirs();
        }
    }

    public void addSearchPackage(String packageName) {
        this.packages.add(packageName);
    }

    public void addLibrary(String libraryName) {
        this.libraries.add(libraryName);
    }

    public void load() {
        for (String packageName : this.packages) {
            try {
                loadLibrary(context.getPackageManager().getApplicationInfo(packageName, 0));
            } catch (PackageManager.NameNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    private void loadLibrary(ApplicationInfo info) throws IOException {
        for (String libName : this.libraries) {
            File file = new File(info.nativeLibraryDir, String.format("lib%s.so", libName));
            if (file.exists()) {
                try {
                    Log.i(TAG, String.format("app lib [%s] loading", libName));
                    System.load(file.getAbsolutePath());
                    Log.i(TAG, String.format("app lib [%s] loaded", libName));
                } catch (SecurityException | UnsatisfiedLinkError | NullPointerException e) {
                    Log.e(TAG, String.format("app load [%s] error", libName), e);
                }
            } else {
                ZipFile zipFile = new ZipFile(info.sourceDir);
                for (String name : info.splitSourceDirs){
                    if (name.contains("arm64_v8a")){
                        zipFile.close();
                        zipFile = new ZipFile(name);
                        break;
                    }
                }
                File soFile = new File(this.libraryDir, libName);
                ZipEntry entry = zipFile.getEntry(String.format("lib/arm64-v8a/lib%s.so", libName));
                if (entry != null) {
                    if (entry.getCrc() != getCRC32(soFile)) {
                        if (soFile.exists()) {
                            Log.i(TAG, String.format("lib [%s] delete %s", libName, soFile.delete() ? "success" : "fail"));
                        }
                        try {
                            InputStream inputStream = zipFile.getInputStream(entry);
                            writeFile(inputStream, soFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Log.i(TAG, String.format("lib [%s] loading", libName));
                        System.load(soFile.getAbsolutePath());
                        Log.i(TAG, String.format("lib [%s] loaded", libName));
                    } catch (SecurityException | UnsatisfiedLinkError | NullPointerException e) {
                        Log.e(TAG, String.format("load [%s] error", libName), e);
                    }
                }
                zipFile.close();
            }
        }
    }

    /**
     * 写出文件
     *
     * @param inputStream 输入流
     * @param pathFile    输出路径
     * @throws IOException e
     */
    private void writeFile(InputStream inputStream, File pathFile) throws IOException {
        FileOutputStream out = new FileOutputStream(pathFile);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.close();
    }

    /**
     * 获取文件CRC32码
     *
     * @return String
     */
    private long getCRC32(File file) {
        CRC32 crc32 = new CRC32();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                crc32.update(buffer, 0, length);
            }
            return crc32.getValue();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder {
        private final SoLibraryLoader loader;

        public Builder(Context context) {
            this.loader = new SoLibraryLoader(context);
        }

        public Builder addSearchPackage(String packageName) {
            this.loader.addSearchPackage(packageName);
            return this;
        }

        public Builder addLibrary(String libraryName) {
            this.loader.addLibrary(libraryName);
            return this;
        }

        public void load() {
            this.loader.load();
        }
    }
}
