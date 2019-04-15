package com.windbora.assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.windbora.assistant.backgroundservice.Listener;
import com.windbora.assistant.fragments.Commands;
import com.windbora.assistant.fragments.Play;
import com.windbora.assistant.fragments.SettingsAssistant;
import com.windbora.assistant.fragments.adapters.MyFragmentPagerAdapter;
import com.windbora.assistant.fragments.base.BaseFragment;

import java.util.ArrayList;

import static com.windbora.assistant.RunVoiceRecognitionIntent.VOICE_RECOGNITION_REQUEST_CODE;

import static com.windbora.assistant.fragments.SettingsAssistant.backgroundWorkIsActive;

public class MainActivity extends AppCompatActivity {

    public final static int DRAW_OVER_OTHER_APP_PERMISSION = 5469;

    ViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accessPreferences();
        findElements();
        addFragments();
        askForSystemOverlayPermission();

        if (preferences.getBoolean(backgroundWorkIsActive, true)) {
            startWidgetService();
        }
    }

    private void accessPreferences() {
        preferences = getBaseContext().getSharedPreferences("SettingsData", Context.MODE_PRIVATE);
    }

    public void startWidgetService(){
        startService(new Intent(this, Listener.class));
    }

    private void addFragments() {
        // Fragments
        BaseFragment settings = new SettingsAssistant(); // settings
        BaseFragment play = new Play();        // command from app
        BaseFragment commands = new Commands(); // list of commands

        // Array of fragments
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(play);
        fragments.add(commands);
        fragments.add(settings);

        // Adapters
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.setList(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Tabs icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_play);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_commands);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);

    }

    private void findElements() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Once command spelled we are searching for it in list
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DRAW_OVER_OTHER_APP_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        //Permission is not available. Display error text.
                        finish();
                    }
                }

                case VOICE_RECOGNITION_REQUEST_CODE:

                    Toast.makeText(getBaseContext(), "QAAAAQ", Toast.LENGTH_LONG).show();

            }
    }
}
