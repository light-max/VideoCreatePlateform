package com.lifengqiang.video.ui.remark.remark;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.utils.AndroidBug5497Workaround;

public class RemarkActivity extends CaptionedActivity<RemarkView> {
    public static String WORKS_ID = "works_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("查看评论");
        setContentView(R.layout.activity_remark);
        AndroidBug5497Workaround.assistActivity(this);
        loadNewData();
    }

    public void loadNewData() {
        Api.getRemarkList(getWorksId()).success(data -> {
            getIView().getAdapter().setData(data.getList());
            getIView().getAdapter().notifyDataSetChanged();
        }).run();
    }

    public int getWorksId() {
        return getIntent().getIntExtra(WORKS_ID, 0);
    }

    public void sendRemark(String content) {
        Api.sendRemark(getWorksId(), content)
                .error((message, e) -> toast(message))
                .success(() -> {
                    view.clearInputText();
                    toast("发送成功");
                    loadNewData();
                }).run();
    }
}
