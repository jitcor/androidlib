package com.github.humenger.httposed.callbacks;

public interface IHUnhook<T> {
    /**
     * Returns the callback that has been registered.
     */
    T getCallback();

    /**
     * Removes the callback.
     */
    void unhook();
}
