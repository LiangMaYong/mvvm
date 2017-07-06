package com.liangmayong.mvvm.core.bind;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by LiangMaYong on 2017/7/5.
 */
public class BindViewModel extends BaseBindModel {

    public int visibility = View.VISIBLE;
    public Drawable background = null;

    public BindViewModel(View view) {
        super(view);
        this.visibility = view.getVisibility();
        this.background = view.getBackground();
    }

    public void onChanged(View view) {
        setVisibility(view, visibility);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
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
