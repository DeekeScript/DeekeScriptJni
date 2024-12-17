package top.deeke.jni.v8;

/**
 * A top-level exception used to indicate that a script failed. In most cases
 * a more specific exception will be thrown.
 */
public class V8RuntimeException extends RuntimeException {
    V8RuntimeException() {
    }

    V8RuntimeException(final String message) {
        super(message);
    }
}
