package com.github.humenger.rsharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.github.humenger.xreflecthelpers.XReflectHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.weishu.reflection.Reflection;

public class RSharedPreferences {
    public static final String TAG = "RSharedPreferences";
    private static final Map<Context,Context> cacheContextMap=new HashMap<>();
    static void begin(Activity activity){
        Context originalBase=activity.getBaseContext();
        if(!(originalBase instanceof RContextWrapper)){
            if(cacheContextMap.containsKey(originalBase)){
                throw new IllegalArgumentException("context is contains");
            }
            XReflectHelpers.setMyObjectField(activity,"mBase",new RContextWrapper(originalBase));
            cacheContextMap.put(activity,originalBase);
        }
    }
    public static void addPreferencesFromResource(PreferenceActivity activity, int preferencesResId){
        begin(activity);
        activity.addPreferencesFromResource(preferencesResId);
        end(activity);
    }
    public static SharedPreferences getSharedPreferences(Context context, String name, int mode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if(Reflection.unseal(context)!=0){
                    Log.w(TAG, "getSharedPreferences: Disable hide api check failed");
                }
                int ori = XReflectHelpers.getMyIntField(context.getApplicationInfo(), "targetSdkVersion");
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", Build.VERSION_CODES.M);
                SharedPreferences preferences = context.getSharedPreferences(name, mode);
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", ori);
                return preferences;
            }catch (Throwable throwable){
                throwable.printStackTrace();
                return context.getSharedPreferences(name,mode);
            }
        }else {
            return context.getSharedPreferences(name,mode);
        }

    }
    public static SharedPreferences getSharedPreferences(PreferenceManager preferenceManager){
        Context context=preferenceManager.getContext();
        if(context==null)return preferenceManager.getSharedPreferences();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if(Reflection.unseal(context)!=0){
                    Log.w(TAG, "getSharedPreferences: Disable hide api check failed");
                }
                int ori = XReflectHelpers.getMyIntField(context.getApplicationInfo(), "targetSdkVersion");
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", Build.VERSION_CODES.M);
                SharedPreferences preferences = preferenceManager.getSharedPreferences();
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", ori);
                return preferences;
            }catch (Throwable throwable){
                throwable.printStackTrace();
                return preferenceManager.getSharedPreferences();
            }
        }else {
            return preferenceManager.getSharedPreferences();
        }

    }
    public static SharedPreferences getSharedPreferences(android.preference.PreferenceManager preferenceManager){
        Context context= (Context) XReflectHelpers.callMyMethod(preferenceManager,"getContext");
        if(context==null)return preferenceManager.getSharedPreferences();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if(Reflection.unseal(context)!=0){
                    Log.w(TAG, "getSharedPreferences: Disable hide api check failed");
                }
                int ori = XReflectHelpers.getMyIntField(context.getApplicationInfo(), "targetSdkVersion");
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", Build.VERSION_CODES.M);
                SharedPreferences preferences = preferenceManager.getSharedPreferences();
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", ori);
                return preferences;
            }catch (Throwable throwable){
                throwable.printStackTrace();
                return preferenceManager.getSharedPreferences();
            }
        }else {
            return preferenceManager.getSharedPreferences();
        }

    }
    static void end(Activity activity){
        if(!cacheContextMap.containsKey(activity)){
           return;
        }
        XReflectHelpers.setMyObjectField(activity,"mBase",cacheContextMap.get(activity));
        cacheContextMap.remove(activity);
    }

}
