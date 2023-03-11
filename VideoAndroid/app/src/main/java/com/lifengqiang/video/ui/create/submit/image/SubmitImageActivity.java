package com.lifengqiang.video.ui.create.submit.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.CaptionedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubmitImageActivity extends CaptionedActivity<SubmitImageView> {
    private List<File> files = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("发布图文");
        setContentView(R.layout.activity_submit_image);
    }

    public static void start(Context context, ArrayList<File> files) {
        Intent intent = new Intent(context, SubmitImageActivity.class);
        intent.putExtra("paths", files);
        context.startActivity(intent);
    }

    public List<File> getImageFiles() {
        if (files == null) {
            files = (ArrayList<File>) getIntent().getSerializableExtra("paths");
        }
        return files;
    }
}
