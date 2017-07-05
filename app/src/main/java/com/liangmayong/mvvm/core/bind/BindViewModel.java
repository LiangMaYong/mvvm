package com.liangmayong.mvvm.core.bind;

import android.view.View;

/**
 * Created by LiangMaYong on 2017/7/5.
 */
public class BindViewModel {

    private View view = null;
    public int visibility = View.VISIBLE;
    public int backgroundColor = 0x00ffffff;

    public BindViewModel(View view) {
        this.view = view;
        this.visibility = view.getVisibility();
    }

    public void bindData(View view) {
        setVisibility(view, visibility);
    }

    public void onResume() {
    }

    public void onPause() {
    }

    private void setVisibility(View view, int visibility) {
        if (visibility == View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        } else if (visibility == View.GONE) {
            view.setVisibility(View.GONE);
        } else if (visibility == View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
