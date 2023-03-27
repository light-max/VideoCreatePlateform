package com.lifengqiang.video.ui.main.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.fragment.PresenterFragment;

public class MessageFragment extends PresenterFragment<MessageView> {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadNewData();
        getIView().getSwipe().setOnRefreshListener(() -> {
            loadNewData();
            mainHandler.postDelayed(() -> {
                getIView().getSwipe().setRefreshing(false);
            }, 500);
        });
    }

    private void loadNewData() {
        Api.getUserMessage().success(data -> {
            getIView().setShowContent(!data.isEmpty());
            if (!data.isEmpty()) {
                MessageListAdapter adapter = getIView().getAdapter();
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }).run();
    }
}
