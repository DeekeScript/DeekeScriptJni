//
// Created by Administrator on 2024/12/12.
//extern "C" JNIEXPORT jstring JNICALL
#include <jni.h>
#include "v8.h"
#include "libplatform/libplatform.h"

extern "C" JNIEXPORT jstring JNICALL
Java_top_deeke_jni_MainActivity_execScriptConsole(JNIEnv *env, jobject /* this */, jstring path,
                                                  jstring code) {
    const char *pathCStr = env->GetStringUTFChars(path, nullptr);
    v8::V8::InitializeICUDefaultLocation(pathCStr);
    v8::V8::InitializeExternalStartupData(pathCStr);
    std::unique_ptr <v8::Platform> platform = v8::platform::NewDefaultPlatform();
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
        v8::Local < v8::Context > context = v8::Context::New(isolate);
        // Enter the context for compiling and running the hello world script.
        v8::Context::Scope context_scope(context);
        {
            const char *str = env->GetStringUTFChars(code, nullptr);
            // Create a string containing the JavaScript source code.
            //v8::Local<v8::String> source = v8::String::NewFromUtf8Literal(isolate, "let a = 1;a;");
            v8::Local < v8::String > source = v8::String::NewFromUtf8(
                    isolate,
                    str,
                    v8::NewStringType::kNormal
            ).ToLocalChecked();
            // Compile the source code.
            v8::Local < v8::Script > script = v8::Script::Compile(context, source).ToLocalChecked();
            // Run the script to get the result.
            v8::Local < v8::Value > result = script->Run(context).ToLocalChecked();
            // Convert the result to an UTF8 string and print it.
            v8::String::Utf8Value utf8(isolate, result);
            //LOGS("%s\n", *utf8);

            // 获取 Java 方法 ID
            jclass cls = env->FindClass("top/deeke/jni/v8/Console");
            jmethodID logMethod = env->GetStaticMethodID(cls, "log", "(Ljava/lang/String;)V");

            jstring message = env->NewStringUTF("测试");
            env->CallStaticVoidMethod(cls, logMethod, message);
        }
    }
    // Dispose the isolate and tear down V8.
    isolate->Dispose();
    v8::V8::Dispose();
    v8::V8::DisposePlatform();
    delete create_params.array_buffer_allocator;
    return env->NewStringUTF("jiang");
}
