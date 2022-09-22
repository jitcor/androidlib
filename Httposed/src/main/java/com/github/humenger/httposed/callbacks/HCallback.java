package com.github.humenger.httposed.callbacks;

import android.os.Bundle;

import com.github.humenger.httposed.HttposedBridge;

import java.io.Serializable;

public abstract class HCallback implements Comparable<HCallback> {
    public static final String TAG = "HCallback";
    public final int priority;
    public HCallback(){
        this.priority=PRIORITY_DEFAULT;
    }
    public HCallback(int priority){
        this.priority=priority;
    }
    /**
     * Base class for Xposed callback parameters.
     */
    public static abstract class Param {
        /** @hide */
        public final Object[] callbacks;
        private Bundle extra;

        /** @deprecated This constructor can't be hidden for technical reasons. Nevertheless, don't use it! */
        @Deprecated
        protected Param() {
            callbacks = null;
        }

        /** @hide */
        protected Param(HttposedBridge.CopyOnWriteSortedSet<? extends HCallback> callbacks) {
            this.callbacks = callbacks.getSnapshot();
        }

        /**
         * This can be used to store any data for the scope of the callback.
         *
         * <p>Use this instead of instance variables, as it has a clear reference to e.g. each
         * separate call to a method, even when the same method is called recursively.
         *
         * @see #setObjectExtra
         * @see #getObjectExtra
         */
        public synchronized Bundle getExtra() {
            if (extra == null)
                extra = new Bundle();
            return extra;
        }

        /**
         * Returns an object stored with {@link #setObjectExtra}.
         */
        public Object getObjectExtra(String key) {
            Serializable o = getExtra().getSerializable(key);
            if (o instanceof SerializeWrapper)
                return ((SerializeWrapper) o).object;
            return null;
        }

        /**
         * Stores any object for the scope of the callback. For data types that support it, use
         * the {@link Bundle} returned by {@link #getExtra} instead.
         */
        public void setObjectExtra(String key, Object o) {
            getExtra().putSerializable(key, new SerializeWrapper(o));
        }

        private static class SerializeWrapper implements Serializable {
            private static final long serialVersionUID = 1L;
            private final Object object;
            public SerializeWrapper(Object o) {
                object = o;
            }
        }
    }
    /** @hide */
    @Override
    public int compareTo(HCallback other) {
        if (this == other)
            return 0;

        // order descending by priority
        if (other.priority != this.priority)
            return other.priority - this.priority;
            // then randomly
        else if (System.identityHashCode(this) < System.identityHashCode(other))
            return -1;
        else
            return 1;
    }

    /** The default priority, see {@link #priority}. */
    public static final int PRIORITY_DEFAULT = 50;

    /** Execute this callback late, see {@link #priority}. */
    public static final int PRIORITY_LOWEST = -10000;

    /** Execute this callback early, see {@link #priority}. */
    public static final int PRIORITY_HIGHEST = 10000;
}
