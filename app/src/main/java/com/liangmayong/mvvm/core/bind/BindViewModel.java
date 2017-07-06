package com.liangmayong.mvvm.core.bind;

import android.view.View;

/**
 * Created by LiangMaYong on 2017/7/5.
 */
public class BindViewModel extends BaseBindModel {

    public int visibility = View.VISIBLE;
    public int backgroundColor = 0x00ffffff;

    public BindViewModel(View view) {
        super(view);
        this.visibility = view.getVisibility();
    }

    public void onChanged(View view) {
        setVisibility(view, visibility);
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
