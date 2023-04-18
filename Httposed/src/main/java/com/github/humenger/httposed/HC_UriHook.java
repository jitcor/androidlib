package com.github.humenger.httposed;

import android.net.Uri;

import com.github.humenger.httposed.callbacks.HCallback;
import com.github.humenger.httposed.callbacks.IHUnhook;

import java.lang.reflect.Member;

public abstract class HC_UriHook extends HCallback {
    public static final String TAG = "HC_UriHook";
    public HC_UriHook(){
        super();
    }
    public HC_UriHook(int priority) {
        super(priority);
    }
    /**
     * Called before the invocation of the method.
     *
     * <p>You can use {@link MethodHookParam#setResult} and {@link MethodHookParam#setThrowable}
     * to prevent the original method from being called.
     *
     * <p>Note that implementations shouldn't call {@code super(param)}, it's not necessary.
     *
     * @param param Information about the method call.
     * @throws Throwable Everything the callback throws is caught and logged.
     */
    protected void beforeHookedRequest(MethodHookParam param) throws Throwable {}

    /**
     * Called after the invocation of the method.
     *
     * <p>You can use {@link MethodHookParam#setResult} and {@link MethodHookParam#setThrowable}
     * to modify the return value of the original method.
     *
     * <p>Note that implementations shouldn't call {@code super(param)}, it's not necessary.
     *
     * @param param Information about the method call.
     * @throws Throwable Everything the callback throws is caught and logged.
     */
    protected void afterHookedResponse(MethodHookParam param) throws Throwable {}
    public static final class HResponse{

    }
    public static final class HRequest{

    }
    /**
     * Wraps information about the method call and allows to influence it.
     */
    public static final class MethodHookParam extends HCallback.Param {
        /** @hide */
        @SuppressWarnings("deprecation")
        public MethodHookParam() {
            super();
        }

        /** The hooked method/constructor. */
        public Member method;

        /** The {@code this} reference for an instance method, or {@code null} for static methods. */
        public Object thisObject;

        /** Arguments to the method call. */
        public HRequest request;

        private HResponse result = null;
        private Throwable throwable = null;
        /* package */ boolean returnEarly = false;

        /** Returns the result of the method call. */
        public HResponse getResult() {
            return result;
        }

        /**
         * Modify the result of the method call.
         *
         * <p>If called from {@link #beforeHookedRequest}, it prevents the call to the original method.
         */
        public void setResult(HResponse result) {
            this.result = result;
            this.throwable = null;
            this.returnEarly = true;
        }

        /** Returns the {@link Throwable} thrown by the method, or {@code null}. */
        public Throwable getThrowable() {
            return throwable;
        }

        /** Returns true if an exception was thrown by the method. */
        public boolean hasThrowable() {
            return throwable != null;
        }

        /**
         * Modify the exception thrown of the method call.
         *
         * <p>If called from {@link #beforeHookedRequest}, it prevents the call to the original method.
         */
        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
            this.result = null;
            this.returnEarly = true;
        }

        /** Returns the result of the method call, or throws the Throwable caused by it. */
        public Object getResultOrThrowable() throws Throwable {
            if (throwable != null)
                throw throwable;
            return result;
        }
    }
    /**
     * An object with which the method/constructor can be unhooked.
     */
    public class Unhook implements IHUnhook<HC_UriHook> {
        private final Uri hookUri;

        /*package*/ Unhook(Uri hookUri) {
            this.hookUri = hookUri;
        }

        /**
         * Returns the method/constructor that has been hooked.
         */
        public Uri getHookedUri() {
            return hookUri;
        }

        @Override
        public HC_UriHook getCallback() {
            return HC_UriHook.this;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void unhook() {
            HttposedBridge.unhookUri(hookUri, HC_UriHook.this);
        }

    }
}
