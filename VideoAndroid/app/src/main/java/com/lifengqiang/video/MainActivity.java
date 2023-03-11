package com.lifengqiang.video;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lifengqiang.video.ui.account.login.LoginActivity;
import com.lifengqiang.video.utils.CameraPermission;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (checkPermission()) {
            goMainPageActivity();
        } else {
            Toast.makeText(this, "请先授权程序权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CameraPermission.PERMISSION_CODE) {
            if (CameraPermission.isPermissionGranted(this)) {
                goMainPageActivity();
            } else {
                finish();
            }
        }
    }

    private boolean checkPermission() {
        if (CameraPermission.isPermissionGranted(this)) {
            return true;
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("程序运行需要获取下列权限: 文件读写、相机、录音，是否同意获取")
                    .setNegativeButton("拒绝，退出程序", (dialog, which) -> finish())
                    .setPositiveButton("同意，去获取", (dialog, which) -> CameraPermission.checkPermission(this))
                    .show();
            return false;
        }
    }

    private void goMainPageActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.auto, true);
            startActivity(intent);
            finish();
        }, 1000);
    }
}