package com.lifengqiang.video.util;

import java.io.*;

public class FileTools {
    public static File getFileMakePath(String parent, String name) {
        File file = new File(parent);
        file.mkdirs();
        return new File(file, name);
    }

    public static void write(InputStream in, File outPath) throws IOException {
        OutputStream out = new FileOutputStream(outPath);
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.flush();
        in.close();
        out.close();
    }

    public static File getHeadImagePath(String parent, Integer id) {
        File file = new File(parent);
        file.mkdirs();
        return new File(parent, String.valueOf(id));
    }
}
