package com.lifengqiang.video.ui.main.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lifengqiang.video.R;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;
import com.lifengqiang.video.ui.main.CreateModelDialog;
import com.lifengqiang.video.ui.search.SearchActivity;

public class MessageView extends BaseMainPageChildView<MessageFragment> {
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerView;
    private TextView empty;

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        click(R.id.search, () -> SearchActivity.start(getContext()));
        click(R.id.create_model, (v) -> CreateModelDialog.show(getActivity(), v));
        swipe = get(R.id.swipe);
        recyclerView = get(R.id.recycler);
        empty = get(R.id.empty);
    }

    public void setShowContent(boolean isShow) {
        recyclerView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        empty.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    public SwipeRefreshLayout getSwipe() {
        return swipe;
    }
}
