package com.windbora.assistant.backgroundservice;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import com.windbora.assistant.RunVoiceRecognitionIntent;


public class Listener extends Service {

    private WindowManager.LayoutParams params;
    private View floatingView;
    private WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();

    }

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

        commandButton.setOnClickListener(new RunVoiceRecognitionIntent());

        commandButton.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
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
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        break;
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

    void requestUsageStatsPermission() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this.getBaseContext())) {
//            startActivity(new Intent(SettingsAssistant.ACTION_USAGE_ACCESS_SETTINGS));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 100;
        params.y = 100;
        try {
            windowManager.addView(floatingView, params);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Could't start service" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Log.d("Error", windowManager.toString() + "FINDME");

//        // Toast.makeText(getApplicationContext(), floatingView.toString(), Toast.LENGTH_LONG).show();
//
//       /* params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.x = 0;
//        params.y = 0;
//
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        windowManager.addView(floatingView, params);*/
    }
//    /*private WindowManager mWindowManager;
//    private View mOverlayView;
//    int mWidth;
//    private ImageView counterFab, mButtonClose;
//    boolean activity_background;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        if (intent != null) {
//            activity_background = intent.getBooleanExtra("activity_background", false);
//        }
//
//        if (mOverlayView == null) {
//
//            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
//
//
//            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//
//
//            //Specify the view position
//            params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
//            params.x = 0;
//            params.y = 100;
//
//
//            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//            mWindowManager.addView(mOverlayView, params);
//
//            Display display = mWindowManager.getDefaultDisplay();
//            final Point size = new Point();
//            display.getSize(size);
//
//            counterFab = (ImageView) mOverlayView.findViewById(R.id.fabHead);
//            mButtonClose = (ImageView) mOverlayView.findViewById(R.id.closeButton);
//
//
//            final RelativeLayout layout = (RelativeLayout) mOverlayView.findViewById(R.id.layout);
//            ViewTreeObserver vto = layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    int width = layout.getMeasuredWidth();
//
//                    //To get the accurate middle of the screen we subtract the width of the floating widget.
//                    mWidth = size.x - width;
//
//                }
//            });
//            mButtonClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    stopSelf();
//                }
//            });
//
//            counterFab.setOnTouchListener(new View.OnTouchListener() {
//                private int initialX;
//                private int initialY;
//                private float initialTouchX;
//                private float initialTouchY;
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//
//                            //remember the initial position.
//                            initialX = params.x;
//                            initialY = params.y;
//
//
//                            //get the touch location
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();
//
//
//                            return true;
//                        case MotionEvent.ACTION_UP:
//
//                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
//                            if(activity_background){
//                                float xDiff = event.getRawX() - initialTouchX;
//                                float yDiff = event.getRawY() - initialTouchY;
//
//                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
//                                    Intent intent = new Intent(getBaseContext(), PostInputActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//
//                                    //close the service and remove the fab view
//                                }
//                                stopSelf();
//
//                                //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
//                                int middle = mWidth / 2;
//                                float nearestXWall = params.x >= middle ? mWidth : 0;
//                                params.x = (int) nearestXWall;
//
//
//                                mWindowManager.updateViewLayout(mOverlayView, params);
//                                return true;}
//                                case MotionEvent.ACTION_MOVE:
//
//
//                                    int xDiff2 = Math.round();
//                                    int yDiff2 = Math.round(event.getRawY() - initialTouchY);
//
//
//                                    //Calculate the X and Y coordinates of the view.
//                                    params.x = initialX + xDiff2;
//                                    params.y = initialY + yDiff2;
//
//                                    //Update the layout with new X & Y coordinates
//                                    mWindowManager.updateViewLayout(mOverlayView, params);
//
//
//                                    return true;
//                            }
//                            return false;
//                    }
//                });
//            }
//
//
//            return super.onStartCommand(intent, flags, startId);
//
//        }
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        setTheme(R.style.AppTheme);
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mOverlayView != null)
//            mWindowManager.removeView(mOverlayView);
//    }*/

}
