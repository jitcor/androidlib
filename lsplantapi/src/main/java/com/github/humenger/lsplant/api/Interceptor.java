package com.github.humenger.lsplant.api;

public class Interceptor {

    InvocationListener attach(NativePointerValue target, InvocationListenerCallbacks callbacksOrProbe, NativePointerValue data) {
        throw new RuntimeException("impl attach");
    }

    InvocationListener attach(NativePointerValue target, InstructionProbeCallback callbacksOrProbe, NativePointerValue data) {
        throw new RuntimeException("attach");
    }

    void detachAll() {

    }

    void replace(NativePointerValue target, NativePointerValue replacement, NativePointerValue data) {

    }

    void revert(NativePointerValue target) {

    }

    void flush() {

    }

}
