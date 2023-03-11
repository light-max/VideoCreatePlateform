package com.lifengqiang.video.utils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequests {
    private static RequestOptions SKIP_CACHE;

    public static RequestOptions skipCache() {
        if (SKIP_CACHE == null) {
            SKIP_CACHE = new RequestOptions().skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        return SKIP_CACHE;
    }
}
