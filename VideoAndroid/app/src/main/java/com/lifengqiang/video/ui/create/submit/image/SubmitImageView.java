package com.lifengqiang.video.ui.create.submit.image;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.view.IndicatorBarView;

import java.io.File;
import java.util.List;

public class SubmitImageView extends BaseView<SubmitImageActivity> {
    private ViewPager2 pager;
    private IndicatorBarView indicator;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        ViewPager2 pager = getPager();
        IndicatorBarView indicator = getIndicator();
        List<File> files = presenter.getImageFiles();
        ImageAdapter adapter = new ImageAdapter();
        adapter.setData(files);
        pager.setAdapter(adapter);
        indicator.setMax(files.size());
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                indicator.setIndex(position);
            }
        });
    }

    public ViewPager2 getPager() {
        if (pager == null) {
            pager = get(R.id.pager);
        }
        return pager;
    }

    public IndicatorBarView getIndicator() {
        if (indicator == null) {
            indicator = get(R.id.indicator);
        }
        return indicator;
    }
}
