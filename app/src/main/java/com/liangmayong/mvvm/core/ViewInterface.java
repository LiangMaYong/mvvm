package com.liangmayong.mvvm.core;

import android.view.View;

/**
 * Created by LiangMaYong on 2017/6/30.
 */
public interface ViewInterface<Model extends ViewModel> {

    void notifyDataSetChanged();

    ViewHolder onCreateViewHolder(int viewType);

    Model getViewModel();

}
