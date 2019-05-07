package com.windbora.assistant.SettingsFragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.windbora.assistant.R;
import com.windbora.assistant.backgroundservice.Listener;
import com.windbora.assistant.sharedpreferences.ISharedPreferences;
import com.windbora.assistant.sharedpreferences.MySharedPreferences;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class MySettingsFragment extends PreferenceFragmentCompat {

    Context context;
    Preference backgroundWork;

    public MySettingsFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        context = getContext();
        final ISharedPreferences sharedPreferences = new MySharedPreferences( getContext());

        setPreferencesFromResource(R.xml.pref_settings, rootKey);

//        R.id.switch_settings_work

        backgroundWork = findPreference("background_work_pref");

        if (backgroundWork instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) backgroundWork;
            int prefIndex = listPreference.findIndexOfValue("background_work_pref");
            if (prefIndex >= 0) {
//                .setSummary(listPreference.getEntries()[prefIndex]);
                backgroundWork.setIconSpaceReserved(false);

                backgroundWork.setDefaultValue(sharedPreferences.getWorkInBackground());
            }
        } else {
            backgroundWork.setIconSpaceReserved(false);

            backgroundWork.setDefaultValue(sharedPreferences.getWorkInBackground());
        }

//        background_work.setIconSpaceReserved(false);
//
//        background_work.setDefaultValue(sharedPreferences.getWorkInBackground());

        //        PreferenceManager.setDefaultValues(context, R.xml.pref_settings, );

//        Toast.makeText(context, String.valueOf(sharedPreferences.getWorkInBackground()), Toast.LENGTH_SHORT).show();

        backgroundWork.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                Intent intent = new Intent(getContext(), Listener.class);

                sharedPreferences.setWorkInBackground();

                if (sharedPreferences.getWorkInBackground()) {
                    getContext().startService(intent);
                } else {
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

                return true;
            }
        });

    }

//    @Override
//    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
//        context = getContext();
//        setPreferencesFromResource(R.xml.pref_settings, rootKey);
//    }
}
