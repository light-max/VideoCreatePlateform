package com.lifengqiang.video.ui.search.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.ui.search.SearchFragment;
import com.lifengqiang.video.ui.works.WorksPlayerActivity;

public class SearchVideoFragment extends SearchFragment<SearchVideoView> {
    private final WorksListAdapter adapter = new WorksListAdapter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView().setAdapter(adapter);
        searchValue.observe(this, this::loadNewData);
        adapter.setOnItemClickListener((data, position) -> {
            Intent intent = new Intent(requireContext(), WorksPlayerActivity.class);
            intent.putExtra(WorksPlayerActivity.WORKS_ID, data.getId());
            startActivity(intent);
        });
    }

    private void loadNewData(String s) {
        Api.searchWorks(s).success(data -> {
            setShowContent(!data.isEmpty());
            if (data.isEmpty()) {
                getEmptyView().setText("搜索内容为空");
            } else {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }).run();
    }
}
