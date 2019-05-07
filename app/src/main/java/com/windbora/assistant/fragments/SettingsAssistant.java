package com.windbora.assistant.fragments;

import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.windbora.assistant.R;
import com.windbora.assistant.backgroundservice.Listener;
import com.windbora.assistant.fragments.base.BaseFragment;
import com.windbora.assistant.sharedpreferences.MySharedPreferences;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
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
        preferences = new MySharedPreferences(getContext());
    }

    private void setListeners() {

        // Background work
        backgroundWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    stopMyListener();
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

    private void stopMyListener() {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            String processName = context.getPackageName() + ":backgroundservice";

//            Log.d("msg", next.processName + " FIND ME");

            if (next.processName.equals(processName)) {
                Process.killProcess(next.pid);
            }
        }
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
        return "MySettings";
    }
}
