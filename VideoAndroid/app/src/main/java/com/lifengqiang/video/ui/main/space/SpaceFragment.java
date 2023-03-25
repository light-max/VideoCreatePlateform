package com.lifengqiang.video.ui.main.space;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.search.SearchActivity;
import com.lifengqiang.video.ui.uspace.UserSpaceActivity;
import com.lifengqiang.video.ui.works.WorksPlayerActivity;

public class SpaceFragment extends PresenterFragment<SpaceView> implements WorksListAdapter.Callback {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_space, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click(R.id.search, () -> SearchActivity.start(requireContext()));
        loadNewData();
    }

    public void loadNewData() {
        Api.getFriendWorksList().success(data -> {
            getIView().getAdapter().setData(data);
            getIView().getAdapter().notifyDataSetChanged();
        }).run();
    }

    @Override
    public void openUserSpace(Works works, int position) {
        Intent intent = new Intent(requireContext(), UserSpaceActivity.class);
        intent.putExtra(UserSpaceActivity.USER_ID, works.getUserId());
        startActivity(intent);
    }

    @Override
    public void like(Works works, int position) {
        Api.likeWorks(works.getId()).success(data -> {
            works.setLike(data.isLike());
            works.setLikeCount(data.getCount());
            getIView().getAdapter().notifyDataSetChanged();
        }).run();
    }

    @Override
    public void openWorks(Works works, int position) {
        Intent intent = new Intent(getContext(), WorksPlayerActivity.class);
        intent.putExtra(WorksPlayerActivity.WORKS_ID, works.getId());
        startActivity(intent);
    }
}
