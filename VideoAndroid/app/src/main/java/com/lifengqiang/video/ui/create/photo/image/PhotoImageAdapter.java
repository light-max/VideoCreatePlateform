package com.lifengqiang.video.ui.create.photo.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lifengqiang.video.R;
import com.lifengqiang.video.base.recycler.SimpleSingleItemRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotoImageAdapter extends SimpleSingleItemRecyclerAdapter<File> {
    private final Map<File, Object> map = new HashMap<>();

    @Override
    protected int getItemViewLayout() {
        return R.layout.view_image_cover;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, File data, int position) {
        ImageView icon = holder.getImage(R.id.icon);
        ImageView check = holder.getImage(R.id.check);
        Glide.with(holder.itemView)
                .load(data)
                .override(600)
                .centerCrop()
                .into(icon);
        check.setImageResource(map.containsKey(data) ?
                R.drawable.ic_baseline_radio_button_checked_24 :
                R.drawable.ic_baseline_radio_button_unchecked_24);
        holder.click(icon, () -> {
            if (map.containsKey(data)) {
                map.remove(data);
            } else {
                map.put(data, new Object());
            }
            check.setImageResource(map.containsKey(data) ?
                    R.drawable.ic_baseline_radio_button_checked_24 :
                    R.drawable.ic_baseline_radio_button_unchecked_24);
        });
    }

    public ArrayList<File> getSelectedFiles() {
        // keySet是无序的
        ArrayList<File> list = new ArrayList<>();
        for (File file : getData()) {
            if (map.containsKey(file)) {
                list.add(file);
            }
        }
        return list;
    }
}
