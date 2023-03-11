package com.lifengqiang.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lifengqiang.video.utils.DisplayUtils;

public class IndicatorBarView extends View {
    private int max = 3;
    private int index = 0;

    private int gapSize;

    public IndicatorBarView(Context context) {
        this(context, null);
    }

    public IndicatorBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IndicatorBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        gapSize = DisplayUtils.dp2px(context, 4);
    }

    public void setMax(int max) {
        this.max = Math.max(1, max);
        invalidate();
    }

    public void setIndex(int index) {
        this.index = index;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int itemWidth = (getWidth() - gapSize * (max + 1)) / max;
        int[] colors = {
                Color.parseColor("#ddffffff"),
                Color.parseColor("#66ffffff")
        };
        Paint paint = new Paint();
        int bottom = getHeight();
        for (int i = 0; i < max; i++) {
            if (i == index) {
                paint.setColor(colors[0]);
            } else {
                paint.setColor(colors[1]);
            }
            int left = gapSize * (i + 1) + itemWidth * i;
            int top = 0;
            int right = left + itemWidth;
            canvas.drawRoundRect(left, top, right, bottom, 8, 8, paint);
        }
    }
}
