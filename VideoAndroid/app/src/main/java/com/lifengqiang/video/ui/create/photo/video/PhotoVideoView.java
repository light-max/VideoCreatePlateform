package com.lifengqiang.video.ui.create.photo.video;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class PhotoVideoView extends BaseView<PhotoVideoActivity> {
    private RecyclerView recyclerView;
    private final PhotoVideoAdapter adapter = new PhotoVideoAdapter();

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        recyclerView = get(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        get(R.id.post).setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
    }

    public PhotoVideoAdapter getAdapter() {
        return adapter;
    }
}
