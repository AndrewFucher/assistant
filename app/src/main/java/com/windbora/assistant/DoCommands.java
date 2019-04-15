package com.windbora.assistant;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class DoCommands{
    public static void makeCall(Context context, String string) {
        Toast.makeText(context, "You called someone!", Toast.LENGTH_SHORT).show();
    }
}
