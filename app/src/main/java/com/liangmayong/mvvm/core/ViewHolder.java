package com.liangmayong.mvvm.core;

import android.view.View;

/**
 * Created by LiangMaYong on 2017/7/5.
 */

public interface ViewHolder {

    View getView();

    void onResume();

    void onPause();
}
