package com.lifengqiang.video.ui.uspace;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Works;

class VideoListAdapter extends SimpleSingleItemRecyclerAdapter<Works> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_works_cover;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Works data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getCover()))
                .into(holder.getImage(R.id.icon));
        holder.setText(R.id.like_count, String.valueOf(data.getLikeCount()));
    }
}
