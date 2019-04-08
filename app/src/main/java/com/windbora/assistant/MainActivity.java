package com.windbora.assistant;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.windbora.assistant.fragments.Commands;
import com.windbora.assistant.fragments.Play;
import com.windbora.assistant.fragments.Settings;
import com.windbora.assistant.fragments.adapters.MyFragmentPagerAdapter;
import com.windbora.assistant.fragments.base.BaseFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findElements();

        addFragments();

    }

    private void addFragments() {
        // Fragments
        BaseFragment settings = new Settings(); // settings
        BaseFragment play = new Play();        // command from app
        BaseFragment commands = new Commands(); // list of commands

        // Fragments array
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(play);
        fragments.add(commands);
        fragments.add(settings);

        // Adapters
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.setList(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void findElements() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }
}
