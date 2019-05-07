package com.windbora.assistant.PreferenceCategorySettings;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.windbora.assistant.R;

public class PreferenceCategorySettings extends PreferenceCategory {

    public PreferenceCategorySettings(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
//        TextView textView = (TextView) view.findViewById(R.xml.pref_settings.);
    }

    public PreferenceCategorySettings(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceCategorySettings(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    public PreferenceCategorySettings(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
}
