package com.lifengqiang.video.ui.main.space;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;

public class SpaceView extends BaseMainPageChildView<SpaceFragment> {
    private RecyclerView recyclerView;
    private WorksListAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        View bar = get(R.id.state_bar_fill);
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = getPresenter().getStateBarHeight();
        bar.setLayoutParams(params);
        recyclerView = get(R.id.recycler);
        adapter = new WorksListAdapter(getPresenter());
        recyclerView.setAdapter(adapter);
    }

    public WorksListAdapter getAdapter() {
        return adapter;
    }
}
