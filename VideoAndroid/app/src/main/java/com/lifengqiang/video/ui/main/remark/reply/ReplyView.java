package com.lifengqiang.video.ui.main.remark.reply;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.result.Remark;
import com.lifengqiang.video.data.result.Reply;
import com.lifengqiang.video.utils.GlideRequests;

public class ReplyView extends BaseView<ReplyActivity> {
    private EditText input;
    private RecyclerView recyclerView;
    private ReplyAdapter adapter;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        input = get(R.id.input);
        recyclerView = get(R.id.recycler);
        adapter = new ReplyAdapter(getPresenter());
        recyclerView.setAdapter(adapter);
        click(R.id.reply, () -> {
            map("replyId", 0);
            input.setHint("我来说两句...");
            input.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) input
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        });
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                InputMethodManager inputMethodManager = (InputMethodManager) input
                        .getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                getPresenter().sendReply(input.getText().toString());
                input.setText("");
            }
            return false;
        });
    }

    public void setRemark(Remark remark) {
        ImageView head = get(R.id.head);
        TextView nickname = get(R.id.nickname);
        TextView me = get(R.id.me);
        TextView content = get(R.id.content);
        TextView date = get(R.id.date);
        TextView like = get(R.id.like_count);
        ImageView likeIcon = get(R.id.like_icon);
        Glide.with(getPresenter())
                .load(ExRequestBuilder.getUrl(remark.getUserHead()))
                .apply(GlideRequests.skipCache())
                .into(head);
        nickname.setText(remark.getUserNickname());
        me.setVisibility(remark.isMe() ? View.VISIBLE : View.GONE);
        content.setText(remark.getContent());
        date.setText(remark.getCreateDateTime());
        like.setText(String.valueOf(remark.getLikeCount()));
        likeIcon.setImageResource(remark.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_dark);
        click(R.id.like, () -> {
            Api.likeRemark(getPresenter().getRemarkId()).success(data -> {
                likeIcon.setImageResource(data.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_dark);
                like.setText(String.valueOf(data.getCount()));
            }).run();
        });
    }

    public ReplyAdapter getAdapter() {
        return adapter;
    }

    public void showInput(Reply reply) {
        map("replyId", reply.getUserId());
        input.setHint(reply.getUserNickname());
        input.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) input
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }
}
