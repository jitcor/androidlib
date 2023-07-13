package com.github.humenger.loglite;

import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogLite {

     private static final ConcurrentMap<String, LogLite> logLiteCache = new ConcurrentHashMap<>();
     private final String tag;

     public LogLite(String tag) {
          this.tag = tag;
     }

     public static LogLite tag(String tag) {
          if (!logLiteCache.containsKey(tag)) {
               logLiteCache.put(tag, new LogLite(tag));
          }
          return logLiteCache.get(tag);
     }

     public void v(String format, Object... args) {
          Log.v(tag, String.format(format, args));
     }

     public void d(String format, Object... args) {
          Log.d(tag, String.format(format, args));
     }

     public void i(String format, Object... args) {
          Log.i(tag, String.format(format, args));
     }

     public void w(String format, Object... args) {
          Log.w(tag, String.format(format, args));
     }

     public void e(String format, Object... args) {
          Log.e(tag, String.format(format, args));
     }

     public void e(Throwable t, String format, Object... args) {
          Log.e(tag, String.format(format, args));
     }

     public void e(Throwable t) {
          Log.e(tag, t.getMessage(), t);
     }


}

