package com.liangmayong.mvvm.core;

import android.view.View;

/**
 * Created by LiangMaYong on 2017/7/5.
 */

public interface ViewModel {

    View getView();

    void resume();

    void pause();

}
