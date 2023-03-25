package com.lifengqiang.video.ui.search.video;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.utils.GlideRequests;
import com.lifengqiang.video.view.MaskImageView;

class WorksListAdapter extends SimpleSingleItemRecyclerAdapter<Works> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_search_works;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Works data, int position) {
        ImageView head = holder.getImage(R.id.head);
        MaskImageView coverBg = holder.get(R.id.cover_bg);
        ImageView cover = holder.get(R.id.cover);
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getUserhead()))
                .apply(GlideRequests.skipCache())
                .into(head);
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getCover()))
                .apply(coverBg.setGaussBlur())
                .into(coverBg);
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getCover()))
                .into(cover);
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.date, data.getDate())
                .setText(R.id.content, data.getContent());
    }
}
