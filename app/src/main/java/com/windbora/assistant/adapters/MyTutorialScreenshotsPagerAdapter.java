package com.windbora.assistant.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.windbora.assistant.R;

import java.util.List;

public class MyTutorialScreenshotsPagerAdapter extends PagerAdapter {

    Context context;
    private List<Integer> drawables;

    public MyTutorialScreenshotsPagerAdapter(Context context) {
        this.context = context;
    }

    public void setDrawables(List<Integer> list) {
        this.drawables = list;
    }

    @Override
    public int getCount() {
        return drawables.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        super.instantiateItem(container, position);

//        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        View itemView = LayoutInflater.from(context).inflate(R.layout.tutorial_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.image_item);
        imageView.setImageDrawable(context.getResources().getDrawable(drawables.get(position)));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);

        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
