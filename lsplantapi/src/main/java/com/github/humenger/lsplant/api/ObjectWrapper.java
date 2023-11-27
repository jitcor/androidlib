package com.github.humenger.lsplant.api;

public class ObjectWrapper {
    public static final String TAG = ObjectWrapper.class.getSimpleName();
    public final NativePointer handle;

    public ObjectWrapper(NativePointer v) {
        handle = v;
    }

}
