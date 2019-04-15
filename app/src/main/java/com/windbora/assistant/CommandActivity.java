package com.windbora.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class CommandActivity extends Activity {

    TextView nameText;
    TextView descriptionText;
    String commandName;
    String commandDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        getElements();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            commandDescription = extras.getString("description", "None");
            commandName = extras.getString("name", "None");
        }

        setText();
    }

    private void setText() {
        nameText.setText(commandName);
        descriptionText.setText(commandDescription);
    }

    private void getElements() {
        nameText = findViewById(R.id.nameCommand);
        descriptionText = findViewById(R.id.descriptionCommand);
    }
}
