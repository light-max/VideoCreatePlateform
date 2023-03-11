package com.lifengqiang.video.async;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class Async implements RunToMainThread {
    private static final boolean debug = true;

    private Thread mThread;

    private final List<Call.OnBefore> before = new ArrayList<>();
    private final List<Call.OnReturnData> success = new ArrayList<>();
    private final List<Call.OnSuccess> success2 = new ArrayList<>();
    private final List<Call.OnError> error = new ArrayList<>();
    private final List<Call.OnAfter> after = new ArrayList<>();

    private AsyncTaskRunnable runnable;
    private Runnable runnable2;

    private boolean useMainHandler = true;

    public void go() {
        runToMainThread(() -> {
            for (Call.OnBefore onBefore : before) {
                onBefore.run();
            }
        });
        mThread = new Thread(() -> {
            try {
                Object result = runnable == null ? null : runnable.run();
                if (runnable2 != null) runnable2.run();
                if (result instanceof AsyncTaskError) {
                    runToMainThread(() -> {
                        AsyncTaskError e = (AsyncTaskError) result;
                        if (debug) {
                            if (e.getException() != null) {
                                e.getException().printStackTrace();
                            }
                        }
                        for (Call.OnError onError : error) {
                            onError.onError(e.getMessage(), e.getException());
                        }
                    });
                } else {
                    runToMainThread(() -> {
                        for (Call.OnReturnData onReturnData : success) {
                            onReturnData.onSuccess(result);
                        }
                    });
                    runToMainThread(() -> {
                        for (Call.OnSuccess onSuccess : success2) {
                            onSuccess.run();
                        }
                    });
                }
            } catch (Exception e) {
                if (debug) {
                    e.printStackTrace();
                }
                runToMainThread(() -> {
                    for (Call.OnError onError : error) {
                        onError.onError(null, e);
                    }
                });
            }
            runToMainThread(() -> {
                for (Call.OnAfter onAfter : after) {
                    onAfter.run();
                }
            });
        }, this.toString());
        mThread.start();
    }

    @Override
    public void runToMainThread(Runnable runnable) {
        if (runnable != null) {
            if (useMainHandler && Looper.getMainLooper() != Looper.myLooper()) {
                new Handler(Looper.getMainLooper()).post(runnable);
            } else {
                runnable.run();
            }
        }
    }

    public void join() {
        try {
            mThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Async async = new Async();

        public Builder<T> before(Call.OnBefore before) {
            async.before.add(before);
            return this;
        }

        public Builder<T> success(Call.OnReturnData<T> success) {
            async.success.add(success);
            return this;
        }

        public Builder<T> success(Call.OnSuccess success) {
            async.success2.add(success);
            return this;
        }

        public Builder<T> error(Call.OnError error) {
            async.error.add(error);
            return this;
        }

        public Builder<T> after(Call.OnAfter after) {
            async.after.add(after);
            return this;
        }

        public Builder<T> task(AsyncTaskRunnable task) {
            async.runnable = task;
            return this;
        }

        public Builder<T> task(Runnable runnable) {
            async.runnable2 = runnable;
            return this;
        }

        /**
         * 为了解决{@link #join()}无法等待 success(data)回调执行的问题而产生的解决方法
         *
         * @param flag true:异步回调接口在主线程中运行, false:异步回调接口在任务线程中运行<b>默认值为true</b>
         */
        public Builder<T> useMainHandler(boolean flag) {
            async.useMainHandler = flag;
            return this;
        }

        public Async build() {
            return async;
        }

        public void run() {
            async.go();
        }

        public void run(AsyncTaskRunnable task) {
            async.runnable = task;
            async.go();
        }
    }
}
