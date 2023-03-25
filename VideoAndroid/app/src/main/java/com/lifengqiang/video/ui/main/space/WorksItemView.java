package com.lifengqiang.video.ui.main.space;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.api.ExRequestBuilder;
import com.lifengqiang.video.base.call.ViewGet;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.view.MaskImageView;

import java.util.List;

public class WorksItemView extends FrameLayout implements ViewGet {
    public WorksItemView(@NonNull Context context) {
        this(context, null);
    }

    public WorksItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_item_space_works, this);
    }

    public void setWorks(Works works) {
        get(R.id.image).setVisibility(works.getType() == 0 ? VISIBLE : GONE);
        get(R.id.video).setVisibility(works.getType() == 0 ? GONE : VISIBLE);
        if (works.getType() == 0) {
            TextView content = get(R.id.content_image);
            TextView count = get(R.id.image_count);
            ImageView[] images = new ImageView[]{
                    get(R.id.image0),
                    get(R.id.image1),
                    get(R.id.image2),
            };
            content.setText(works.getContent());
            List<String> urls = works.getImages();
            count.setText(String.valueOf(urls.size()));
            for (int i = 0; i < urls.size() && i < images.length; i++) {
                Glide.with(this)
                        .load(ExRequestBuilder.getUrl(urls.get(i)))
                        .into(images[i]);
            }
        } else {
            TextView content = get(R.id.content_video);
            MaskImageView maskImageView  =get(R.id.video_cover_bg);
            ImageView image = get(R.id.video_cover);
            content.setText(works.getContent());
            Glide.with(this)
                    .load(ExRequestBuilder.getUrl(works.getCover()))
                    .into(image);
            Glide.with(this)
                    .load(ExRequestBuilder.getUrl(works.getCover()))
                    .apply(maskImageView.setGaussBlur())
                    .into(maskImageView);
        }
    }

    public Bitmap setGaussblur(int radius, Bitmap source) {
        RenderScript renderScript = RenderScript.create(getContext());
        final Allocation input = Allocation.createFromBitmap(renderScript, source);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur;
        scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(source);
        renderScript.destroy();
        return source;
    }

    @Override
    public <T extends View> T get(int viewId) {
        return findViewById(viewId);
    }
}
