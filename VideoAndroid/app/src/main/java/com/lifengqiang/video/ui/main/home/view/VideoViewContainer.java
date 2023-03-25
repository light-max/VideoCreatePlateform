package com.lifengqiang.video.ui.main.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.data.result.Works;

public class VideoViewContainer extends LinearLayout {
    private VideoPlayerView videoView;
    private ImagePlayerView imageView;
    private ImageView coverImage;

    private Works works;

    public VideoViewContainer(@NonNull Context context) {
        this(context, null);
    }

    public VideoViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_video_container, this);
        videoView = findViewById(R.id.video_player);
        imageView = findViewById(R.id.image_player);
        coverImage = findViewById(R.id.cover);
        videoView.setVideoViewContainer(this);
        imageView.setVideoViewContainer(this);
    }

    public void onResume() {
        videoView.onResume();
    }

    public void onPause() {
        videoView.onLifePause();
    }

    public ImagePlayerView getImageView() {
        return imageView;
    }

    public void setWorks(Works works) {
        if (works.getType() == 0) {
            videoView.setVisibility(GONE);
            videoView.pause();
            imageView.setWorks(works);
        } else if (works.getType() == 1) {
            imageView.setVisibility(GONE);
            videoView.setWorks(works);
        }
        this.works = works;
    }

    public void hideCover() {
        coverImage.setVisibility(GONE);
    }

    public ImageView getCoverImage() {
        return coverImage;
    }

    public void release() {
        videoView.release();
    }
}
