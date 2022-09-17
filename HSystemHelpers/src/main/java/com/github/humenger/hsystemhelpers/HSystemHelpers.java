package com.github.humenger.hsystemhelpers;

import android.content.Context;

import java.io.File;

import dalvik.system.DexClassLoader;

public class HSystemHelpers {
    public static final String TAG = "HSystemHelpers";
    public static String findSystemJarPathWithClassName(Context context,String className){

        String sysJarList[]=System.getenv("BOOTCLASSPATH").split(":");
        String hshCache = context.getDir("HSH_cache", Context.MODE_PRIVATE).getPath();
        DexClassLoader classLoader;
        for (String jarPath:sysJarList){
            classLoader=new DexClassLoader(jarPath,hshCache,null,null);
            try {
                classLoader.loadClass(className);
                return jarPath;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
