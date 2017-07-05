package com.liangmayong.mvvm.core.bind;

import android.view.View;
import android.widget.TextView;

/**
 * Created by LiangMaYong on 2017/7/5.
 */

public class BindTextViewModel extends BindViewModel {

    public String text = "";

    public BindTextViewModel(TextView textView) {
        super(textView);
        if (textView != null){
            text = textView.getText().toString();
        }
    }

    @Override
    public void bindData(View view) {
        super.bindData(view);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setText(text);
        }
    }
}
