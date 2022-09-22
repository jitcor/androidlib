package com.github.humenger.android13lib;

import android.net.Uri;

import com.github.humenger.httposed.HC_MethodHook;
import com.github.humenger.httposed.HttposedHelpers;
import com.github.humenger.httposed.IHttposedInit;
import com.github.humenger.httposed.callbacks.HC_HookInit;
import com.github.humenger.httposed.callbacks.HCallback;

public class ExampleHttposedModule implements IHttposedInit {
    public static final String TAG = "ExampleHttposedModule";
    @Override
    public void handleInit(HC_HookInit.HookInitParam hookInitParam) throws Throwable {
        HttposedHelpers.hookMethod("cc.forensix.cn", "/", new HC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Uri.parse(param.uri);
            }
        });
    }
}
