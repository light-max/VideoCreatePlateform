package com.lifengqiang.video.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageTimeUtils {
    @SuppressLint("SimpleDateFormat")
    public static String getFuzzyTime(long time) {
        System.out.println(new SimpleDateFormat("dd HH:mm").format(new Date(time)));
        long millis = System.currentTimeMillis();
        int[] day = new int[]{
                (int) (millis / 1000 / 60 / 60 / 24),
                (int) (time / 1000 / 60 / 60 / 24),
        };
        long diffDay = day[0] - day[1];
        if (diffDay == 0) {
            return new SimpleDateFormat("HH:mm").format(new Date(time));
        } else if (diffDay == 1) {
            return new SimpleDateFormat("昨天 HH:mm").format(new Date(time));
        } else if (diffDay == 2) {
            return new SimpleDateFormat("前天 HH:mm").format(new Date(time));
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
        }
    }
}
