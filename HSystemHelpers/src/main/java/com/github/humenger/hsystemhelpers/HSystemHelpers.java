package com.github.humenger.hsystemhelpers;

import android.content.Context;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;

import java.io.File;

import dalvik.system.DexClassLoader;

public class HSystemHelpers {
    public static final String TAG = "HSystemHelpers";
    public static String findSystemJarPathWithClassName(String className){
        String sysJarList[]=System.getenv("BOOTCLASSPATH").split(":");
        for (String jarPath:sysJarList){
            try{
                ApkFile apkFile=new ApkFile(new File(jarPath));
                for(DexClass cls:apkFile.getDexClasses()){
                    if(cls.toString().equals("L"+className.replace('.','/')+";")){
                        return jarPath;
                    }
                }
            }catch (Exception ioException){
                ioException.printStackTrace();
            }
        }
        return "";
    }

}
