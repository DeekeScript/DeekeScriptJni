package top.deeke.jni.v8.utils;

public abstract class SingleTypeAdapter implements TypeAdapter {

    private final int typeToAdapt;

    /**
     * Create a SingleTypeAdapter
     *
     * @param typeToAdapt The V8 Type this TypeAdapter should be applied to.
     */
    public SingleTypeAdapter(final int typeToAdapt) {
        this.typeToAdapt = typeToAdapt;
    }

    /*
     * (non-Javadoc)
     * @see com.eclipsesource.v8.utils.TypeAdapter#adapt(int, java.lang.Object)
     */
    @Override
    public Object adapt(final int type, final Object value) {
        if (type == typeToAdapt) {
            return adapt(value);
        }
        return TypeAdapter.DEFAULT;
    }

    /**
     * Adapt an object from V8 to Java.
     *
     * If the value is a V8Value (V8Object) then it will be released after
     * this method is called. If you wish to retain the object, call
     * ((V8Value)value).twin();
     *
     * @param value The V8 Object to be converted.
     * @return The adapted Java Object or {@link TypeAdapter#DEFAULT} for the default conversion.
     */
    public abstract Object adapt(final Object value);

}
