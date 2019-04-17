package com.windbora.assistant.checks;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

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

        Iterator<ActivityManager.RunningAppProcessInfo> iter = runningAppProcesses.iterator();

        while (iter.hasNext()) {
            ActivityManager.RunningAppProcessInfo next = iter.next();

            String processName = context.getPackageName() + ":backgroundwork";

            if (next.processName.equals(processName)) {
                Process.killProcess(next.pid);
                break;
            }
        }
    }
}
