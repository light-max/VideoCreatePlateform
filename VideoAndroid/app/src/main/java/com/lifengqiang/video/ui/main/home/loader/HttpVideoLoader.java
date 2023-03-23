package com.lifengqiang.video.ui.main.home.loader;

import android.content.Context;

import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.net.result.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpVideoLoader {
    public static void load(Context context, String url, Call call) {
        File file = new File(context.getExternalCacheDir(), url.substring(url.lastIndexOf('/')));
        if (file.exists()) {
            call.onSuccess(file);
        } else {
            new Thread(() -> {
                try {
                    Result result = ExRequestBuilder.get(url)
                            .useStream()
                            .execute();
                    InputStream in = result.inputStream();
                    OutputStream out = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = in.read(bytes)) >= 0) {
                        out.write(bytes, 0, len);
                    }
                    in.close();
                    out.close();
                    call.onSuccess(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    call.onError("网络错误");
                }
            }).start();
        }
    }

    public static File getFileByUrl(Context context, String url) {
        return new File(context.getExternalCacheDir(), url.substring(url.lastIndexOf('/')));
    }

    public interface Call {
        void onSuccess(File file);

        void onError(String msg);
    }
}
