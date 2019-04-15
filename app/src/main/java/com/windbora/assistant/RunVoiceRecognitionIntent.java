package com.windbora.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;

import java.util.Locale;

public class RunVoiceRecognitionIntent extends Activity implements View.OnClickListener {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    public void onClick(View v) {
        startVoiceRecognitionActivity(v.getContext());
    }

    private void startVoiceRecognitionActivity(Context context) {
        Intent voiceRecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        voiceRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        ((Activity) context).startActivityForResult(voiceRecognitionIntent, VOICE_RECOGNITION_REQUEST_CODE);
    }

}
