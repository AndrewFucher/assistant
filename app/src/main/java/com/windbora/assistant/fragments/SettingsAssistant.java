package com.windbora.assistant.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.windbora.assistant.R;
import com.windbora.assistant.backgroundservice.Listener;
import com.windbora.assistant.checks.Checks;
import com.windbora.assistant.fragments.base.BaseFragment;
import com.windbora.assistant.fragments.sharedpreferences.MySharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SettingsAssistant extends BaseFragment {

    private MySharedPreferences preferences;
    private SettingsViewModel mViewModel;
    private Switch backgroundWork;
    private Switch proximitySensor;
    private Context context;

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

        setContext();

        findElements();
        accessPreferences();

        recoverStates();

        proximitySensor.setClickable(false);

        setListeners();

    }

    private void setContext() {
        context = getContext();
    }

    private void recoverStates() {
        if (preferences.getWorkInBackground()) {
            backgroundWork.setChecked(true);
        }
        if (preferences.getEnableProximitySensor()) {
            proximitySensor.setChecked(true);
        }
    }

    private void accessPreferences() {
        preferences = new MySharedPreferences(MODE_PRIVATE, getContext());
    }

    private void setListeners() {

        // Background work
        backgroundWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Checks.stopListenerService(context);
                } else {
                    context.startService(new Intent(context, Listener.class));
                }
//                Toast.makeText(context, String.valueOf(preferences.getWorkInBackground()), Toast.LENGTH_SHORT).show();
                preferences.setWorkInBackground();
            }
        });

        proximitySensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferences.setEnableProximitySensor();
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
