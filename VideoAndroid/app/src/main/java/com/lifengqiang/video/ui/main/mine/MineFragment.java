package com.lifengqiang.video.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.follow.FollowActivity;
import com.lifengqiang.video.ui.setting.SettingActivity;
import com.lifengqiang.video.ui.setting.user.UserInfoActivity;
import com.lifengqiang.video.ui.works.WorksPlayerActivity;

public class MineFragment extends PresenterFragment<MineView> implements SimpleSingleItemRecyclerAdapter.OnItemClickListener<Works> {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click(R.id.setting, () -> {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            FragmentActivity activity = getActivity();
            assert activity != null;
            activity.startActivityForResult(intent, SettingActivity.LOGOUT);
        });
        click(R.id.edit_info, () -> {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            startActivity(intent);
        });
        Runnable relationOpen = () -> startActivity(new Intent(getContext(), FollowActivity.class));
        click(R.id.friend_layout, relationOpen);
        click(R.id.follow_layout, relationOpen);
        click(R.id.follower_layout, relationOpen);
    }

    @Override
    public void onClick(Works data, int position) {
        Intent intent = new Intent(getContext(), WorksPlayerActivity.class);
        intent.putExtra(WorksPlayerActivity.WORKS_ID, data.getId());
        startActivity(intent);
    }
}
