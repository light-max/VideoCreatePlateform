package com.lifengqiang.video.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.fragment.PresenterFragment;

public class SearchFragment<V extends SearchFragmentView<?>> extends PresenterFragment<V> {
    protected MutableLiveData<String> searchValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SearchActivity activity = (SearchActivity) context;
        searchValue = activity.getSearchValue();
    }

    public RecyclerView getRecyclerView() {
        return get(R.id.recycler);
    }

    public TextView getEmptyView() {
        return get(R.id.empty);
    }

    public void setShowContent(boolean isShow) {
        getRecyclerView().setVisibility(isShow ? View.VISIBLE : View.GONE);
        getEmptyView().setVisibility(isShow ? View.GONE : View.VISIBLE);
    }
}
