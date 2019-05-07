package com.windbora.assistant.sharedpreferences;

public interface ISharedPreferences {

    void setWorkInBackground();
    boolean getWorkInBackground();

    void setEnableProximitySensor();
    boolean getEnableProximitySensor();

    int getMode();

}
