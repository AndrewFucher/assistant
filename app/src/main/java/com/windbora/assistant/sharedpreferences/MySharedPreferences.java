package com.windbora.assistant.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences implements ISharedPreferences {

    private final String backgroundWorkIsActive = "background_work_is_active";
    private final String proximitySensorIsActive = "proximity_sensor_is_active";
    private final String sharedPreferencesName = "SettingData";

    private SharedPreferences sharedPreferences;
    private boolean defaultValue = false;

    public MySharedPreferences(Context context) {

        this.sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
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
        return this.sharedPreferences.getBoolean(backgroundWorkIsActive, defaultValue);
    }

    @Override
    public void setEnableProximitySensor() {
        this.sharedPreferences.edit().putBoolean(proximitySensorIsActive, !getEnableProximitySensor()).apply();
//        this.editor.putBoolean(proximitySensorIsActive, !sharedPreferences.getBoolean(proximitySensorIsActive, true));
//        this.editor.apply();
    }

    @Override
    public boolean getEnableProximitySensor() {
        return this.sharedPreferences.getBoolean(proximitySensorIsActive, defaultValue);
    }

    @Override
    public int getMode() {
        return Context.MODE_PRIVATE;
    }


}
