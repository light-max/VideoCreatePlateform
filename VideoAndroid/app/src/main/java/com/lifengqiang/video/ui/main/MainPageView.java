package com.lifengqiang.video.ui.main;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.ui.main.home.HomeFragment;
import com.lifengqiang.video.ui.main.message.MessageFragment;
import com.lifengqiang.video.ui.main.mine.MineFragment;
import com.lifengqiang.video.ui.main.space.SpaceFragment;

public class MainPageView extends BaseView<MainPageActivity> {
    private final PresenterFragment<?>[] fragments = new PresenterFragment<?>[]{
            new HomeFragment(),
            new SpaceFragment(),
            new MessageFragment(),
            new MineFragment()
    };

    private final int[] labels = new int[]{
            R.id.home,
            R.id.space,
            R.id.message,
            R.id.mine
    };
    private final boolean[][] themes = new boolean[][]{
            {true, true, false, true},
            {true, true, false, false},
    };

    private Fragment currentFragment = null;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        RadioGroup group = get(R.id.group);
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            for (int i = 0; i < labels.length; i++) {
                if (labels[i] == checkedId) {
                    switchFragment(fragments[i]);
                    presenter.setStatusBar(themes[0][i]);
                    setNavigationStyle(themes[1][i]);
                }
            }
        });
        group.check(R.id.home);
    }

    private void switchFragment(PresenterFragment<?> fragment) {
        if (fragment == currentFragment) return;
        FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        if (currentFragment != null) {
            if (!currentFragment.isHidden()) {
                transaction.hide(currentFragment);
            }
        }
        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                transaction.show(fragment);
            }
        } else {
            transaction.add(R.id.container, fragment);
        }
        transaction.commit();
        currentFragment = fragment;
    }

    private void setNavigationStyle(boolean isDark) {
        Resources resources = getContext().getResources();
        ImageView create = get(R.id.create);
        int iconColor = resources.getColor(isDark ? R.color.white : R.color.black, null);
        create.setColorFilter(iconColor);
        int colorId = isDark ?
                R.color.home_navigation_label_color_dark :
                R.color.home_navigation_label_color_light;
        ColorStateList colorStateList = resources.getColorStateList(colorId, null);
        RadioGroup group = get(R.id.group);
        for (int i = 0; i < group.getChildCount(); i++) {
            View childView = group.getChildAt(i);
            if (childView instanceof RadioButton) {
                RadioButton button = (RadioButton) childView;
                button.setTextColor(colorStateList);
            }
        }
    }
}
