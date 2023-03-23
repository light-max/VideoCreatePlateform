package com.lifengqiang.video.ui.main.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.data.result.Works;

public class VerticalLooperVideoContainer extends ViewGroup {
    private PointF startPoint = new PointF(0, 0);
    private PointF currentPoint = new PointF(0, 0);
    private boolean animation = false;

    private DataSource dataSource;

    private ImageView previousCover;
    private ImageView nextCover;
    private VideoViewContainer videoView;
    /**
     * 0 正在判定
     * 1 判定为上下滑动
     * 2 判定为子布局的左右滑动
     */
    private int mode = 0;

    public VerticalLooperVideoContainer(Context context) {
        this(context, null);
    }

    public VerticalLooperVideoContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalLooperVideoContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VerticalLooperVideoContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        previousCover = new ImageView(getContext());
        nextCover = new ImageView(getContext());
        videoView = new VideoViewContainer(getContext());
        previousCover.setScaleType(ImageView.ScaleType.FIT_CENTER);
        nextCover.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(previousCover, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(nextCover, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(videoView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void onResume() {
        videoView.onResume();
    }

    public void onPause() {
        videoView.onPause();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (animation || mode == 1) {
            float v_diff = currentPoint.y - startPoint.y;
            int mHeight = getHeight();
            int mWidth = getWidth();
            previousCover.layout(0, -mHeight + (int) v_diff, mWidth, (int) v_diff);
            videoView.layout(0, (int) v_diff, mWidth, mHeight + (int) v_diff);
            nextCover.layout(0, mHeight + (int) v_diff, mWidth, mHeight * 2 + (int) v_diff);
        } else {
            previousCover.layout(0, 0, 0, 0);
            videoView.layout(l, t, r, b);
            nextCover.layout(0, 0, 0, 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (dataSource == null) {
            return super.onTouchEvent(e);
        }
        if (animation) {
            return super.onTouchEvent(e);
        }
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startPoint.x = e.getX();
                startPoint.y = e.getY();
                currentPoint.x = e.getX();
                currentPoint.y = e.getY();
                mode = 0;
                videoView.getImageView().touchViewPager(e);
                return true;
            case MotionEvent.ACTION_MOVE:
                currentPoint.x = e.getX();
                currentPoint.y = e.getY();
                switch (mode) {
                    case 0:
                        float v_diff = startPoint.y - currentPoint.y;
                        float h_diff = startPoint.x - currentPoint.x;
                        if (Math.max(Math.abs(v_diff), Math.abs(h_diff)) > 50) {
                            if (Math.abs(v_diff) > Math.abs(h_diff)) {
                                mode = 1;
                                loadViewCover();
                                verticalScrollView();
                                return true;
                            } else {
                                mode = 2;
                                return false;
                            }
                        } else {
                            return true;
                        }
                    case 1:
                        verticalScrollView();
                        return true;
                    case 2:
                        videoView.getImageView().touchViewPager(e);
                        return false;
                }
            case MotionEvent.ACTION_UP:
                mode = 0;
                float v_diff = startPoint.y - currentPoint.y;
                if (Math.abs(v_diff) > 100) {
                    if (v_diff < 0 && !dataSource.hasPrevious()) {
                        Toast.makeText(getContext(), "刷新数据", Toast.LENGTH_SHORT).show();
                        dataSource.resetData();
                        postResAnimation();
                    } else {
                        postMoveAnimation();
                    }
                } else {
                    postResAnimation();
                }
                videoView.getImageView().touchViewPager(e);
                return false;
        }
        return false;
    }

    public void loadVideo() {
        if (dataSource == null) return;
        if (dataSource.hasCurrent()) {
            dataSource.getWorks(dataSource.getCurrentId(), works -> {
                videoView.setWorks(works);
                dataSource.loadWorksInfo(works);
            });
        }
    }

    private void loadViewCover() {
        boolean uppull = currentPoint.y < startPoint.y;
        if (!uppull) {
            if (dataSource.hasPrevious()) {
                dataSource.getWorks(dataSource.getPreviousId(), works -> {
                    Glide.with(this)
                            .load(ExRequestBuilder.getUrl(works.getCover()))
                            .into(previousCover);
                });
            } else {
                previousCover.setImageDrawable(null);
            }
        } else {
            if (dataSource.hasNext()) {
                dataSource.getWorks(dataSource.getNextId(), works -> {
                    Glide.with(this)
                            .load(ExRequestBuilder.getUrl(works.getCover()))
                            .into(nextCover);
                });
            } else {
                previousCover.setImageDrawable(null);
            }
        }
    }

    private void verticalScrollView() {
        requestLayout();
    }

    private void postResAnimation() {
        requestLayout();
//        previousCover.layout(0, 0, 0, 0);
//        videoView.layout(0, 0, getWidth(), getHeight());
//        nextCover.layout(0, 0, 0, 0);
    }

    private void postMoveAnimation() {
        animation = true;
        Handler backgroundHandler = dataSource.getBackgroundHandler();
        Handler mainHandler = dataSource.getMainHandler();
        backgroundHandler.post(() -> {
            float distance = currentPoint.y - startPoint.y > 0 ? 10 : -10;
            while (true) {
                float diff = currentPoint.y - startPoint.y;
                if (Math.abs(diff) >= getHeight()) {
                    dataSource.move(distance < 0);
                    mainHandler.post(() -> {
                        ImageView cover = videoView.getCoverImage();
                        cover.setVisibility(VISIBLE);
                        if (distance < 0) {
                            cover.setImageDrawable(nextCover.getDrawable());
                        } else {
                            cover.setImageDrawable(previousCover.getDrawable());
                        }
                        postResAnimation();
                        if (dataSource.hasCurrent()) {
                            dataSource.getWorks(dataSource.getCurrentId(), works -> {
                                videoView.setWorks(works);
                                dataSource.loadWorksInfo(works);
                            });
                        }
                        animation = false;
                    });
                    break;
                }
                currentPoint.y += distance;
                SystemClock.sleep(1);
                mainHandler.post(this::verticalScrollView);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取父控件的宽高建议值
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        //测量逻辑
        int width = 0;
        int height = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            width = Math.max(childWidth, width);
            height += childHeight;
        }
        //设置测量建议值
        setMeasuredDimension(
                (measureWidthMode == MeasureSpec.EXACTLY) ? measureWidthSize : width,
                (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeightSize : height
        );
    }

    public interface DataSource {
        boolean hasPrevious();

        boolean hasNext();

        boolean hasCurrent();

        int getPreviousId();

        int getCurrentId();

        int getNextId();

        void getWorks(int id, OnWorksCall call);

        void move(boolean next);

        Handler getBackgroundHandler();

        Handler getMainHandler();

        void resetData();

        void loadWorksInfo(Works works);
    }

    public interface OnWorksCall {
        void onCall(Works works);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
