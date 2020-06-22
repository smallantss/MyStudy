#include <stdio.h>
#include "com_example_mystudy_ndk_JNI.h"
#include <android/log.h>

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
JNIEXPORT jstring JNICALL Java_com_example_mystudy_ndk_JNI_helloFromNdk
  (JNIEnv * env, jobject obj){
    LOGD("c print");
    return (*env)->NewStringUTF(env,"I am from C");
}