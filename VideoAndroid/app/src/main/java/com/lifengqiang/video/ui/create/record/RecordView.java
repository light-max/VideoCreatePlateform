package com.lifengqiang.video.ui.create.record;

import android.os.Bundle;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.camera.CameraPreviewView;

public class RecordView extends BaseView<RecordActivity> {
    private CameraPreviewView previewView;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        previewView = get(R.id.preview);
    }

    public CameraPreviewView getPreviewView() {
        return previewView;
    }
}
