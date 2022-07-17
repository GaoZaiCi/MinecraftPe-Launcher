/*
* 日期: 2022/4/6 12:15 星期三
* 工程: MinecraftPe-Launcher
*/
//

#ifndef MINECRAFTPE_LAUNCHER_LAUNCHER_H
#define MINECRAFTPE_LAUNCHER_LAUNCHER_H

#include <cstdint>
#include <memory>
#include <string>
#include <dlfcn.h>
#include <jni.h>
#include <android/asset_manager.h>
#include <android/native_activity.h>
#include "android_log.h"
#include "Hooks.h"

using namespace std;

class Launcher {
  public:
    static shared_ptr<Launcher> instance;
    AAssetManager *assetManager;
  public:
    JavaVM *vm;
    void *handle;
    shared_ptr<Hooks> hooks;
  public:
    static Launcher &get();

  public:
    ~Launcher();

    void init(JavaVM *javaVm, AAssetManager *manager);

    void initGame(ANativeActivity *activity, void *savedState, size_t savedStateSize) const;

    JNIEnv* getJNIEnv() const;

    bool getAssetFile(const char *filename, void **outData, int64_t *outLen) const;

  public:
    static void onSurfaceCreated(JNIEnv *env, jobject thiz);
    static void onSurfaceChanged(JNIEnv *env, jobject thiz, jint width, jint height);
    static void onDrawFrame(JNIEnv *env, jobject thiz);
};

#endif //MINECRAFTPE_LAUNCHER_LAUNCHER_H
