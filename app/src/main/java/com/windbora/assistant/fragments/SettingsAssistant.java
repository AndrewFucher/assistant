package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.windbora.assistant.R;
import com.windbora.assistant.fragments.base.BaseFragment;

public class SettingsAssistant extends BaseFragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private SettingsViewModel mViewModel;
    private Switch backgroundWork;
    private Switch proximitySensor;
    public final static String backgroundWorkIsActive = "background_work_is_active";
    public final static String proximitySensorIsActive = "proximity_sensor_is_active";

    public static SettingsAssistant newInstance() {
        return new SettingsAssistant();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        findElements();
        accessPreferences();

        proximitySensor.setClickable(false);

        setListeners();

        recoverStates();

    }

    private void recoverStates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundWork.setSplitTrack(preferences.getBoolean(backgroundWorkIsActive, true));
            proximitySensor.setSplitTrack(preferences.getBoolean(proximitySensorIsActive, false));
        }
    }

    private void accessPreferences() {
        preferences = getContext().getSharedPreferences("SettingsData", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    private void setListeners() {

        // Background work
        backgroundWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferencesEditor.putBoolean(backgroundWorkIsActive, isChecked);
                preferencesEditor.apply();
            }
        });

        proximitySensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferencesEditor.putBoolean(proximitySensorIsActive, isChecked);
                preferencesEditor.apply();
            }
        });

    }

    private void findElements() {
        backgroundWork = getView().findViewById(R.id.background_work);
        proximitySensor = getView().findViewById(R.id.proximity_sensor);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public String getName() {
        return "Settings";
    }
}
