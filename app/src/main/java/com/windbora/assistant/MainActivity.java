package com.windbora.assistant;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import static com.windbora.assistant.RunVoiceRecognition.VOICE_RECOGNITION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public final static int DRAW_OVER_OTHER_APP_PERMISSION = 5469;
    public final static int REQUEST_PERMISSION = 10;

    WindowManager windowManager;
    View view;

    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    SparseIntArray sparseIntArray;
    Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sparseIntArray = new SparseIntArray();

        getContext();
        findElements();
        addFragments();
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            requestAppPermissions(new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE},
                    R.string.msg, REQUEST_PERMISSION);

            askForSystemSettingWritePermission();
        }

        askForSystemOverlayPermission();

        MySharedPreferences preferences = new MySharedPreferences(MODE_PRIVATE, context);


//        Toast.makeText(context, String.valueOf(preferences.getWorkInBackground()), Toast.LENGTH_SHORT).show();
        if (preferences.getWorkInBackground() && !Checks.isServiceRunning(Listener.class, context)) {
            startWidgetService();
//            floatingWindow();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForSystemSettingWritePermission() {
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void getContext() {
        context = this;
    }


    public void startWidgetService(){
        startService(new Intent(context, Listener.class));
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
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this.getBaseContext())) {

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
//                case VOICE_RECOGNITION_REQUEST_CODE:
//                    try {
//                        List<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                        // Toast.makeText(getBaseContext(), command.get(0), Toast.LENGTH_LONG).show();
//
//                        String bestMatch = command.get(0);
//
//
//                        if (bestMatch.contains("call")) {
//                            DoCommands.makeCall(context, bestMatch);
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(context, "You didn't say anything", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
        }
    }

    public void onPermissionGranted(int requestCode){}

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

//    private void floatingWindow(){
//        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
//
//        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.layout_floating_widget, null);
//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );
//        params.x = 0;
//        params.y = 0;
//        params.gravity = Gravity.CENTER;
//        windowManager.addView(view,params);
//
//        view.setOnTouchListener(new View.OnTouchListener() {
//            private WindowManager.LayoutParams updParams = params;
//            int x,y;
//            float touchedX, touchedY;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN :
//                        x = updParams.x;
//                        y= updParams.y;
//
//                        touchedX = event.getRawX();
//                        touchedY = event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        updParams.x = (int) (x + (event.getRawX() - touchedX));
//                        updParams.y = (int) (x + (event.getRawY() - touchedY));
//                        windowManager.updateViewLayout(view,updParams);
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//        Toast.makeText(context, "WORKS", Toast.LENGTH_SHORT).show();
//
//
////        view.findViewById(R.id.start_stop_audio).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                if (!isVideoPlay) {
//////                    if (!isAudioPlay) {
//////                        Intent myintent = new Intent(getApplicationContext(), IpCallService.class);
//////                        myintent.putExtra("commandType", STATE_CALL_START);
//////                        getApplication().startService(myintent);
//////                        isAudioPlay = !isAudioPlay;
//////                        ((ImageView) view.findViewById(R.id.start_stop_audio)).setImageResource(R.drawable.user_icon7);
//////                    } else {
//////                        Intent myintent = new Intent(getApplicationContext(), IpCallService.class);
//////                        myintent.putExtra("commandType", STATE_CALL_END);
//////                        getApplication().startService(myintent);
//////                        isAudioPlay = !isAudioPlay;
//////                        ((ImageView) view.findViewById(R.id.start_stop_audio)).setImageResource(R.drawable.user_icon6);
//////                    }
//////                }
////            }
////        });
//        view.findViewById(R.id.command_button).setOnClickListener(new View.OnClickListener() {
//            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                RunVoiceRecognition.startVoiceRecognitionActivity(context);
////                if (!isAudioPlay) {
////                    if (!isVideoPlay) {
////                        final MediaProjectionManager manager
////                                = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
////                        final Intent permissionIntent = manager.createScreenCaptureIntent();
////                        startActivityForResult(permissionIntent, 1);
////                    } else {
////                        final Intent intent = new Intent(SettingsActivity.this, ScreenRecorderService.class);
////                        intent.setAction(ScreenRecorderService.ACTION_STOP);
////                        startService(intent);
////                        isVideoPlay = false;
////                        ((ImageView) view.findViewById(R.id.start_stop_video)).setImageResource(R.drawable.user_icon6);
////
////                        Date date = new Date();
////                        long stop_call = date.getTime();
////                        java.text.DateFormat df = new java.text.SimpleDateFormat("HH:mm:ss-yyyy.MM.dd");
////
////                        SharedPreferences settings = getApplication().getSharedPreferences(
////                                Constants.LISTEN_ENABLED, 0);
////                        int numb = settings.getInt("videoNumb", 0);
////                        if (numb > 0) numb--;
////                        File file = new File(getFilePath(getApplication()), "video_" + numb + ".mp4");
////
////                        DataBasePhone db = new DataBasePhone(getApplicationContext());
////                        db.Insert("video_" + numb, "null", "", df.format(new Date(start_call)),
////                                df.format(new Date(stop_call - start_call)), Boolean.toString(false), "0", file.getAbsolutePath());
////                    }
////                }
//            }
//        });
//
////        windowManager.addView(view, params);
//    }
}
