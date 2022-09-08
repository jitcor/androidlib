package com.github.humenger.rsharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.github.humenger.xreflecthelpers.XReflectHelpers;

import me.weishu.reflection.Reflection;

public class RSharedPreferences {
    public static final String TAG = "RSharedPreferences";
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

}
