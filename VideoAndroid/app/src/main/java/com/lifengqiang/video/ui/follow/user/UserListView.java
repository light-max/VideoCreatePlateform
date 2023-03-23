package com.lifengqiang.video.ui.follow.user;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class UserListView extends BaseView<UserListFragment> {
    private RecyclerView recycler;
    private UserListAdapter adapter;
    private TextView countView;

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        recycler = get(R.id.recycler);
        countView = get(R.id.count);
        adapter = new UserListAdapter(getPresenter());
        recycler.setAdapter(adapter);
    }

    public UserListAdapter getAdapter() {
        return adapter;
    }

    public TextView getCountView() {
        return countView;
    }
}
