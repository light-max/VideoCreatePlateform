package com.lifengqiang.video.ui.remark.remark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.pm.BaseView;
import com.lifengqiang.video.data.result.Remark;
import com.lifengqiang.video.ui.remark.reply.ReplyActivity;
import com.lifengqiang.video.ui.uspace.UserSpaceActivity;

public class RemarkView extends BaseView<RemarkActivity> implements RemarkAdapter.Callback {
    private EditText input;
    private RecyclerView recyclerView;
    private RemarkAdapter adapter = new RemarkAdapter(this);

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        input = get(R.id.input);
        recyclerView = get(R.id.recycler);
        recyclerView.setAdapter(adapter);
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                InputMethodManager inputMethodManager = (InputMethodManager) input
                        .getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                getPresenter().sendRemark(getInputString());
            }
            return false;
        });
    }

    public void clearInputText() {
        input.setText("");
    }

    public String getInputString() {
        return input.getText().toString();
    }

    public RemarkAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onOpenUserSpace(Remark data, int position) {
        Intent intent = new Intent(getContext(), UserSpaceActivity.class);
        intent.putExtra(UserSpaceActivity.USER_ID, data.getUserId());
        getContext().startActivity(intent);
    }

    @Override
    public void onReply(Remark data, int position) {
        View view = View.inflate(getContext(), R.layout.view_reply_input_view, null);
        EditText input = view.findViewById(R.id.input);
        Button send = view.findViewById(R.id.send);
        input.setHint("回复@" + data.getUserNickname());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .show();
        send.setOnClickListener(v -> {
            Api.sendReply(data.getId(), input.getText().toString(), 0)
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        data.setReplyCount(data.getReplyCount() + 1);
                        adapter.notifyItemChanged(position);
                        toast("发送成功");
                        dialog.dismiss();
                        InputMethodManager inputMethodManager = (InputMethodManager) input
                                .getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }).run();

        });
    }

    @Override
    public void onLike(Remark data, int position) {
        Api.likeRemark(data.getId()).success(state -> {
            data.setLikeCount(state.getCount());
            data.setLike(state.isLike());
            adapter.notifyItemChanged(position);
        }).run();
    }

    @Override
    public void onOpenReply(Remark data, int position) {
        Intent intent = new Intent(getContext(), ReplyActivity.class);
        intent.putExtra(ReplyActivity.REMARK_ID, data.getId());
        getPresenter().startActivity(intent);
    }
}
