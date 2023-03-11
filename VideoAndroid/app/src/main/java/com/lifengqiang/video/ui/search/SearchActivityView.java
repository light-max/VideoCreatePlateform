package com.lifengqiang.video.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.ui.search.user.SearchUserFragment;
import com.lifengqiang.video.ui.search.video.SearchVideoFragment;
import com.lifengqiang.video.view.SearchView;

public class SearchActivityView extends BaseView<SearchActivity> {
    private final SearchFragment<?>[] fragments = new SearchFragment<?>[]{
            new SearchVideoFragment(),
            new SearchUserFragment()
    };

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        SearchView searchView = get(R.id.search);
        searchView.setOnSearchListener(text -> {

        });
        TabLayout tab = get(R.id.tab);
        ViewPager pager = get(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(presenter.getSupportFragmentManager(), 0) {
            private final String[] titles = new String[]{"视频", "用户"};

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
        tab.setupWithViewPager(pager);
    }
}
