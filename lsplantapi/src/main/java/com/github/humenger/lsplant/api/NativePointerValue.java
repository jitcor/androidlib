package com.github.humenger.lsplant.api;

public class NativePointerValue {
    public static final String TAG = NativePointerValue.class.getSimpleName();

    private NativePointer pointer;
    private ObjectWrapper objectWrapper;

    public void setPointer(NativePointer v) {
        pointer = v;
    }

    public void setObjectWrapper(ObjectWrapper v) {
        objectWrapper = v;
    }

    public NativePointer getPointer() {
        return pointer == null ? new NativePointer(0) : pointer;
    }

    public ObjectWrapper getObjectWrapper() {
        return objectWrapper == null ? new ObjectWrapper(new NativePointer(0)) : objectWrapper;
    }

}
