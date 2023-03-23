package com.lifengqiang.video.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;
import com.lifengqiang.video.ui.main.home.view.VerticalLooperVideoContainer;
import com.lifengqiang.video.ui.main.remark.remark.RemarkActivity;
import com.lifengqiang.video.utils.GlideRequests;

public class HomeView extends BaseMainPageChildView<HomeFragment> {
    private VerticalLooperVideoContainer videoContainer;

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        TextView content = get(R.id.content);
        click(content, () -> {
            if (content.getMaxLines() == 2) {
                content.setMaxLines(Integer.MAX_VALUE);
            } else {
                content.setMaxLines(2);
            }
        });
    }

    public VerticalLooperVideoContainer getVideoContainer() {
        if (videoContainer == null) {
            videoContainer = get(R.id.video_container);
        }
        return videoContainer;
    }

    @Override
    public void onResume() {
        if (getPresenter().isVisible()) {
            videoContainer.onResume();
        }
    }

    @Override
    public void onPause() {
        videoContainer.onPause();
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
        Glide.with(getFragment())
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
            getPresenter().startActivity(intent);
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
}
