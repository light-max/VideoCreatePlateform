package com.lifengqiang.video.util.datetranslate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String format(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }

    public static String format(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return simpleDateFormat.format(new Date(time));
    }
}
