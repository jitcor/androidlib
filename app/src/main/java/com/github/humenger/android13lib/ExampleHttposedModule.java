package com.github.humenger.android13lib;

import android.net.Uri;

import com.github.humenger.httposed.HC_UriHook;
import com.github.humenger.httposed.HttposedHelpers;
import com.github.humenger.httposed.IHttposedInit;
import com.github.humenger.httposed.callbacks.HC_HookInit;

public class ExampleHttposedModule implements IHttposedInit {
    public static final String TAG = "ExampleHttposedModule";
    @Override
    public void handleInit(HC_HookInit.HookInitParam hookInitParam) throws Throwable {
        HttposedHelpers.hookUri("cc.forensix.cn", "/", new HC_UriHook() {
            @Override
            protected void afterHookedResponse(MethodHookParam param) throws Throwable {
                super.afterHookedResponse(param);
//                Uri.parse(param.uri);
            }
        });
    }
}
