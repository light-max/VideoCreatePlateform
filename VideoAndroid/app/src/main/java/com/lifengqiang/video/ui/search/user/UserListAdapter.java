package com.lifengqiang.video.ui.search.user;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.SearchValue;
import com.lifengqiang.video.utils.GlideRequests;

class UserListAdapter extends SimpleSingleItemRecyclerAdapter<SearchValue> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_search_user;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, SearchValue data, int position) {
        ImageView head = holder.getImage(R.id.head);
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getCover()))
                .apply(GlideRequests.skipCache())
                .into(head);
        holder.setText(R.id.nickname, data.getPrimary());
    }
}
