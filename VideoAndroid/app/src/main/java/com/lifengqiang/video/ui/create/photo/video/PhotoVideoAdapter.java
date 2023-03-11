package com.lifengqiang.video.ui.create.photo.video;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;

import java.io.File;

class PhotoVideoAdapter extends SimpleSingleItemRecyclerAdapter<File> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_video_cover;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, File data, int position) {
        Glide.with(holder.itemView)
                .load(data)
                .override(600)
                .centerCrop()
                .into(holder.getImage(R.id.icon));
    }
}
