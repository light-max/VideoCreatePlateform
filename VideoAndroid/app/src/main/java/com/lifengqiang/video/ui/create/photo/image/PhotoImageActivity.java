package com.lifengqiang.video.ui.create.photo.image;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.CaptionedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoImageActivity extends CaptionedActivity<PhotoImageView> {
    public static final String IMAGE_FILES_PATH = "image_files_path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("选择图片");
        setContentView(R.layout.activity_photo);
        File[] dirs = {
                getExternalFilesDir("record/picture"),
                new File("/sdcard/DCIM"),
        };
        new Thread(() -> {
            List<File> files = new ArrayList<>();
            find(files, dirs[0]);
            find(files, dirs[1]);
            files.sort((o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
            mainHandler.post(() -> {
                view.getAdapter().setData(files);
                view.getAdapter().notifyDataSetChanged();
            });
        }).start();
        click(R.id.post, () -> {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_FILES_PATH, view.getAdapter().getSelectedFiles());
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void find(List<File> list, File path) {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    find(list, file);
                else {
                    String name = file.getName();
                    if (name.endsWith(".jpg") ||
                            name.endsWith(".jpeg") ||
                            name.endsWith(".png")) {
                        list.add(file);
                    }
                }
            }
        }
    }
}
