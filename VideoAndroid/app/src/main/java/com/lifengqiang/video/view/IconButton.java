package com.lifengqiang.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;

public class IconButton extends LinearLayout {
    public IconButton(Context context) {
        this(context, null, 0, 0);
    }

    public IconButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public IconButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("Recycle")
    public IconButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View.inflate(context, R.layout.view_icon_button, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IconButton);
        Drawable icon = array.getDrawable(R.styleable.IconButton_bt_icon);
        String text = array.getString(R.styleable.IconButton_bt_text);
        ImageView imageView = findViewById(R.id.icon);
        TextView textView = findViewById(R.id.text);
        imageView.setImageDrawable(icon);
        textView.setText(text);
    }
}
