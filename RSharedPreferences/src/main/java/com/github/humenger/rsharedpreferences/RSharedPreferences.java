package com.github.humenger.rsharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.util.Log;

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
            XReflectHelpers.setMyObjectField(activity,"base",new RContextWrapper(originalBase));
            cacheContextMap.put(originalBase,activity);
        }
    }
    public static void addPreferencesFromResource(PreferenceActivity activity, int preferencesResId){
        begin(activity);
        activity.addPreferencesFromResource(preferencesResId);
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
    static void end(Context originalBase){
        if(!cacheContextMap.containsKey(originalBase)){
           return;
        }
        XReflectHelpers.setMyObjectField(Objects.requireNonNull(cacheContextMap.get(originalBase)),"base",originalBase);
        cacheContextMap.remove(originalBase);
    }

}
