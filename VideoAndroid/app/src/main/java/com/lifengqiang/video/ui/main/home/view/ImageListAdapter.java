package com.lifengqiang.video.ui.main.home.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.data.result.Works;

import java.util.HashMap;
import java.util.Map;

class ImageListAdapter extends PagerAdapter {
    private Works works;
    private Map<Integer, ImageView> map = new HashMap<>();

    public void setWorks(Works works) {
        this.works = works;
        map.clear();
    }

    @Override
    public int getCount() {
        return works != null ? works.getImages().size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (!map.containsKey(position)) {
            ImageView view = new ImageView(container.getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(1080, 1080));
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            map.put(position, view);
        }
        ImageView image = map.get(position);
        container.addView(image);
        Glide.with(image)
                .load(ExRequestBuilder.getUrl(works.getImages().get(position)))
                .into(image);
        return image;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (map.containsKey(position)) {
            container.removeView(map.get(position));
        }
    }
}
