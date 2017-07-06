package com.liangmayong.mvvm;

import android.content.Context;
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

public class MainActivity extends ViewActivity {

    ActivityMainViewHolder viewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHolder = new ActivityMainViewHolder(this, R.layout.activity_main);
        bindViewHolder(viewHolder);
        viewHolder.viewModel.text.text = "123";
    }

    public class ActivityMainViewHolder implements ViewHolder, View.OnClickListener {

        // ActivityMainViewHolder create by activity_main.xml

        public View view;
        public ActivityMainViewModel viewModel;
        public TextView text;
        public Button button;
        public RelativeLayout activity_main;

        public ActivityMainViewHolder(Context context, int layoutId) {
            this(LayoutInflater.from(context).inflate(layoutId, null));
        }

        public ActivityMainViewHolder(View view) {
            this.view = view;
            this.text = (TextView) view.findViewById(R.id.text);
            this.button = (Button) view.findViewById(R.id.button);
            this.button.setOnClickListener(this);
            this.activity_main = (RelativeLayout) view.findViewById(R.id.activity_main);
            this.viewModel = new ActivityMainViewModel(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    viewModel.button.text = "点击了按钮";
                    break;
            }
        }

        @Override
        public void onResume() {
            this.viewModel.resume();
        }

        @Override
        public void onPause() {
            this.viewModel.pause();
        }

        @Override
        public View getView() {
            return view;
        }

        public class ActivityMainViewModel {

            // ActivityMainViewModel create by activity_main.xml

            public ActivityMainViewHolder viewHolder;
            public BindTextViewModel text;
            public BindButtonModel button;
            public BindRelativeLayoutModel activity_main;

            public ActivityMainViewModel(ActivityMainViewHolder viewHolder) {
                this.viewHolder = viewHolder;
                this.text = new BindTextViewModel(viewHolder.text);
                this.button = new BindButtonModel(viewHolder.button);
                this.activity_main = new BindRelativeLayoutModel(viewHolder.activity_main);
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
}
