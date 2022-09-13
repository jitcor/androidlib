package com.github.humenger.android13lib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.github.humenger.rsharedpreferences.RSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RSharedPreferences.getSharedPreferences(this,"www", Context.MODE_WORLD_READABLE);
    }
}