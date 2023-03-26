package com.lifengqiang.video.ui.uspace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.result.User;
import com.lifengqiang.video.ui.message.MessageActivity;
import com.lifengqiang.video.ui.works.WorksPlayerActivity;
import com.lifengqiang.video.utils.GlideRequests;

public class UserSpaceView extends BaseView<UserSpaceActivity> {
    private RecyclerView recyclerView;
    private final VideoListAdapter adapter = new VideoListAdapter();
    private TextView worksCount;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        View stateBarFill = get(R.id.state_bar_fill);
        ViewGroup.LayoutParams params = stateBarFill.getLayoutParams();
        params.height = getPresenter().getStateBarHeight();
        stateBarFill.setLayoutParams(params);
        click(R.id.back, () -> getPresenter().finish());
        recyclerView = get(R.id.recycler);
        worksCount = get(R.id.works_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            Intent intent = new Intent(getPresenter(), WorksPlayerActivity.class);
            intent.putExtra(WorksPlayerActivity.WORKS_ID, data.getId());
            getContext().startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setUserData(User user) {
        TextView nickname = get(R.id.nickname);
        TextView username = get(R.id.username);
        ImageView head = get(R.id.head);
        TextView friend = get(R.id.friend_count);
        TextView follow = get(R.id.follow_count);
        TextView follower = get(R.id.follower_count);
        TextView age = get(R.id.age);
        ImageView gender = get(R.id.gender);
        TextView des = get(R.id.des);
        TextView followState = get(R.id.follow_state);
        nickname.setText(user.getNickname());
        username.setText("账号: " + user.getUsername());
        Glide.with(getPresenter())
                .load(ExRequestBuilder.getUrl(user.getHead()))
                .apply(GlideRequests.skipCache())
                .into(head);
        friend.setText(String.valueOf(user.getFriendCount()));
        follow.setText(String.valueOf(user.getFollowCount()));
        follower.setText(String.valueOf(user.getFollowerCount()));
        age.setText(user.getAge() + "岁");
        if ("male".equals(user.getGender())) {
            gender.setImageResource(R.drawable.ic_gender_male);
        } else {
            gender.setImageResource(R.drawable.ic_gender_female);
        }
        if (user.getDes().trim().isEmpty()) {
            des.setTextColor(presenter.getResources().getColor(R.color.gray_888, null));
            des.setText("该作者没有内容介绍自己...");
        } else {
            des.setTextColor(0xFF000000);
            des.setText(user.getDes());
        }
        click(R.id.message, () -> {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra(MessageActivity.USER_ID, user.getId());
            intent.putExtra(MessageActivity.USER_NICKNAME, user.getNickname());
            intent.putExtra(MessageActivity.USER_HEAD, user.getHead());
            getContext().startActivity(intent);
        });
        click(followState, () -> {
            Api.follow(getPresenter().getTargetUserId())
                    .error((message, e) -> toast(message))
                    .success(data -> setFollowState(followState))
                    .run();
        });
        setFollowState(followState);
    }

    private void setFollowState(TextView text) {
        Api.isFriend(getPresenter().getTargetUserId())
                .error((message, e) -> text.setText("关注"))
                .success(data -> {
                    if (data.isFriend()) {
                        text.setText("互相关注");
                    } else if (data.isFollow()) {
                        text.setText("已关注");
                    } else {
                        text.setText("关注");
                    }
                }).run();
    }

    public VideoListAdapter getAdapter() {
        return adapter;
    }

    public TextView getWorksCount() {
        return worksCount;
    }
}
