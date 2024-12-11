
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <jni.h>
#include "v8.h"
#include "libplatform/libplatform.h"

#include <android/log.h>

#define LOG_TAG "debug"
#define LOGS(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


extern "C" JNIEXPORT jstring JNICALL
Java_top_deeke_jni_MainActivity_execScript(JNIEnv *env, jobject, jstring code, jstring path) {
    // Initialize V8.
    const char *pathCStr = env->GetStringUTFChars(path, nullptr);
    v8::V8::InitializeICUDefaultLocation(pathCStr);
    v8::V8::InitializeExternalStartupData(pathCStr);
    std::unique_ptr<v8::Platform> platform = v8::platform::NewDefaultPlatform();
    v8::V8::InitializePlatform(platform.get());
    v8::V8::Initialize();

    // Create a new Isolate and make it the current one.
    v8::Isolate::CreateParams create_params;
    create_params.array_buffer_allocator = v8::ArrayBuffer::Allocator::NewDefaultAllocator();
    v8::Isolate *isolate = v8::Isolate::New(create_params);
    {
        v8::Isolate::Scope isolate_scope(isolate);
        // Create a stack-allocated handle scope.
        v8::HandleScope handle_scope(isolate);
        // Create a new context.
        v8::Local<v8::Context> context = v8::Context::New(isolate);
        // Enter the context for compiling and running the hello world script.
        v8::Context::Scope context_scope(context);
        {
            const char *str = env->GetStringUTFChars(code, nullptr);
            // Create a string containing the JavaScript source code.
            //v8::Local<v8::String> source = v8::String::NewFromUtf8Literal(isolate, "let a = 1;a;");
            v8::Local<v8::String> source = v8::String::NewFromUtf8(
                    isolate,
                    str,
                    v8::NewStringType::kNormal
            ).ToLocalChecked();
            // Compile the source code.
            v8::Local<v8::Script> script = v8::Script::Compile(context, source).ToLocalChecked();
            // Run the script to get the result.
            v8::Local<v8::Value> result = script->Run(context).ToLocalChecked();
            // Convert the result to an UTF8 string and print it.
            v8::String::Utf8Value utf8(isolate, result);
            LOGS("%s\n", *utf8);
        }
    }
    // Dispose the isolate and tear down V8.
    isolate->Dispose();
    v8::V8::Dispose();
    v8::V8::DisposePlatform();
    delete create_params.array_buffer_allocator;
    return env->NewStringUTF("jiang");
}
