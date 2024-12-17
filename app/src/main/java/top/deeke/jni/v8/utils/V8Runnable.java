package top.deeke.jni.v8.utils;

import top.deeke.jni.v8.V8;

public interface V8Runnable {

    /**
     * Execute the code on the provided runtime.
     *
     * @param runtime The V8 runtime assigned to this runnable.
     */
    public void run(final V8 runtime);

}