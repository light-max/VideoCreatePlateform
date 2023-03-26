package com.lifengqiang.video.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;
import com.lifengqiang.video.data.result.Message;
import com.lifengqiang.video.utils.MessageTimeUtils;

import java.util.ArrayList;
import java.util.List;

class MessageListAdapter extends RecyclerView.Adapter<SimpleSingleItemRecyclerAdapter.ViewHolder> {
    private int sender = 0;
    private String receiverHead = "/head/0";
    private String meHead = "/head/0";
    private final List<Message> messages = new ArrayList<>();
    private LayoutInflater inflater;

    @NonNull
    @Override
    public SimpleSingleItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_message_me, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_message_you, parent, false);
        }
        return new SimpleSingleItemRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleSingleItemRecyclerAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (getItemViewType(position) == 0) {
            Glide.with(holder.itemView)
                    .load(ExRequestBuilder.getUrl(meHead))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.getImage(R.id.head));
        } else {
            Glide.with(holder.itemView)
                    .load(ExRequestBuilder.getUrl(receiverHead))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.getImage(R.id.head));
        }
        holder.setText(R.id.content, message.getContent());
        holder.setText(R.id.date, MessageTimeUtils.getFuzzyTime(message.getCreateTime()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messages.get(position);
        if (m.getSendId() == sender) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setReceiverHead(String receiverHead) {
        this.receiverHead = receiverHead;
    }

    public void setMeHead(String meHead) {
        this.meHead = meHead;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
