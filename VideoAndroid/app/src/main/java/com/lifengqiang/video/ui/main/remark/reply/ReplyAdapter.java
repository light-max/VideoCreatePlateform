package com.lifengqiang.video.ui.main.remark.reply;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Reply;
import com.lifengqiang.video.utils.GlideRequests;

public class ReplyAdapter extends SimpleSingleItemRecyclerAdapter<Reply> {
    private final Callback callback;

    public ReplyAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_reply;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Reply data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getUserhead()))
                .apply(GlideRequests.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getUserNickname())
                .setText(R.id.to_username, data.getToUserNickname())
                .setText(R.id.content, data.getContent())
                .setText(R.id.date, data.getCreateDateTime());
        if (data.isToReply()) {
            TextView text = holder.getText(R.id.to_username);
            text.setVisibility(View.VISIBLE);
            text.setText(data.getToUserNickname());
        } else {
            holder.getText(R.id.to_username).setVisibility(View.GONE);
        }
        holder.click(R.id.head, () -> {
            if (callback != null) {
                callback.onOpenSpace(data.getUserId(), position);
            }
        }).click(R.id.nickname, () -> {
            if (callback != null) {
                callback.onOpenSpace(data.getUserId(), position);
            }
        }).click(R.id.to_username, () -> {
            if (callback != null) {
                callback.onOpenSpace(data.getReplyId(), position);
            }
        }).click(R.id.reply, () -> {
            if (callback != null) {
                callback.onReply(data, position);
            }
        });
    }

    public interface Callback {
        void onOpenSpace(int userId, int position);

        void onReply(Reply reply, int position);
    }
}
