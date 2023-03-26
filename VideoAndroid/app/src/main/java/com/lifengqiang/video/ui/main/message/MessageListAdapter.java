package com.lifengqiang.video.ui.main.message;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.LastMessage;
import com.lifengqiang.video.utils.GlideRequests;
import com.lifengqiang.video.utils.MessageTimeUtils;

public class MessageListAdapter extends SimpleSingleItemRecyclerAdapter<LastMessage> {
    @Override
    protected int getItemViewLayout() {
        return R.layout.item_user_message;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, LastMessage data, int position) {
        ImageView head = holder.getImage(R.id.head);
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getUserhead()))
                .apply(GlideRequests.skipCache())
                .into(head);
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.content, data.getLastContent())
                .setText(R.id.time, MessageTimeUtils.getFuzzyTime(data.getLastTime()));
    }
}
