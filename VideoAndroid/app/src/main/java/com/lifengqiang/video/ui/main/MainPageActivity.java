package com.lifengqiang.video.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.activity.FullScreenActivity;
import com.lifengqiang.video.ui.account.login.LoginActivity;
import com.lifengqiang.video.ui.create.photo.image.PhotoImageActivity;
import com.lifengqiang.video.ui.create.photo.video.PhotoVideoActivity;
import com.lifengqiang.video.ui.create.record.RecordActivity;
import com.lifengqiang.video.ui.create.submit.image.SubmitImageActivity;
import com.lifengqiang.video.ui.create.submit.video.SubmitVideoActivity;
import com.lifengqiang.video.ui.setting.SettingActivity;

import java.io.File;
import java.util.ArrayList;

public class MainPageActivity extends FullScreenActivity<MainPageView> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setStatusBar(true);
        click(R.id.create, () -> RecordActivity.startImage(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingActivity.LOGOUT && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.auto, false);
            startActivity(intent);
            finish();
        } else if (resultCode == RESULT_OK) {
            if (requestCode == CreateModelDialog.REQUEST_VIDEO && data != null) {
                File file = (File) data.getSerializableExtra(PhotoVideoActivity.VIDEO_FILE_PATH);
                if (file == null) {
                    toast("没有视频");
                } else {
                    SubmitVideoActivity.start(this, file);
                }
            } else if (requestCode == CreateModelDialog.REQUEST_IMAGE && data != null) {
                ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra(PhotoImageActivity.IMAGE_FILES_PATH);
                if (files == null || files.isEmpty()) {
                    toast("请选择至少一张图片");
                } else {
                    SubmitImageActivity.start(this, files);
                }
            }
        }
    }
}
