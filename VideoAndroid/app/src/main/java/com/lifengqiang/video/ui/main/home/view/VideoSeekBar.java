package com.lifengqiang.video.ui.main.home.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class VideoSeekBar extends View {
    private final Paint paint;
    private int progress = 0;
    private int max = 100;

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public VideoSeekBar(Context context) {
        this(context, null);
    }

    public VideoSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0x66FFFFFF);
        canvas.drawRect(0, 0, getWidth(), getBottom(), paint);
        int p = (int) ((float) progress / (float) max * (float) getWidth());
        paint.setColor(0x99FFFFFF);
        canvas.drawRect(0, 0, p, getBottom(), paint);
    }
}
