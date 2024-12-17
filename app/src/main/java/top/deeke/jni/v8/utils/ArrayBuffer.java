package top.deeke.jni.v8.utils;

import java.nio.ByteBuffer;

import top.deeke.jni.v8.V8;
import top.deeke.jni.v8.V8ArrayBuffer;

public class ArrayBuffer {

    private final V8ArrayBuffer arrayBuffer;

    ArrayBuffer(final V8ArrayBuffer arrayBuffer) {
        this.arrayBuffer = (V8ArrayBuffer) arrayBuffer.twin().setWeak();
    }

    /**
     * Create a new ArrayBuffer from a java.nio.ByteBuffer
     *
     * @param v8         the Runtime on which to create the ArrayBuffer
     * @param byteBuffer the ByteBuffer to use to back the ArrayBuffer
     */
    public ArrayBuffer(final V8 v8, final ByteBuffer byteBuffer) {
        try (V8ArrayBuffer v8ArrayBuffer = new V8ArrayBuffer(v8, byteBuffer)) {
            arrayBuffer = (V8ArrayBuffer) v8ArrayBuffer.twin().setWeak();
        }
    }

    /**
     * Determine if the underlying V8ArrayBuffer is still available, or if it's been cleaned up by the JavaScript
     * garbage collector.
     *
     * @return true if the underlying V8ArrayBuffer is still available, false otherwise.
     */
    public boolean isAvailable() {
        return !arrayBuffer.isReleased();
    }

    /**
     * Returns the underlying V8ArrayBuffer.
     *
     * @return the underlying V8ArrayBuffer.
     */
    public V8ArrayBuffer getV8ArrayBuffer() {
        return arrayBuffer.twin();
    }
}