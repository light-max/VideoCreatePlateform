package com.lifengqiang.video.ui.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.data.result.User;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;
import com.lifengqiang.video.utils.GlideRequests;

public class MineView extends BaseMainPageChildView<MineFragment> {
    private RecyclerView recycler;
    private VideoListAdapter[] adapters;
    private MutableLiveData<Integer>[] counts = new MutableLiveData[]{
            new MutableLiveData<Integer>(),
            new MutableLiveData<Integer>(),
            new MutableLiveData<Integer>(),
    };

    @Override
    public void onCreate(Bundle saveInstanceState) {
        adapters = new VideoListAdapter[]{
                new VideoListAdapter("/user/works/all/list", counts[0]),
                new VideoListAdapter("/user/works/like/all/list", counts[1]),
                new VideoListAdapter("/user/works/collect/all/list", counts[2]),
        };
        for (VideoListAdapter adapter : adapters) {
            adapter.setOnItemClickListener(getPresenter());
            adapter.requestData();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        UserDetailsData.ob(this, this::setUserData);
        TabLayout tab = get(R.id.tab);
        recycler = get(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (MutableLiveData<Integer> count : counts) {
                    count.removeObservers(getPresenter());
                }
                recycler.setAdapter(adapters[tab.getPosition()]);
                counts[tab.getPosition()].observe(getPresenter(), value -> {
                    recycler.setVisibility(value > 0 ? View.VISIBLE : View.GONE);
                    get(R.id.empty).setVisibility(value > 0 ? View.GONE : View.VISIBLE);
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tab.addTab(tab.newTab().setText("作品"));
        tab.addTab(tab.newTab().setText("喜欢"));
        tab.addTab(tab.newTab().setText("收藏"));
    }

    @SuppressLint("SetTextI18n")
    private void setUserData(User user) {
        TextView nickname = get(R.id.nickname);
        TextView username = get(R.id.username);
        ImageView head = get(R.id.head);
        TextView friend = get(R.id.friend_count);
        TextView follow = get(R.id.follow_count);
        TextView follower = get(R.id.follower_count);
        TextView age = get(R.id.age);
        ImageView gender = get(R.id.gender);
        TextView des = get(R.id.des);
        nickname.setText(user.getNickname());
        username.setText("账号: " + user.getUsername());
        Glide.with(getFragment())
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
            des.setText("点击右侧铅笔图标添加介绍，让大家认识你");
        } else {
            des.setTextColor(0xFF000000);
            des.setText(user.getDes());
        }
    }
}
