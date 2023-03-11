package com.lifengqiang.video.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.call.ViewGet;

public class CameraControllerView extends LinearLayout implements ViewGet {
    private Callback mCallback;
    private boolean mActive = true;

    private int state;

    private static final int STATE_RECORD_STOP = 0;
    private static final int STATE_RECORD_RUNNING = 1;
    private static final int STATE_PICTURES_PREPARE = 2;

    public CameraControllerView(Context context) {
        this(context, null);
    }

    public CameraControllerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraControllerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View.inflate(context, R.layout.view_camera_controller, this);
        RadioGroup group = get(R.id.camera_mode);
        ImageView icon = get(R.id.icon);
        group.setOnCheckedChangeListener((g, checkedId) -> {
            if (checkedId == R.id.mode_video) {
                state = STATE_RECORD_STOP;
                icon.setImageResource(R.drawable.ic_camera_record_start);
                if (mCallback != null) {
                    mCallback.onSelectRecord(this);
                }
            } else if (checkedId == R.id.mode_image) {
                state = STATE_PICTURES_PREPARE;
                icon.setImageResource(R.drawable.ic_camera_picture);
                if (mCallback != null) {
                    mCallback.onSelectPicture(this);
                }
            }
        });
        click(R.id.photo_album, () -> {
            if (mCallback != null) {
                mCallback.onOpenPhotoAlbum(this);
            }
        });
        click(R.id.flip, () -> {
            if (mCallback != null) {
                mCallback.onToggleCamera(this);
            }
        });
        click(icon, () -> {
            if (!isActive()) return;
            switch (state) {
                case STATE_RECORD_STOP:
                    if (mCallback != null) {
                        mCallback.onStartRecord(this);
                    }
                    break;
                case STATE_RECORD_RUNNING:
                    if (mCallback != null) {
                        mCallback.onStopRecord(this);
                    }
                    break;
                case STATE_PICTURES_PREPARE:
                    if (mCallback != null) {
                        mCallback.onTakePicture(this);
                    }
                    break;
            }
        });
        group.check(R.id.mode_image);
    }

    @Override
    public <T extends View> T get(int viewId) {
        return findViewById(viewId);
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }

    public void start() {
        get(R.id.camera_mode).setVisibility(GONE);
        get(R.id.photo_album).setVisibility(GONE);
        get(R.id.flip).setVisibility(GONE);
        ImageView icon = get(R.id.icon);
        icon.setImageResource(R.drawable.ic_camera_record_stop);
        state = STATE_RECORD_RUNNING;
    }

    public void stop() {
        get(R.id.camera_mode).setVisibility(VISIBLE);
        get(R.id.photo_album).setVisibility(VISIBLE);
        get(R.id.flip).setVisibility(VISIBLE);
        ImageView icon = get(R.id.icon);
        icon.setImageResource(R.drawable.ic_camera_record_start);
        state = STATE_RECORD_STOP;
    }

    public interface Callback {
        void onSelectRecord(CameraControllerView view);

        void onSelectPicture(CameraControllerView view);

        void onStartRecord(CameraControllerView view);

        void onStopRecord(CameraControllerView view);

        void onTakePicture(CameraControllerView view);

        void onToggleCamera(CameraControllerView view);

        void onOpenPhotoAlbum(CameraControllerView view);
    }
}
