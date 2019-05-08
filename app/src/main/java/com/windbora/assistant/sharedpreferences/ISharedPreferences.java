package com.windbora.assistant.sharedpreferences;

public interface ISharedPreferences {

    void setWorkInBackground();
    boolean getWorkInBackground();

    void setEnableProximitySensor();
    boolean getEnableProximitySensor();

    void setFirstTime();
    boolean getFirstTime();

    int getMode();

}
