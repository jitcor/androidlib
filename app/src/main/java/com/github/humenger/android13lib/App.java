package com.github.humenger.android13lib;

import android.app.Application;

import com.github.humenger.githubproxyhelpers.GithubProxyHelpers;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GithubProxyHelpers.init(App.this);
            }
        }).start();
    }
}
