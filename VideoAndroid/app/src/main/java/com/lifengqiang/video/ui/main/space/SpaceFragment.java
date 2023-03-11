package com.lifengqiang.video.ui.main.space;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.ui.search.SearchActivity;

public class SpaceFragment extends PresenterFragment<SpaceView> {
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
    }
}
