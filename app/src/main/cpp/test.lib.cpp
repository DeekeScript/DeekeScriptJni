//
// Created by Administrator on 2024/12/2.
//
#include <sstream>
#include <iostream>
#include <jni.h>
#include "include/v8.h"
#include "include/v8-version.h"

extern "C" JNIEXPORT jstring JNICALL
Java_top_deeke_jni_MainActivity_exec(JNIEnv *env, jobject /* this */) {
    // Initialize V8.
    std::cout << "V8 Version: "
              << V8_MAJOR_VERSION << "."
              << V8_MINOR_VERSION << "."
              << V8_BUILD_NUMBER << "."
              << V8_PATCH_LEVEL << std::endl;
    std::string hello = "版本号是：";
    std::ostringstream oss;
    oss << hello << V8_MAJOR_VERSION << "." << V8_MINOR_VERSION << "." << V8_BUILD_NUMBER << "." <<
        V8_PATCH_LEVEL;
    return env->NewStringUTF(oss.str().c_str());
}
