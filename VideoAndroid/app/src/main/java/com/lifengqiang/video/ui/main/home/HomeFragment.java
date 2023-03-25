package com.lifengqiang.video.ui.main.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.lifengqiang.video.R;
import com.lifengqiang.video.api.Api;
import com.lifengqiang.video.base.fragment.PresenterFragment;
import com.lifengqiang.video.data.result.Works;
import com.lifengqiang.video.ui.main.home.view.VerticalLooperVideoContainer;
import com.lifengqiang.video.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends PresenterFragment<HomeView> implements VerticalLooperVideoContainer.DataSource {
    private final MutableLiveData<Object> initDataNotifier = new MutableLiveData<>();
    private final List<Integer> ids = new ArrayList<>();
    private int index = -1;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    private boolean random = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNewData();
        if (backgroundThread != null) {
            backgroundThread.quit();
        }
        backgroundThread = new HandlerThread("HomeWorksLoadThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click(R.id.search, () -> SearchActivity.start(requireContext()));
        getIView().getVideoContainer().setDataSource(this);
        initDataNotifier.observe(this, o -> {
            getIView().getVideoContainer().loadVideo();
        });
        click(R.id.follow, () -> {
            index = -1;
            ids.clear();
            random = false;
            getNewData();
            TextView follow = get(R.id.follow);
            TextView recommend = get(R.id.recommend);
            follow.setTextColor(requireActivity().getColor(R.color.white));
            recommend.setTextColor(requireActivity().getColor(R.color.gray_bbb));
        });
        click(R.id.recommend, () -> {
            Api.resetRandomWorksIds().success(() -> {
                index = -1;
                ids.clear();
                random = true;
                getNewData();
            }).run();
            TextView follow = get(R.id.follow);
            TextView recommend = get(R.id.recommend);
            follow.setTextColor(requireActivity().getColor(R.color.gray_bbb));
            recommend.setTextColor(requireActivity().getColor(R.color.white));
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            getIView().onPause();
        } else {
            getIView().onResume();
        }
    }

    public void getNewData() {
        if (random) {
            Api.getRandomWorksIds().success(data -> {
                if (index < 0) {
                    initDataNotifier.postValue(new Object());
                }
                index = Math.max(index, 0);
                ids.addAll(data);
            }).run();
        } else {
            Api.getFollowWorksIds().success(data -> {
                index = 0;
                ids.clear();
                ids.addAll(data);
                getIView().getVideoContainer().loadVideo();
            }).run();
        }
    }

    @Override
    public boolean hasPrevious() {
        return index - 1 >= 0;
    }

    @Override
    public boolean hasNext() {
        boolean flag = index + 1 < ids.size();
        if (!flag) getNewData();
        return flag;
    }

    @Override
    public boolean hasCurrent() {
        return index >= 0 && index < ids.size();
    }

    @Override
    public int getPreviousId() {
        if (index >= ids.size()) {
            return ids.size() - 1;
        }
        return ids.get(index - 1);
    }

    @Override
    public int getCurrentId() {
        return ids.get(index);
    }

    @Override
    public int getNextId() {
        return ids.get(index + 1);
    }

    @Override
    public void getWorks(int id, VerticalLooperVideoContainer.OnWorksCall call) {
        Api.getWorks(id).success(call::onCall).run();
    }

    @Override
    public void move(boolean next) {
        index += next ? 1 : -1;
    }

    @Override
    public Handler getBackgroundHandler() {
        return backgroundHandler;
    }

    @Override
    public Handler getMainHandler() {
        return mainHandler;
    }

    @Override
    public void resetData() {
        if (random) {
            Api.resetRandomWorksIds().success(() -> {
                index = -1;
                ids.clear();
                getNewData();
            }).run();
        } else {
            toast("没有内容了");
        }
    }

    @Override
    public void loadWorksInfo(Works works) {
        getIView().setWorks(works);
    }
}
