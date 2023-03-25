package com.lifengqiang.video.ui.main.home.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.call.ViewGet;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.main.remark.remark.RemarkActivity;
import com.lifengqiang.video.ui.uspace.UserSpaceActivity;
import com.lifengqiang.video.utils.GlideRequests;

public class WorksInfoView extends FrameLayout implements ViewGet {
    public WorksInfoView(@NonNull Context context) {
        this(context, null);
    }

    public WorksInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorksInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WorksInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View.inflate(context, R.layout.view_works_info, this);
    }

    public void setWorks(Works works) {
        ImageView head = get(R.id.head);
        ImageView addFollow = get(R.id.add_follow);
        ImageView like = get(R.id.like);
        TextView likeCount = get(R.id.like_count);
        ImageView remark = get(R.id.remark);
        TextView remarkCount = get(R.id.remark_count);
        ImageView collect = get(R.id.collect);
        TextView collectCount = get(R.id.collect_count);
        TextView content = get(R.id.content);
        Glide.with(this)
                .load(ExRequestBuilder.getUrl(works.getUserhead()))
                .apply(GlideRequests.skipCache())
                .into(head);
        addFollow.setVisibility(works.isFollwuser() ? View.GONE : View.VISIBLE);
        like.setImageResource(works.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_false);
        likeCount.setText(String.valueOf(works.getLikeCount()));
        remarkCount.setText(String.valueOf(works.getRemarkCount()));
        collect.setImageResource(works.isCollect() ? R.drawable.ic_collect_true : R.drawable.ic_collect_false);
        collectCount.setText(String.valueOf(works.getCollectCount()));
        content.setText(works.getContent());
        content.setMaxLines(2);
        click(head, () -> {
            Intent intent = new Intent(getContext(), UserSpaceActivity.class);
            intent.putExtra(UserSpaceActivity.USER_ID, works.getUserId());
            getContext().startActivity(intent);
        });
        click(addFollow, () -> {
            Api.follow(works.getUserId()).success(data -> {
                works.setFollwuser(data.isFollow());
                addFollow.setVisibility(data.isFollow() ? View.GONE : View.VISIBLE);
            }).run();
        });
        click(like, () -> {
            Api.likeWorks(works.getId()).success(data -> {
                works.setLike(data.isLike());
                works.setLikeCount(data.getCount());
                like.setImageResource(works.isLike() ? R.drawable.ic_love_true : R.drawable.ic_love_false);
                likeCount.setText(String.valueOf(works.getLikeCount()));
            }).run();
        });
        click(remark, () -> {
            Intent intent = new Intent(getContext(), RemarkActivity.class);
            intent.putExtra(RemarkActivity.WORKS_ID, works.getId());
            getContext().startActivity(intent);
        });
        click(collect, () -> {
            Api.collect(works.getId()).success(data -> {
                works.setCollect(data.isCollect());
                works.setCollectCount(data.getCount());
                collect.setImageResource(works.isCollect() ? R.drawable.ic_collect_true : R.drawable.ic_collect_false);
                collectCount.setText(String.valueOf(works.getCollectCount()));
            }).run();
        });
    }

    @Override
    public <T extends View> T get(int viewId) {
        return findViewById(viewId);
    }
}
