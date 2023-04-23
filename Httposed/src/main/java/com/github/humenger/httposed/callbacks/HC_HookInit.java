package com.github.humenger.httposed.callbacks;

import android.content.pm.ApplicationInfo;

import com.github.humenger.httposed.HttposedBridge;

public abstract class HC_HookInit extends HCallback {
    public static final String TAG = "HC_HookInit";
    /**
     * Wraps information about the app being loaded.
     */
    public static final class HookInitParam extends HCallback.Param {
        /** @hide */
        public HookInitParam(HttposedBridge.CopyOnWriteSortedSet<HC_HookInit> callbacks) {
            super(callbacks);
        }

        public String host;

        /** The process in which the package is executed. */
        public int port;

        /** The ClassLoader used for this package. */
        public ClassLoader classLoader;

        /** More information about the application being loaded. */
        public ApplicationInfo appInfo;

        /** Set to {@code true} if this is the first (and main) application for this process. */
        public boolean isFirstApplication;
    }
}
