package com.liangmayong.mvvm.core.bind;

import android.os.Handler;
import android.view.View;

import com.liangmayong.mvvm.core.ViewWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LiangMaYong on 2017/7/6.
 */
public class BaseBindModel {

    private View view = null;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler();
    private ViewWatch viewWatch = null;
    private boolean isChanged = false;
    private boolean isRunning = false;
    private boolean isWatching = false;

    public BaseBindModel(View view) {
        this.view = view;
        this.viewWatch = new ViewWatch(this, new ViewWatch.OnWatch() {
            @Override
            public void onChanged() {
                isChanged = true;
            }
        });
    }

    public void onResume() {
        if (!isRunning) {
            isRunning = true;
            notifyDataSetChanged();
        }
    }

    public void onPause() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void onChanged(View view) {
    }


    private void notifyDataSetChanged() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isWatching) {
                    isWatching = true;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewWatch.watch();
                            isWatching = false;
                        }
                    });
                }
                if (isChanged) {
                    isChanged = false;
                    if (view != null) {
                        onChanged(view);
                    }
                }
                if (isRunning) {
                    handler.postDelayed(this, 20);
                }
            }
        }, 20);
    }

}
