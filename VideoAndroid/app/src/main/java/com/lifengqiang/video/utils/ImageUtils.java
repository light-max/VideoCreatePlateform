package com.lifengqiang.video.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageUtils {
    public static void compress(List<File> files, File outputPath, Call call) {
        new Thread(() -> {
            List<File> outputs = new ArrayList<>();
            for (File file : files) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                int width = opts.outWidth;
                int height = opts.outHeight;
                if (width > 1920 || height > 1920) {
                    if (width > height) {
                        height = (int) (1920.f / width * height);
                        width = 1920;
                    } else {
                        width = (int) (1920.f / height * width);
                        height = 1920;
                    }
                }
                Bitmap source = BitmapFactory.decodeFile(file.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.postScale((float) width / opts.outWidth, (float) height / opts.outHeight);
                int orientation = getImageOrientation(file.getAbsolutePath());
                matrix.postRotate(orientation);
                Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, opts.outWidth, opts.outHeight, matrix, true);
                File cacheFile = new File(outputPath, UUID.randomUUID().toString() + ".jpg");
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(cacheFile));
                    outputs.add(cacheFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (call != null) {
                    call.onCall(outputs);
                }
            }
        }).start();
    }

    public static int getImageOrientation(String imageLocalPath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imageLocalPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ExifInterface.ORIENTATION_NORMAL;
        }
    }

    public interface Call {
        void onCall(List<File> files);
    }
}
