package com.lifengqiang.video.ui.main.mine;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Works;

class VideoListAdapter extends SimpleSingleItemRecyclerAdapter<Works> {
    private final String url;
    private final MutableLiveData<Integer> count;

    public void requestData() {
        ExRequestBuilder.get(url).asyncList(Works.class)
                .success(data -> {
                    count.setValue(data.size());
                    setData(data);
                    notifyDataSetChanged();
                }).run();
    }

    public VideoListAdapter(String url, MutableLiveData<Integer> count) {
        this.url = url;
        this.count = count;
    }

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
