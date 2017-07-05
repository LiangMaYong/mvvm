package com.liangmayong.mvvm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liangmayong.mvvm.core.ViewActivity;
import com.liangmayong.mvvm.core.ViewHolder;
import com.liangmayong.mvvm.core.bind.BindButtonModel;
import com.liangmayong.mvvm.core.bind.BindRelativeLayoutModel;
import com.liangmayong.mvvm.core.bind.BindTextViewModel;

public class MainActivity extends ViewActivity<MainViewModel> {
    ActivityMainViewHolder viewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().user.text.text = "你好";
        getViewModel().user.age = "25";
        getViewModel().buttonText = "点击更新文本";
    }

    @Override
    public ViewHolder onCreateViewHolder(int viewType) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        return new ActivityMainViewHolder(view);
    }

    private class ActivityMainViewHolder implements ViewHolder {

        // ActivityMainViewHolder create by activity_main.xml

        public View view;
        public ActivityMainViewModel model;
        public TextView text;
        public Button button;
        public RelativeLayout activity_main;

        public ActivityMainViewHolder(Activity activity) {
            this(activity.getWindow().getDecorView());
        }

        public ActivityMainViewHolder(View view) {
            this.view = view;
            this.text = (TextView) view.findViewById(R.id.text);
            this.button = (Button) view.findViewById(R.id.button);
            this.activity_main = (RelativeLayout) view.findViewById(R.id.activity_main);
            this.model = new ActivityMainViewModel(this);
        }

        @Override
        public View getView() {
            return view;
        }

        @Override
        public void onChanged() {
            this.model.bind();
        }

        @Override
        public void onResume() {
            this.model.resume();
        }

        @Override
        public void onPause() {
            this.model.pause();
        }

    }

    public class ActivityMainViewModel {

        // ActivityMainViewModel create by activity_main.xml

        public ActivityMainViewHolder holder;
        public BindTextViewModel text;
        public BindButtonModel button;
        public BindRelativeLayoutModel activity_main;

        public ActivityMainViewModel(ActivityMainViewHolder holder) {
            this.holder = holder;
            this.text = new BindTextViewModel(holder.text);
            this.button = new BindButtonModel(holder.button);
            this.activity_main = new BindRelativeLayoutModel(holder.activity_main);
        }

        void bind() {
            this.text.bindData(holder.text);
            this.button.bindData(holder.button);
            this.activity_main.bindData(holder.activity_main);
        }

        void resume() {
            this.text.onResume();
            this.button.onResume();
            this.activity_main.onResume();
        }

        void pause() {
            this.text.onPause();
            this.button.onPause();
            this.activity_main.onPause();
        }

    }
}
