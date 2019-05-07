package com.windbora.assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class CommandActivity extends AppCompatActivity {

    TextView nameText;
    TextView descriptionText;
    String commandName;
    String commandDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        getElements();

        setupActionBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            commandDescription = extras.getString("description", "None");
            commandName = extras.getString("name", "None");
        }

        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
