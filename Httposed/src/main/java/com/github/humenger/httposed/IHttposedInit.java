package com.github.humenger.httposed;

import com.github.humenger.httposed.callbacks.HC_HookInit;

public interface IHttposedInit {
    void handleInit(HC_HookInit.HookInitParam hookInitParam) throws Throwable;
}
