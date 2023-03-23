package com.lifengqiang.video.ui.follow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.ui.follow.user.UserListFragment;

public class FollowView extends BaseView<FollowActivity> {
    private final UserListFragment[] fragments = new UserListFragment[]{
            new UserListFragment(),
            new UserListFragment(),
            new UserListFragment(),
    };

    private final String[] urls = new String[]{
            "/user/friends",
            "/user/follow/list",
            "/user/follower/list",
    };

    private final String[] titles = new String[]{"朋友", "关注", "粉丝"};

    @Override
    public void onCreate(Bundle saveInstanceState) {
        for (int i = 0; i < fragments.length; i++) {
            fragments[i].setUrl(urls[i]);
        }
        TabLayout tab = get(R.id.tab);
        ViewPager pager = get(R.id.pager);
        tab.setupWithViewPager(pager);
        pager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager(), 0) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
    }
}
