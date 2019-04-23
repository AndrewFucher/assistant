package com.windbora.assistant.backgroundservice;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.windbora.assistant.R;
import com.windbora.assistant.RunVoiceRecognition;
import com.windbora.assistant.checks.Checks;
import com.windbora.assistant.fragments.sharedpreferences.MySharedPreferences;

import java.util.Calendar;


public class Listener extends Service {

    private WindowManager.LayoutParams params;
    private View floatingView;
    private WindowManager windowManager;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this.getBaseContext(), "Start Widget Service", Toast.LENGTH_SHORT).show();
        displayWidget();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayWidget() {
        initWidget();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidget() {
        // requestUsageStatsPermission();
        initView();
        setWidgetListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setWidgetListener() {
        final ImageView commandButton = floatingView.findViewById(R.id.command_button);

        //commandButton.setOnClickListener(new RunVoiceRecognition());

        commandButton.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private static final int MAX_CLICK_DURATION = 100; // milliseconds
            private long startClickTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;
                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        break;

                    case MotionEvent.ACTION_UP:
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            //click event has occurred
//                            RunVoiceRecognition.startVoiceRecognitionActivity(context);
                            Intent intent = new Intent(context, RunVoiceRecognition.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    /*case MotionEvent.ACTION_UP:
                        stopButton.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
                        recordButton.animate().translationX(-getResources().getDimension(R.dimen.standard_55));
                        startButton.setVisibility(View.GONE);
                        break;*/
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getBaseContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    void requestUsageStatsPermission() {
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
//                && !hasUsageStatsPermission(this.getBaseContext())) {
////            startActivity(new Intent(SettingsAssistant.ACTION_USAGE_ACCESS_SETTINGS));
//            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    boolean hasUsageStatsPermission(Context context) {
//        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
//                android.os.Process.myUid(), context.getPackageName());
//        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
//        return granted;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initView() {


        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        try {
            windowManager.addView(floatingView, params);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Could't start service" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
