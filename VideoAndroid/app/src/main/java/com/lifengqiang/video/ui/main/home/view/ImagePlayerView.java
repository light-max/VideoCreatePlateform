package com.lifengqiang.video.ui.main.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.lifengqiang.video.R;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.view.IndicatorBarView;

public class ImagePlayerView extends LinearLayout {
    private ImageListAdapter adapter = new ImageListAdapter();
    private MyViewPager pager;
    private IndicatorBarView indicator;
    private VideoViewContainer videoViewContainer;

    public ImagePlayerView(Context context) {
        this(context, null);
    }

    public ImagePlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_image_loop_player, this);
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);
        indicator = findViewById(R.id.indicator);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicator.setIndex(position);
            }
        });
    }

    public void setVideoViewContainer(VideoViewContainer videoViewContainer) {
        this.videoViewContainer = videoViewContainer;
    }

    public void setWorks(Works works) {
        setVisibility(VISIBLE);
        indicator.setMax(works.getImages().size());
        indicator.setIndex(0);
        pager.setCurrentItem(0, false);
        adapter.setWorks(works);
        adapter.notifyDataSetChanged();
        videoViewContainer.hideCover();
    }

    public void touchViewPager(MotionEvent e) {
        pager.onITouchEvent(e);
    }
}
