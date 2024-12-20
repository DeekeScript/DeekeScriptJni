package top.deeke.jni.v8;

public interface JavaVoidCallback {

    /**
     * Called when a JS Function invokes a the registered Java
     * method.
     *
     * @param receiver   The V8Object that the function was called on.
     * @param parameters The parameters passed to the JS Function. The
     *                   parameter array does not need to be released, by any objects accessed
     *                   from the array must be.
     */
    public void invoke(V8Object receiver, V8Array parameters);

}