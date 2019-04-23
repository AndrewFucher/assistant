package com.windbora.assistant.fragments.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences implements ISharedPreferences {

    public final static String backgroundWorkIsActive = "background_work_is_active";
    public final static String proximitySensorIsActive = "proximity_sensor_is_active";

    private Context context;
    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
    private String sharedPreferencesName = "SettingData";
    private int mode;

    public MySharedPreferences(int mode, Context context) {
        this.mode = mode;
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(sharedPreferencesName, mode);
//        this.editor = sharedPreferences.edit();
//        this.editor.apply();
    }

    @Override
    public void setWorkInBackground() {
        this.sharedPreferences.edit().putBoolean(backgroundWorkIsActive, !getWorkInBackground()).apply();
//        this.editor.putBoolean(backgroundWorkIsActive, !sharedPreferences.getBoolean(backgroundWorkIsActive, true));
//        this.editor.apply();
    }

    @Override
    public boolean getWorkInBackground() {
        return this.sharedPreferences.getBoolean(backgroundWorkIsActive, false);
    }

    @Override
    public void setEnableProximitySensor() {
        this.sharedPreferences.edit().putBoolean(proximitySensorIsActive, getEnableProximitySensor()).apply();
//        this.editor.putBoolean(proximitySensorIsActive, !sharedPreferences.getBoolean(proximitySensorIsActive, true));
//        this.editor.apply();
    }

    @Override
    public boolean getEnableProximitySensor() {
        return this.sharedPreferences.getBoolean(proximitySensorIsActive, false);
    }


    @Override
    public int getMode() {
        return this.mode;
    }
}
