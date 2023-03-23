package com.lifengqiang.video.ui.main.remark.remark;

import android.view.View;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Remark;
import com.lifengqiang.video.utils.GlideRequests;

class RemarkAdapter extends SimpleSingleItemRecyclerAdapter<Remark> {
    private Callback callback;

    public RemarkAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_remark;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Remark data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getUserHead()))
                .apply(GlideRequests.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getUserNickname())
                .setText(R.id.content, data.getContent())
                .setText(R.id.date, data.getCreateDateTime())
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()));
        if (data.getReplyCount() > 0) {
            holder.get(R.id.reply_open).setVisibility(View.VISIBLE);
            holder.setText(R.id.reply_count, "查看所有共" + data.getReplyCount() + "条回复");
        } else {
            holder.get(R.id.reply_open).setVisibility(View.GONE);
        }
        holder.getText(R.id.me).setVisibility(data.isMe() ? View.VISIBLE : View.GONE);
        holder.getImage(R.id.like_icon).setImageResource(
                data.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_dark);
        Runnable space = () -> {
            if (callback != null) {
                callback.onOpenUserSpace(data, position);
            }
        };
        holder.click(R.id.head, space)
                .click(R.id.nickname, space);
        holder.click(R.id.reply, () -> {
            if (callback != null) {
                callback.onReply(data, position);
            }
        }).click(R.id.reply_open, () -> {
            if (callback != null) {
                callback.onOpenReply(data, position);
            }
        }).click(R.id.like, () -> {
            if (callback != null) {
                callback.onLike(data, position);
            }
        });
    }

    public interface Callback {
        void onOpenUserSpace(Remark data, int position);

        void onReply(Remark data, int position);

        void onLike(Remark data, int position);

        void onOpenReply(Remark data, int position);
    }
}
