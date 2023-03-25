package com.lifengqiang.video.ui.works;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.main.home.view.WorksInfoView;

public class WorksPlayerView extends BaseView<WorksPlayerActivity> {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        View view = get(R.id.state_bar_fill);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = presenter.getStateBarHeight();
        view.setLayoutParams(params);
    }

    public void setWorksInfo(Works works) {
        WorksInfoView view = get(R.id.works_info);
        view.setWorks(works);
    }
}
