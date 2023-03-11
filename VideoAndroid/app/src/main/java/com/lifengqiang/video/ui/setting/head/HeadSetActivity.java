package com.lifengqiang.video.ui.setting.head;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.activity.CaptionedActivity;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.observer.UserDetailsData;
import com.lifengqiang.video.utils.FileUtil;
import com.lifengqiang.video.utils.GlideRequests;
import com.lifengqiang.video.utils.PermissionUtil;

import java.io.File;

public class HeadSetActivity extends CaptionedActivity<BaseView<?>> {
    public static final int PICTURE = 0x2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTitle("更换头像");
        setContentView(R.layout.activity_head_set);
        UserDetailsData.ob(this, user -> {
            Glide.with(this)
                    .load(ExRequestBuilder.getUrl(user.getHead()))
                    .apply(GlideRequests.skipCache())
                    .into(this.<ImageView>get(R.id.image));
        });
        click(R.id.upload, () -> {
            if (PermissionUtil.localStorage(getActivity())) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                this.startActivityForResult(intent, PICTURE);
            } else {
                toast("没有文件读取权限");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }
        // 选择照片
        else if (requestCode == PICTURE) {
            Uri uri = data.getData();
            assert uri != null;
            String filePath = FileUtil.getFilePathByUri(getContext(), uri);
            File file = new File(filePath);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("上传中")
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            Api.setHead(file).error((message, e) -> toast(message))
                    .before(dialog::show)
                    .after(dialog::dismiss)
                    .success(() -> Api.getUserDetails().run())
                    .run();
        }
    }
}
