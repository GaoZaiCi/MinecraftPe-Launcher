package com.wglink.minecraftpe.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;


@SuppressLint("UnsafeDynamicallyLoadedCode")
public class Patcher {
    private static final String TAG = "Launcher";
    private static final String PLUGIN_DIR = "DexDir";
    private static final String PLUGIN_CACHE_DIR = "DexCacheDir";

    public static void installPlugin(Context context, ClassLoader classLoader) throws IOException, PackageManager.NameNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, InstantiationException {
        List<File> list = new ArrayList<>();
        ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.mojang.minecraftpe", 0);
        ZipFile zipFile = new ZipFile(info.sourceDir);
        File dex = new File(new File(context.getFilesDir(), PLUGIN_DIR), "classes.dex");
        writeFile(zipFile.getInputStream(zipFile.getEntry("classes.dex")), dex);
        for (int i = 2; i < 10; i++) {
            String name = String.format("classes%d.dex", i);
            ZipEntry entry = zipFile.getEntry(name);
            if (entry == null) {
                break;
            } else {
                File file = new File(new File(context.getFilesDir(), PLUGIN_DIR), name);
                writeFile(zipFile.getInputStream(entry), file);
                list.add(file);
            }
        }
        zipFile.close();
        list.add(dex);
        V23.install(classLoader, list, new File(context.getFilesDir(), PLUGIN_CACHE_DIR));
        patchNativeLibraryDir(classLoader, info.nativeLibraryDir);
    }

