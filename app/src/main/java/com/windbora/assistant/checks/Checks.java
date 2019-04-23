package com.windbora.assistant.checks;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class Checks {

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopListenerService(Context context) {
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
}
