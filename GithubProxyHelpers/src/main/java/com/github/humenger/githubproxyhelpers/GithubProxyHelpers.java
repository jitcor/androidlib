package com.github.humenger.githubproxyhelpers;

import android.os.AsyncTask;
import android.os.Build;

import com.qiniu.android.netdiag.Ping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GithubProxyHelpers {

    private static final int timeout=10*1000;//10s
    private static final List<ProxyRule> proxyRules=new ArrayList<>();
    static {
        proxyRules.add(new ProxyRule(){{id=0;
            match ="https://raw.githubusercontent.com/";replace="https://raw.fastgit.org/";mirrorHost="raw.fastgit.org";}});
        proxyRules.add(new ProxyRule(){{id=0;
            match ="https://raw.githubusercontent.com/";replace="https://gh.wget.cool/https://raw.githubusercontent.com/";mirrorHost="gh.wget.cool";}});
        proxyRules.add(new ProxyRule(){{id=0;
            match ="https://raw.githubusercontent.com/";replace="https://y8b4odqg.fast-github.ml/-----https://raw.githubusercontent.com/";mirrorHost="y8b4odqg.fast-github.ml";}});
        proxyRules.add(new ProxyRule(){{id=0;
            match ="https://raw.githubusercontent.com/";replace="https://ghproxy.com/https://raw.githubusercontent.com/";mirrorHost="ghproxy.com";}});
        proxyRules.add(new ProxyRule(){{id=0;
            match ="https://raw.githubusercontent.com/";replace="https://raw.staticdn.net/";mirrorHost="raw.staticdn.net";}});
        checkDelay();
    }

    private static void checkDelay() {
        for (ProxyRule proxyRule : proxyRules) {
           checkDelayImpl(proxyRule);
        }
    }

    private static void checkDelayImpl(ProxyRule proxyRule) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                PingV2 pingV2=new PingV2(InetAddress.getByName(proxyRule.mirrorHost), new PingV2.PingListener() {
                    int allTimeMs=0;
                    int failedCount=0;
                    @Override
                    public void onPing(long timeMs, int index) {
                        allTimeMs+=timeMs;
                        proxyRule.delayMs=allTimeMs/(index+1-failedCount);
                    }

                    @Override
                    public void onPingException(Exception e, int index) {
                        failedCount++;
                        proxyRule.pingFailedProportion=failedCount/10f*100;
                    }
                });
                pingV2.setCount(10);
                pingV2.setTimeoutMs(3000);
                AsyncTask.THREAD_POOL_EXECUTOR.execute(pingV2);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else {
            Ping.start(proxyRule.mirrorHost, 10, null, r -> {
                proxyRule.delayMs= (int) r.avg;
                proxyRule.pingFailedProportion=r.dropped/10f*100;
            });

        }
    }
    public static void init(){

    }
    public static void addProxyRule(ProxyRule rule){
        if(rule==null)return;
        checkDelayImpl(rule);
        proxyRules.add(rule);
    }

    public static String getProxyUrl(String originalUrl){
        Collections.sort(proxyRules);
        for (ProxyRule proxyRule : proxyRules) {
            if(proxyRule.isFailed())continue;
            return originalUrl.replace(proxyRule.match,proxyRule.replace);
        }
        return originalUrl;
    }

}
