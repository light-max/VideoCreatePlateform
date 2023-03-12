package com.lifengqiang.video.ui.create.submit.image;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.utils.ImageUtils;

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
        click(R.id.post, () -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setView(new ProgressBar(getContext()))
                    .setCancelable(false)
                    .setMessage("正在上传...")
                    .create();
            ImageUtils.compress(getImageFiles(), getExternalCacheDir(), files -> {
                Api.submitImages(view.getContent(), files)
                        .before(dialog::show)
                        .after(dialog::dismiss)
                        .error((message, e) -> toast(message))
                        .success(() -> {
                            toast("发布成功");
                            finish();
                        }).run();
            });
        });
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
