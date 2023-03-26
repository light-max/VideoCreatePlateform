package com.lifengqiang.video.ui.message;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.base.pm.BaseView;

public class MessageView extends BaseView<MessageActivity> {
    private RecyclerView recyclerView;
    private final MessageListAdapter adapter = new MessageListAdapter();
    private EditText input;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        recyclerView = get(R.id.recycler);
        recyclerView.setAdapter(adapter);
        input = get(R.id.input);
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                InputMethodManager inputMethodManager = (InputMethodManager) input
                        .getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                getPresenter().sendMessage(input.getText().toString());
                input.setText("");
            }
            return false;
        });
    }

    public MessageListAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void scrollLastMessage() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}
