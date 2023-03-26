package com.lifengqiang.video.ui.remark.reply;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.data.result.Reply;
import com.lifengqiang.video.ui.uspace.UserSpaceActivity;
import com.lifengqiang.video.utils.AndroidBug5497Workaround;

public class ReplyActivity extends CaptionedActivity<ReplyView> implements ReplyAdapter.Callback {
    public static final String REMARK_ID = "remark_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map("replyId", 0);
        setPageTitle("查看回复");
        setContentView(R.layout.activity_reply);
        AndroidBug5497Workaround.assistActivity(this);
        loadRemark();
        loadReplyList();
    }

    public void loadRemark() {
        Api.getRemark(getRemarkId()).success(data -> {
            view.setRemark(data);
        }).run();
    }

    public void loadReplyList() {
        Api.getReplyList(getRemarkId()).success(data -> {
            view.getAdapter().setData(data.getList());
            view.getAdapter().notifyDataSetChanged();
        }).run();
    }

    public int getRemarkId() {
        return getIntent().getIntExtra(REMARK_ID, 0);
    }

    public void sendReply(String content) {
        Api.sendReply(getRemarkId(), content, map("replyId"))
                .error((message, e) -> toast(message))
                .success(() -> {
                    loadRemark();
                    loadReplyList();
                }).run();
    }

    @Override
    public void onOpenSpace(int userId, int position) {
        Intent intent = new Intent(this, UserSpaceActivity.class);
        intent.putExtra(UserSpaceActivity.USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onReply(Reply reply, int position) {
        getIView().showInput(reply);
    }
}
