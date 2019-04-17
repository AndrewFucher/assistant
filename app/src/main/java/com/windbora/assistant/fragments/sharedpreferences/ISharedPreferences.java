package com.windbora.assistant.fragments.sharedpreferences;

public interface ISharedPreferences {

    void setWorkInBackground();
    boolean getWorkInBackground();

    void setEnableProximitySensor();
    boolean getEnableProximitySensor();

    int getMode();

}
