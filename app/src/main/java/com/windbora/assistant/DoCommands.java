package com.windbora.assistant;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;


public class DoCommands{

    public static void makeCall(Context context, String string) {
        try {

            String number = "";

            string = string.replace("call ", "");

            // Get Contacts

            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = contentResolver.query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toLowerCase().equals(string.toLowerCase())) {
                        number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));break;
                    }
                    // Toast.makeText(context, cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), Toast.LENGTH_LONG).show();
                }
            }
            cursor.close();

            // Call by number

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);

            Toast.makeText(context, number, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void turnOffTheScreen(Activity activity, Window window) {
        Toast.makeText(activity.getBaseContext(), "MADE IT HERE", Toast.LENGTH_SHORT).show();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//        params.screenBrightness = 100;
//        window.setAttributes(params);
        //Get the window from the context
//        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

//        Unlock
//        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "DoCommands:mywakelocktag");
//        wakeLock.acquire(3000);
//        wakeLock.release();

        //Lock device
//        DevicePolicyManager mDPM = (DevicePolicyManager)activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        PowerManager.WakeLock wakeLock = ((PowerManager) activity.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:mywaketag");
//        wakeLock.release();
    }

    public static void setBrightnessTo(Context context, String string) {

        string = string.replace("set brightness to ", "");

        int brightness;

        try {
            brightness = Integer.valueOf(string);
            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
        } catch (Exception e) {
            Toast.makeText(context, "Couldn't set brightness to " + string, Toast.LENGTH_SHORT).show();
        }
    }

    public static void open(Context context, String string) {
        string = string.replace("open ", "").toLowerCase();

//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
//        Iterator<ResolveInfo> iter = pkgAppsList.iterator();
        Intent intent = null;


        final PackageManager pm = context.getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        String TAG = "DATAME";

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
//            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            if (packageInfo.packageName.contains(string)) {
                intent = context.getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
            }
        }


//        while (iter.hasNext()) {
//            ResolveInfo next = iter.next();
//            Log.d("DataME", next.resolvePackageName);
//            if (next.resolvePackageName.contains(string)) {
//                intent = context.getPackageManager().getLaunchIntentForPackage(next.resolvePackageName);
//            }
//        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Did not find such app", Toast.LENGTH_SHORT).show();
        }
    }
}
