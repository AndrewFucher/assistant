package com.windbora.assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.windbora.assistant.SettingsFragments.MySettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.settings_layout);

        setupActionBar();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new MySettingsFragment())
                .commit();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
}
