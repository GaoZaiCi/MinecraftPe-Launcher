#include <android/native_activity.h>
#include "Launcher.h"

JNIEXPORT void ANativeActivity_onCreate(ANativeActivity *activity, void *savedState, size_t savedStateSize) {
    Launcher::get().init(activity->vm, activity->assetManager);
    Launcher::get().initGame(activity, savedState, savedStateSize);
}

shared_ptr<Launcher> Launcher::instance;

Launcher &Launcher::get() {
    if (instance == nullptr) {
        instance = make_shared<Launcher>();
    }
    return *instance;
}

Launcher::~Launcher() {
    dlclose(this->handle);
}

void Launcher::init(JavaVM *javaVm, AAssetManager *manager) {
    this->vm = javaVm;
    this->assetManager = manager;
    /*JNIEnv *env = getJNIEnv();
    jclass clazz = env->FindClass("com/wglink/minecraftpe/view/GLView");
    if (clazz){
        jint count = env->RegisterNatives(clazz, (JNINativeMethod[]) {{"onSurfaceCreated", "()V",   (void *) onSurfaceCreated},
                                                                      {"onSurfaceChanged", "(II)V", (void *) onSurfaceChanged},
                                                                      {"onDrawFrame",      "()V",   (void *) onDrawFrame}}, 3);
        LOGI("RegisterNatives %d",count);
    } else {
        LOGE("注册失败 空指针");
    }*/

    this->handle = dlopen("libminecraftpe.so", RTLD_LAZY);
    if (this->handle == nullptr) {
        LOGE("open error %s", dlerror());
        return;
    }
    this->hooks = make_shared<Hooks>(handle);
    //this->hooks->init();
}

void Launcher::initGame(ANativeActivity *activity, void *savedState, size_t savedStateSize) const {
    auto fun = (void (*)(ANativeActivity *, void *, size_t)) (dlsym(handle, "ANativeActivity_onCreate"));
    fun(activity, savedState, savedStateSize);
}

JNIEnv *Launcher::getJNIEnv() const {
    if (this->vm) {
        JNIEnv *env;
        this->vm->AttachCurrentThread(&env, nullptr);
        return env;
    }
    return nullptr;
}

void Launcher::onSurfaceCreated(JNIEnv *env, jobject thiz) {

}

void Launcher::onSurfaceChanged(JNIEnv *env, jobject thiz, jint width, jint height) {

}

void Launcher::onDrawFrame(JNIEnv *env, jobject thiz) {

}

bool Launcher::getAssetFile(const char *filename, void **outData, int64_t *outLen) const {
    AAsset *asset_descriptor = AAssetManager_open(this->assetManager, filename, AASSET_MODE_BUFFER);
    if (asset_descriptor) {
        *outLen = AAsset_getLength(asset_descriptor);
        *outData = malloc(*outLen);
        int64_t num_bytes_read = AAsset_read(asset_descriptor, *outData, *outLen);
        AAsset_close(asset_descriptor);
        assert(num_bytes_read == *outLen);
        return true;
    }
    return false;
}

