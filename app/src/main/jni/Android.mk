LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ndktest-jni
LOCAL_SRC_FILES := ndktest.c
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)