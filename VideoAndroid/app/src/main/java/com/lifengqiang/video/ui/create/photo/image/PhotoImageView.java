package com.lifengqiang.video.ui.create.photo.image;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class PhotoImageView extends BaseView<PhotoImageActivity> {
    private RecyclerView recyclerView;
    private final PhotoImageAdapter adapter = new PhotoImageAdapter();

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        recyclerView = get(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public PhotoImageAdapter getAdapter() {
        return adapter;
    }
}
