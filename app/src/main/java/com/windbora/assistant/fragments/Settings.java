package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.windbora.assistant.R;
import com.windbora.assistant.fragments.base.BaseFragment;

public class Settings extends BaseFragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private SettingsViewModel mViewModel;
    public Switch backgroundInWork;

    public static Settings newInstance() {
        return new Settings();
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
        setListeners();

    }

    private void accessPreferences() {
        preferences = getContext().getSharedPreferences("SettingsData", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    private void setListeners() {

        // Background work
        backgroundInWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferencesEditor.putBoolean("is_active_background", isChecked);
            }
        });


    }

    private void findElements() {
        backgroundInWork = getView().findViewById(R.id.background_work);

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
