package com.lifengqiang.video.ui.main.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lifengqiang.video.R;
import com.lifengqiang.video.ui.main.BaseMainPageChildView;
import com.lifengqiang.video.ui.main.CreateModelDialog;
import com.lifengqiang.video.ui.message.MessageActivity;
import com.lifengqiang.video.ui.search.SearchActivity;

public class MessageView extends BaseMainPageChildView<MessageFragment> {
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerView;
    private TextView empty;
    private final MessageListAdapter adapter = new MessageListAdapter();

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        click(R.id.search, () -> SearchActivity.start(getContext()));
        click(R.id.create_model, (v) -> CreateModelDialog.show(getActivity(), v));
        swipe = get(R.id.swipe);
        recyclerView = get(R.id.recycler);
        empty = get(R.id.empty);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra(MessageActivity.USER_ID, data.getTargetId());
            intent.putExtra(MessageActivity.USER_HEAD, data.getUserhead());
            intent.putExtra(MessageActivity.USER_NICKNAME, data.getNickname());
            getContext().startActivity(intent);
        });
    }

    public void setShowContent(boolean isShow) {
        recyclerView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        empty.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    public SwipeRefreshLayout getSwipe() {
        return swipe;
    }

    public MessageListAdapter getAdapter() {
        return adapter;
    }
}
