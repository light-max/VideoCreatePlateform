package com.lifengqiang.video.ui.main.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;
import com.lifengqiang.video.ui.main.CreateModelDialog;
import com.lifengqiang.video.ui.main.home.view.VerticalLooperVideoContainer;
import com.lifengqiang.video.ui.main.home.view.WorksInfoView;

public class HomeView extends BaseMainPageChildView<HomeFragment> {
    private VerticalLooperVideoContainer videoContainer;

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        click(R.id.create_model, (v) -> CreateModelDialog.show(getActivity(), v));
        TextView content = get(R.id.content);
        click(content, () -> {
            if (content.getMaxLines() == 2) {
                content.setMaxLines(Integer.MAX_VALUE);
            } else {
                content.setMaxLines(2);
            }
        });
    }

    public VerticalLooperVideoContainer getVideoContainer() {
        if (videoContainer == null) {
            videoContainer = get(R.id.video_container);
        }
        return videoContainer;
    }

    @Override
    public void onResume() {
        if (getPresenter().isVisible()) {
            videoContainer.onResume();
        }
    }

    @Override
    public void onPause() {
        videoContainer.onPause();
    }

    public void setWorks(Works works) {
        WorksInfoView view = get(R.id.works_info);
        view.setWorks(works);
    }
}
