package com.lifengqiang.video.ui.search.user;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.ui.search.SearchFragment;

public class SearchUserFragment extends SearchFragment<SearchUserView> {
    private final UserListAdapter adapter = new UserListAdapter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView().setAdapter(adapter);
        searchValue.observe(this, this::loadNewData);
    }

    private void loadNewData(String w) {
        Api.searchUsers(w).success(data -> {
            setShowContent(!data.isEmpty());
            if (data.isEmpty()) {
                getEmptyView().setText("搜索内容为空");
            } else {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }).run();
    }
}
