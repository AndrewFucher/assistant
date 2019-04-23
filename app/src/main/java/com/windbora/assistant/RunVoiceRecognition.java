package com.windbora.assistant;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class RunVoiceRecognition extends Activity{

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public static void startVoiceRecognitionActivity(Context context) {
        Intent voiceRecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        String language = "en-GB";

        voiceRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        voiceRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);

        ((Activity) context).startActivityForResult(voiceRecognitionIntent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startVoiceRecognitionActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VOICE_RECOGNITION_REQUEST_CODE:
                try {
                    List<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Toast.makeText(getBaseContext(), command.get(0), Toast.LENGTH_LONG).show();

                    String bestMatch = command.get(0);


                    if (bestMatch.contains("call")) {
                        DoCommands.makeCall(this, bestMatch);
                    } else if (bestMatch.equals("turn off the screen")) {
                        DoCommands.turnOffTheScreen(this, getWindow());
                    } else if (bestMatch.contains("set brightness to ")) {
                        DoCommands.setBrightnessTo(this, bestMatch);
                    } else if (bestMatch.contains("open")) {
                        DoCommands.open(this, bestMatch);
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(this, "You didn't say anything", Toast.LENGTH_SHORT).show();
                    break;
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        finish();
    }
}
