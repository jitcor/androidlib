package com.github.humenger.android13lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.github.humenger.githubproxyhelpers.GithubProxyHelpers;
import com.github.humenger.rsharedpreferences.RBypassCallback;
import com.github.humenger.rsharedpreferences.RContextWrapper;
import com.github.humenger.rsharedpreferences.RSharedPreferences;

import java.util.function.Function;

public class SettingsActivity extends AppCompatActivity {
public static final String TAG="SettingsActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("Test");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

//            SharedPreferences preferences= RSharedPreferences.getSharedPreferences(getPreferenceManager());
            RSharedPreferences.sharedPreferencesBypass(getContext(), (RBypassCallback<Void>) () -> {
                setPreferencesFromResource(R.xml.root_preferences,rootKey);
                return null;
            });
            SharedPreferences preferences= RSharedPreferences.sharedPreferencesBypass(getContext(), (RBypassCallback<SharedPreferences>) () ->
                    getPreferenceManager().getSharedPreferences());
//            SharedPreferences preferences= RSharedPreferences.getSharedPreferences(getPreferenceManager());

//            RSharedPreferences.setPreferencesFromResource(this,R.xml.root_preferences, rootKey);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onCreatePreferences: new Url:"+GithubProxyHelpers.getProxyUrl("https://raw.githubusercontent.com/serifer/alertclose/main/frida.json"));
                        }
                    }).start();

                }
            },10*1000);
            Log.d(TAG, "onCreatePreferences: replace success");
        }
    }
}