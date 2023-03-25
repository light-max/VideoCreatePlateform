package com.lifengqiang.video.ui.main.space;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.utils.GlideRequests;

class WorksListAdapter extends SimpleSingleItemRecyclerAdapter<Works> {
    private Callback callback;

    public WorksListAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_space_works;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Works data, int position) {
        Glide.with(holder.itemView)
                .load(ExRequestBuilder.getUrl(data.getUserhead()))
                .apply(GlideRequests.skipCache())
                .into(holder.getImage(R.id.head));
        holder.setText(R.id.nickname, data.getNickname())
                .setText(R.id.date, data.getDate())
                .setText(R.id.remark_count, String.valueOf(data.getRemarkCount()))
                .setText(R.id.like_count, String.valueOf(data.getLikeCount()));
        ImageView icon = holder.getImage(R.id.like_icon);
        icon.setImageResource(data.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_dark);
        WorksItemView worksItemView = holder.get(R.id.works_view);
        worksItemView.setWorks(data);
        Runnable openUserSpace = () -> {
            if (callback != null) {
                callback.openUserSpace(data, position);
            }
        };
        holder.click(R.id.head, openUserSpace);
        holder.click(R.id.nickname, openUserSpace);
        holder.click(R.id.like, () -> {
            if (callback != null) {
                callback.like(data, position);
            }
        });
        holder.click(worksItemView, () -> {
            if (callback != null) {
                callback.openWorks(data, position);
            }
        });
    }

    public interface Callback {
        void openUserSpace(Works works, int position);

        void like(Works works, int position);

        void openWorks(Works works, int position);
    }
}
