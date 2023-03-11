package com.lifengqiang.video.data.observer;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.lifengqiang.video.data.result.User;

public class UserDetailsData extends MutableLiveData<User> {
    private static UserDetailsData instance;

    public static UserDetailsData getInstance() {
        if (instance == null) {
            instance = new UserDetailsData();
        }
        return instance;
    }

    public static void post(User user) {
        getInstance().postValue(user);
    }

    public static void ob(LifecycleOwner owner, Observer<User> observer) {
        getInstance().observe(owner, observer);
    }
}