    private static void writeFile(InputStream inputStream, File pathFile) throws IOException {
        File parentFile = pathFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        } else if (pathFile.exists()) {
            pathFile.delete();
        }
        pathFile.createNewFile();
        FileOutputStream out = new FileOutputStream(pathFile);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.close();
    }

    public static final class V23 {
        static void install(ClassLoader classLoader, List<? extends File> list, File file) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            IOException[] iOExceptionArr;
            Object obj = findField(classLoader, "pathList").get(classLoader);
            ArrayList<IOException> arrayList = new ArrayList<>();
            expandFieldArray(obj, makeDexElements(obj, new ArrayList(list), file, arrayList));
            if (arrayList.size() > 0) {
                for (Object o : arrayList) {
                    Log.w(TAG, "Exception in makeDexElement", (IOException) o);
                }
                try {
                    Field a = findField(classLoader, "dexElementsSuppressedExceptions");
                    IOException[] iOExceptionArr2 = (IOException[]) a.get(classLoader);
                    if (iOExceptionArr2 == null) {
                        iOExceptionArr = arrayList.toArray(new IOException[0]);
                    } else {
                        IOException[] iOExceptionArr3 = new IOException[(arrayList.size() + iOExceptionArr2.length)];
                        arrayList.toArray(iOExceptionArr3);
                        System.arraycopy(iOExceptionArr2, 0, iOExceptionArr3, arrayList.size(), iOExceptionArr2.length);
                        iOExceptionArr = iOExceptionArr3;
                    }
                    a.set(classLoader, iOExceptionArr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "no dexElementsSuppressedExceptions parameter for v23!");
                }
            }
        }

        private static Object[] makeDexElements(Object obj, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return (Object[]) mFindField(obj, List.class, File.class, List.class).invoke(obj, new Object[]{arrayList, file, arrayList2});
        }

        private static Method mFindField(Object obj, Class<?>... clsArr) throws NoSuchMethodException {
            Class cls = obj.getClass();
            while (cls != null) {
                try {
                    Method declaredMethod = cls.getDeclaredMethod("makePathElements", clsArr);
                    if (!declaredMethod.isAccessible()) {
                        declaredMethod.setAccessible(true);
                    }
                    return declaredMethod;
                } catch (NoSuchMethodException unused) {
                    cls = cls.getSuperclass();
                }
            }
            throw new NoSuchMethodException("Method " + "makePathElements" + " with parameters " + Arrays.asList(clsArr) + " not found in " + obj.getClass());
        }

        private static Field findField(Object instance, String name) throws NoSuchFieldException {
            for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Field field = clazz.getDeclaredField(name);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                    // ignore and search next
                }
            }
            throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
        }

        private static void expandFieldArray(Object instance, Object[] extraElements) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            Field jlrField = findField(instance, "dexElements");
            Object[] original = (Object[]) jlrField.get(instance);
            Class<?> clazz = original.getClass().getComponentType();
            Object[] combined = (Object[]) Array.newInstance(clazz
                    , original.length + extraElements.length);
            System.arraycopy(original, 0, combined, 0, original.length);
            System.arraycopy(extraElements, 0, combined, original.length, extraElements.length);
            jlrField.set(instance, combined);
        }
    }

    public static void patchNativeLibraryDir(ClassLoader classLoader, String nativeLibraryPath) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        PathClassLoader pathClassLoader = (PathClassLoader) classLoader;
        if (Build.VERSION.SDK_INT <= 22) {
            Field fieldPathList = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
            fieldPathList.setAccessible(true);
            Object pathList = fieldPathList.get(pathClassLoader);
            Field nativeLibraryDirectories = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            nativeLibraryDirectories.setAccessible(true);
            File[] files = (File[]) nativeLibraryDirectories.get(pathList);
            Object newFiles = Array.newInstance(File.class, files.length + 1);
            Array.set(newFiles, 0, new File(nativeLibraryPath));
            for (int i = 1; i < files.length + 1; i++) {
                Array.set(newFiles, i, files[i - 1]);
            }
            nativeLibraryDirectories.set(pathList, newFiles);
        } else if (Build.VERSION.SDK_INT <= 25) {
            Class<?> classBaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field fieldPathList = classBaseDexClassLoader.getDeclaredField("pathList");
            fieldPathList.setAccessible(true);
            Object pathList = fieldPathList.get(pathClassLoader);

            Class<?> nativeLibraryElementClass = Class.forName("dalvik.system.DexPathList$Element");
            Constructor<?> element = nativeLibraryElementClass.getConstructor(File.class, boolean.class, File.class, DexFile.class);
            Field systemNativeLibraryDirectories = pathList.getClass()
                    .getDeclaredField("systemNativeLibraryDirectories");
            Field nativeLibraryDirectories = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            Field nativeLibraryPathElements = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            systemNativeLibraryDirectories.setAccessible(true);
            nativeLibraryDirectories.setAccessible(true);
            nativeLibraryPathElements.setAccessible(true);
            List<File> systemFiles = (List<File>) systemNativeLibraryDirectories.get(pathList);
            List<File> nativeFiles = (List<File>) nativeLibraryDirectories.get(pathList);
            Object[] elementFiles = (Object[]) nativeLibraryPathElements.get(pathList);
            Object newElementFiles = Array.newInstance(nativeLibraryElementClass, elementFiles.length + 1);

            systemFiles.add(new File(nativeLibraryPath));
            nativeFiles.add(new File(nativeLibraryPath));

            systemNativeLibraryDirectories.set(pathList, systemFiles);
            nativeLibraryDirectories.set(pathList, nativeFiles);
            element.setAccessible(true);
            Object newInstance = element.newInstance(new File(nativeLibraryPath), true, null, null);
            Array.set(newElementFiles, 0, newInstance);
            for (int i = 1; i < elementFiles.length + 1; i++) {
                Array.set(newElementFiles, i, elementFiles[i - 1]);
            }
            nativeLibraryPathElements.set(pathList, newElementFiles);
        } else {
            Class<?> classBaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field fieldPathList = classBaseDexClassLoader.getDeclaredField("pathList");
            fieldPathList.setAccessible(true);
            Object pathList = fieldPathList.get(pathClassLoader);

            Class<?> nativeLibraryElementClass = Class.forName("dalvik.system.DexPathList$NativeLibraryElement");
            Constructor<?> element = nativeLibraryElementClass.getConstructor(File.class);
            Field systemNativeLibraryDirectories = pathList.getClass()
                    .getDeclaredField("systemNativeLibraryDirectories");
            Field nativeLibraryDirectories = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            Field nativeLibraryPathElements = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            systemNativeLibraryDirectories.setAccessible(true);
            nativeLibraryDirectories.setAccessible(true);
            nativeLibraryPathElements.setAccessible(true);
            List<File> systemFiles = (List<File>) systemNativeLibraryDirectories.get(pathList);
            List<File> nativeFiles = (List<File>) nativeLibraryDirectories.get(pathList);
            Object[] elementFiles = (Object[]) nativeLibraryPathElements.get(pathList);
            Object newElementFiles = Array.newInstance(nativeLibraryElementClass, elementFiles.length + 1);

            systemFiles.add(new File(nativeLibraryPath));
            nativeFiles.add(new File(nativeLibraryPath));

            systemNativeLibraryDirectories.set(pathList, systemFiles);
            nativeLibraryDirectories.set(pathList, nativeFiles);
            element.setAccessible(true);
            Object newInstance = element.newInstance(new File(nativeLibraryPath));
            Array.set(newElementFiles, 0, newInstance);
            for (int i = 1; i < elementFiles.length + 1; i++) {
                Array.set(newElementFiles, i, elementFiles[i - 1]);
            }
            nativeLibraryPathElements.set(pathList, newElementFiles);
        }
    }

}
