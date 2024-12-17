package top.deeke.jni.v8.utils;

public interface TypeAdapter {

    /**
     * A default adapter that if returned in {@link TypeAdapter#adapt(int, Object)}, will result
     * in the default type adaption.
     */
    public static final Object DEFAULT = new Object();

    /**
     * Adapt an object from V8 to Java.
     * <p>
     * If the value is a V8Value (V8Object) then it will be released after
     * this method is called. If you wish to retain the object, call
     * ((V8Value)value).twin();
     *
     * @param type  The Type of the object to be adapted.
     * @param value The V8 Object to be converted.
     * @return The adapted Java Object or {@link TypeAdapter#DEFAULT} for the default conversion.
     */
    public Object adapt(int type, Object value);

}
