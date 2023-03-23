package com.lifengqiang.video.ui.follow.user;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.UserFollow;
import com.lifengqiang.video.utils.GlideRequests;

class UserListAdapter extends SimpleSingleItemRecyclerAdapter<UserFollow> {
    private Callback callback;

    public UserListAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_follow_user;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, UserFollow data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getHead()))
                .apply(GlideRequests.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname());
        if (data.isFriend()) {
            holder.setText(R.id.toggle, "互相关注");
        } else if (data.isFollow()) {
            holder.setText(R.id.toggle, "已关注");
        } else if (data.isFollower()) {
            holder.setText(R.id.toggle, "回关");
        } else {
            holder.setText(R.id.toggle, "关注");
        }
        Runnable openSpace = () -> {
            if (callback != null) {
                callback.openUserSpace(data, position);
            }
        };
        holder.click(R.id.head, openSpace);
        holder.click(R.id.nickname, openSpace);
        holder.click(R.id.toggle, () -> {
            if (callback != null) {
                callback.followToggle(data, position);
            }
        });
    }

    public interface Callback {
        void openUserSpace(UserFollow user, int position);

        void followToggle(UserFollow user, int position);
    }
}
