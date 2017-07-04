package com.liangmayong.mvvm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liangmayong.mvvm.core.ViewActivity;

public class MainActivity extends ViewActivity<MainViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().user.text.text = "你好";
        getViewModel().user.age = "25";
        getViewModel().buttonText = "点击更新文本";
    }

    @Override
    public View onCreateView(int viewType) {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null);
    }

    private int index = 0;

    @Override
    public void onUpdateView(int viewType, View view) {
        ViewHolder2 viewHolder = new ViewHolder2(view);
        viewHolder.text.setText(getViewModel().user.text.text + "\n" + getViewModel().user.age);
        viewHolder.button.setText(getViewModel().buttonText);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                getViewModel().user.text.text = "onClick -> " + index;
                getViewModel().user.age = index + "";
                getViewModel().viewType = 1;
                getViewModel().buttonText = "onClick -> " + index;
            }
        });
    }

    public class ViewHolder2 {
        public View rootView;
        public TextView text;
        public Button button;
        public RelativeLayout activity_main;

        public ViewHolder2(View rootView) {
            this.rootView = rootView;
            this.text = (TextView) rootView.findViewById(R.id.text);
            this.button = (Button) rootView.findViewById(R.id.button);
            this.activity_main = (RelativeLayout) rootView.findViewById(R.id.activity_main);
        }

    }
}
