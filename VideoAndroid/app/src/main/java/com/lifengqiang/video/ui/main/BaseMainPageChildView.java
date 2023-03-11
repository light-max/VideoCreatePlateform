package com.lifengqiang.video.ui.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.base.pm.BaseView;

/**
 * 主要是统一解决状态栏填充问题
 */
public class BaseMainPageChildView<F extends PresenterFragment<?>> extends BaseView<F> {
    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        View stateBarFill = get(R.id.state_bar_fill);
        ViewGroup.LayoutParams params = stateBarFill.getLayoutParams();
        params.height = presenter.getStateBarHeight();
        stateBarFill.setLayoutParams(params);
    }
}
