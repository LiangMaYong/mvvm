package com.liangmayong.mvvm.core;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by LiangMaYong on 2017/6/30.
 */
public abstract class ViewActivity extends AppCompatActivity {

    private ViewHolder viewHolder = null;

    protected void bindViewHolder(ViewHolder viewHolder) {
        if (this.viewHolder != null) {
            this.viewHolder.onPause();
        }
        this.viewHolder = viewHolder;
        if (this.viewHolder != null) {
            if (this.viewHolder.getView() != null && this.viewHolder.getView().getParent() == null) {
                setContentView(this.viewHolder.getView());
            }
            this.viewHolder.onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewHolder != null) {
            viewHolder.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewHolder != null) {
            viewHolder.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }

}
