package com.lifengqiang.video.ui.create.submit.image;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;

import java.io.File;

class ImageAdapter extends SimpleSingleItemRecyclerAdapter<File> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.view_image_fill_max;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, File data, int position) {
        Glide.with(holder.itemView)
                .load(data)
                .into(holder.getImage(R.id.image));
    }
}
