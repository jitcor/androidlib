package com.github.humenger.multidexhook;


import android.content.Context;
import android.content.ContextWrapper;

import com.github.humenger.loglite.LogLite;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class MultiDexHook extends XC_MethodHook {
     public static final String TAG = MultiDexHook.class.getSimpleName();
     private static MultiDexHook instance;
     private final CopyOnWriteArraySet<ClassLoader> classLoaderCache = new CopyOnWriteArraySet<>();
     private final CopyOnWriteArraySet<Observer> observers = new CopyOnWriteArraySet<>();

     private MultiDexHook() {
     }

     public static MultiDexHook getInstance() {
          if (instance == null) instance = new MultiDexHook();
          return instance;
     }

     public void init() {
          try {
               XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, this);
               XposedBridge.hookAllConstructors(ClassLoader.class, this);
               XposedHelpers.findAndHookMethod(Class.class, "forName", String.class, boolean.class, ClassLoader.class, this);
          } catch (Throwable e) {
               LogLite.tag(TAG).e(e);
          }
     }

     public Set<ClassLoader> getClassLoaders() {
          return Collections.unmodifiableSet(classLoaderCache);
     }

     public synchronized void register(Observer observer) {
          if (observer != null) {
               observers.add(observer);
               receiveClassLoaderAll(observer);
          }
     }

     private void receiveClassLoaderAll(Observer observer) {
          if (observer != null) {
               for (ClassLoader classLoader : classLoaderCache) {
                    observer.receive(classLoader);
               }
          }
     }

     public synchronized void unregister(Observer observer) {
          if (observer != null) {
               observers.remove(observer);
          }
     }


     @Override
     protected synchronized void beforeHookedMethod(MethodHookParam param) throws Throwable {
          super.beforeHookedMethod(param);
          if ("attachBaseContext".equals(param.method.getName())) {
               ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
               if (classLoader == null) return;
               LogLite.tag(TAG).i("attachBaseContext: %s", classLoader);
               classLoaderCache.add(classLoader);
               notifyObserverAll(classLoader);
          }

     }

     private void notifyObserverAll(ClassLoader classLoader) {
          for (Observer observer : observers) {
               observer.receive(classLoader);
          }
     }

     @Override
     protected synchronized void afterHookedMethod(MethodHookParam param) throws Throwable {
          super.afterHookedMethod(param);
          if ("forName".equals(param.method.getName())) {
               classLoaderCache.add((ClassLoader) param.args[2]);
               LogLite.tag(TAG).i("attachBaseContext: %s", (ClassLoader) param.args[2]);
          } else if ("java.lang.ClassLoader".equals(param.method.getName())) {
               classLoaderCache.add((ClassLoader) param.thisObject);
               LogLite.tag(TAG).i("attachBaseContext: %s", (ClassLoader) param.thisObject);
               notifyObserverAll((ClassLoader) param.thisObject);
          }
     }

     public interface Observer {
          void receive(ClassLoader classLoader);
     }
}

