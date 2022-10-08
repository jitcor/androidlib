package com.github.humenger.hsystemhelpers;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import com.github.humenger.xreflecthelpers.XReflectHelpers;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;

import java.io.File;
import java.util.List;

import dalvik.system.DexClassLoader;
import me.weishu.reflection.Reflection;

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
    public static String getProcessName(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        try {
            if(Reflection.unseal(context)==0){
               return (String)XReflectHelpers.callMyStaticMethod(XReflectHelpers.findMyClass("android.app.ActivityThread",null),"currentProcessName");
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am==null)return null;
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

}
