package com.lifengqiang.video.ui.create.photo.video;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.CaptionedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoVideoActivity extends CaptionedActivity<PhotoVideoView> {
    public final static String VIDEO_FILE_PATH = "video_file_path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("选择视频");
        setContentView(R.layout.activity_photo);
        File[] dirs = {
                getExternalFilesDir("record/video"),
                new File("/sdcard/DCIM"),
                new File("/sdcard/Download")
        };
        new Thread(() -> {
            List<File> files = new ArrayList<>();
            find(files, dirs[0]);
            find(files, dirs[1]);
            find(files, dirs[2]);
            files.sort((o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
            mainHandler.post(() -> {
                view.getAdapter().setData(files);
                view.getAdapter().notifyDataSetChanged();
            });
        }).start();
        view.getAdapter().setOnItemClickListener((data, position) -> {
            Intent intent = new Intent();
            intent.putExtra(VIDEO_FILE_PATH, data);
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
                    if (name.endsWith(".mp4") ||
                            name.endsWith(".MP4") ||
                            name.endsWith(".avi") ||
                            name.endsWith(".AVI")) {
                        list.add(file);
                    }
                }
            }
        }
    }
}
