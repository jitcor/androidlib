package com.github.humenger.githubproxyhelpers;

import java.util.Map;

public class ProxyUrl {
    public String proxyUrl;
    public String originalUrl;
    public Map<String,String> headers;
    public Map<String, String> getHeaders(Map<String,String> def) {
        return headers==null?def:headers;
    }
}
