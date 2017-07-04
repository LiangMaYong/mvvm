package com.liangmayong.mvvm.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiangMaYong on 2017/6/30.
 */
public abstract class ViewModelActivity<Model extends ViewModel> extends AppCompatActivity implements ViewModelInterface<Model> {

    private Model model = null;
    private Map<Integer, View> viewMap = new HashMap<>();

    public Model getViewModel() {
        return model;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModel.createModel(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if (model != null) {
            int viewType = model.viewType;
            View view = null;
            if (viewMap.containsKey(viewType)) {
                view = viewMap.get(viewType);
            }
            if (view == null) {
                view = onCreateView(viewType);
                viewMap.put(viewType, view);
            }
            if (view != null && view.getParent() == null) {
                setContentView(view);
            }
            if (view != null) {
                onUpdateView(viewType, view);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getViewModel().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getViewModel().onPause();
    }

}
