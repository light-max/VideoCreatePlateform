package com.lifengqiang.video.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.ui.create.photo.image.PhotoImageActivity;
import com.lifengqiang.video.ui.create.photo.video.PhotoVideoActivity;
import com.lifengqiang.video.ui.create.record.RecordActivity;

public class CreateModelDialog {
    public static final int REQUEST_VIDEO = 2;
    public static final int REQUEST_IMAGE = 3;

    public static void show(Activity activity, View parent) {
        View view = View.inflate(activity, R.layout.dlg_create_model, null);
        PopupWindow window = new PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAsDropDown(parent);
        TextView video = view.findViewById(R.id.video);
        TextView image = view.findViewById(R.id.image);
        TextView camera = view.findViewById(R.id.camera);
        video.setOnClickListener(v -> {
            window.dismiss();
            Intent intent = new Intent(activity, PhotoVideoActivity.class);
            activity.startActivityForResult(intent, REQUEST_VIDEO);
        });
        image.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PhotoImageActivity.class);
            activity.startActivityForResult(intent, REQUEST_IMAGE);
            window.dismiss();
        });
        camera.setOnClickListener(v -> {
            RecordActivity.startVideo(activity);
            window.dismiss();
        });
    }
}
