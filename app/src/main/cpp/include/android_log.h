

#ifndef MINECRAFTPE_LAUNCHER_ANDROID_LOG_H
#define MINECRAFTPE_LAUNCHER_ANDROID_LOG_H

#include <android/log.h>

#define TAG "Launcher"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)


#endif //MINECRAFTPE_LAUNCHER_ANDROID_LOG_H
