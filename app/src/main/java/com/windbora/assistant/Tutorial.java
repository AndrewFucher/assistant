package com.windbora.assistant;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.windbora.assistant.adapters.MyTutorialScreenshotsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Tutorial extends AppCompatActivity {

    ImageView leftArrow;
    ImageView rightArrow;
    List<Integer> drawables = new ArrayList<>();
    ViewPager viewPager;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        setListDrawables();

        final MyTutorialScreenshotsPagerAdapter adapter = new MyTutorialScreenshotsPagerAdapter(this);
        adapter.setDrawables(drawables);

        leftArrow  = findViewById(R.id.left_arrow);
        rightArrow = findViewById(R.id.right_arrow);
        viewPager  = findViewById(R.id.viewPager);
        finish     = findViewById(R.id.finish);

        viewPager.setAdapter(adapter);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem() - 1;
                if (position >= 0) {
                    viewPager.setCurrentItem(position);
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem() + 1;
                if (position < drawables.size()) {
                    viewPager.setCurrentItem(position);
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListDrawables() {
        drawables.add(R.drawable.screenshot_1);
        drawables.add(R.drawable.screenshot_4);
        drawables.add(R.drawable.screenshot_2);
        drawables.add(R.drawable.screenshot_3);
    }
}
