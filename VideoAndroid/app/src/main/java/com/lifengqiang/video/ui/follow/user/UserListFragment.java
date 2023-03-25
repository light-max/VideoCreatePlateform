package com.lifengqiang.video.ui.follow.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.data.result.UserFollow;
import com.lifengqiang.video.ui.uspace.UserSpaceActivity;

public class UserListFragment extends PresenterFragment<UserListView> implements UserListAdapter.Callback {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadNewData();
    }

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @SuppressLint("SetTextI18n")
    public void loadNewData() {
        ExRequestBuilder.get(url).asyncList(UserFollow.class)
                .success(data -> {
                    getIView().getAdapter().setData(data);
                    getIView().getAdapter().notifyDataSetChanged();
                    getIView().getCountView().setText("共" + data.size() + "个用户");
                }).run();
    }

    @Override
    public void openUserSpace(UserFollow user, int position) {
        Intent intent = new Intent(requireContext(), UserSpaceActivity.class);
        intent.putExtra(UserSpaceActivity.USER_ID, user.getUserId());
        startActivity(intent);
    }

    @Override
    public void followToggle(UserFollow user, int position) {
        Api.follow(user.getUserId()).success(data -> {
            user.setFollow(data.isFollow());
            user.setFriend(user.isFollow() && user.isFollower());
            getIView().getAdapter().notifyItemChanged(position);
        }).run();
    }
}
