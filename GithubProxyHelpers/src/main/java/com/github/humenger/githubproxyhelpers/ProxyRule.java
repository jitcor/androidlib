package com.github.humenger.githubproxyhelpers;

import android.util.Log;

import java.util.Map;

public class ProxyRule implements Comparable<ProxyRule>{
    public static final String TAG="ProxyRule";
    public int id;
    public String match;
    public String replace;
    public String originalHost;
    public String mirrorHost;
    public int delayMs=60*1000;
    public float pingFailedProportion;
    public Map<String,String> headers;

    @Override
    public int compareTo(ProxyRule proxyRule) {
        Log.d(TAG, "compareTo: this.delayMs:"+delayMs+",proxyRule.delayMs:"+proxyRule.delayMs);
        return delayMs-proxyRule.delayMs;
    }
    public boolean isFailed(){
        return pingFailedProportion>=80f;
    }

    public Map<String, String> getHeaders(Map<String,String> def) {
        return headers==null?def:headers;
    }
}
