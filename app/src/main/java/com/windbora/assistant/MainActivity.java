package com.windbora.assistant;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.windbora.assistant.backgroundservice.Listener;
import com.windbora.assistant.checks.Checks;
import com.windbora.assistant.fragments.Commands;
import com.windbora.assistant.fragments.Play;
import com.windbora.assistant.fragments.SettingsAssistant;
import com.windbora.assistant.fragments.adapters.MyFragmentPagerAdapter;
import com.windbora.assistant.fragments.base.BaseFragment;
import com.windbora.assistant.fragments.sharedpreferences.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.windbora.assistant.RunVoiceRecognitionIntent.VOICE_RECOGNITION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public final static int DRAW_OVER_OTHER_APP_PERMISSION = 5469;

    ViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferences preferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContext();
        findElements();
        addFragments();
        askForSystemOverlayPermission();

        MySharedPreferences preferences = new MySharedPreferences(MODE_PRIVATE, getApplicationContext());

        if (preferences.getWorkInBackground() && !Checks.isServiceRunning(Listener.class, context)) {
            startWidgetService();
        }
    }

    private void getContext() {
        context = getApplicationContext();
    }


    public void startWidgetService(){
        startService(new Intent(getApplicationContext(), Listener.class));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DRAW_OVER_OTHER_APP_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(context)) {
                        //Permission is not available. Display error text.
                        Toast.makeText(context, "Permission error", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                case VOICE_RECOGNITION_REQUEST_CODE:

                    List<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Toast.makeText(getBaseContext(), command.get(0), Toast.LENGTH_LONG).show();

                    String bestMatch = command.get(0);


                    if (bestMatch.contains("call")) {
                        DoCommands.makeCall(context, bestMatch);
                    }
            }
    }
}
