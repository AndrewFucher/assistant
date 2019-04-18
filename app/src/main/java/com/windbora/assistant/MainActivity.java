package com.windbora.assistant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
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
    public final static int REQUEST_PERMISSION = 10;

    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    SparseIntArray sparseIntArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sparseIntArray = new SparseIntArray();

        getContext();
        findElements();
        addFragments();
        requestAppPermissions(new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE},
                R.string.msg, REQUEST_PERMISSION);
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
                break;
                case VOICE_RECOGNITION_REQUEST_CODE:

                    List<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Toast.makeText(getBaseContext(), command.get(0), Toast.LENGTH_LONG).show();

                    String bestMatch = command.get(0);


                    if (bestMatch.contains("call")) {
                        DoCommands.makeCall(context, bestMatch);
                    }
                    break;
        }
    }

    public void onPermissionGranted(int requestCode){};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission: grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == permissionCheck) {
            onPermissionGranted(requestCode);
        } else {
            try {

                Snackbar.make(findViewById(R.id.content), sparseIntArray.get(requestCode), Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestAppPermissions(final String[] requestedPermissions, final int stringID, final int requestCode) {
        sparseIntArray.put(requestCode, stringID);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;
        for (String permission: requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || shouldShowRequestPermissionRationale(permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRequestPermission) {
                Snackbar.make(findViewById(R.id.content), stringID, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionGranted(requestCode);
        }
    }
}
