package com.github.humenger.rsharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.github.humenger.xreflecthelpers.XReflectHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import me.weishu.reflection.Reflection;

public class RSharedPreferences {
    public static final String TAG = "RSharedPreferences";
    public static <T> T sharedPreferencesBypass(Context context,RBypassCallback<T> callback){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if (Reflection.unseal(context) != 0) {
                    Log.w(TAG, "getSharedPreferences: Disable hide api check failed");
                }
                int ori = XReflectHelpers.getMyIntField(context.getApplicationInfo(), "targetSdkVersion");
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", Build.VERSION_CODES.M);
                T obj=null;
                try {
                    obj = callback.call();
                }catch (Throwable throwable){throwable.printStackTrace();}
                XReflectHelpers.setMyIntField(context.getApplicationInfo(), "targetSdkVersion", ori);
                return obj;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return callback.call();
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

}
