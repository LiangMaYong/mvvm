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
public abstract class ViewActivity<Model extends ViewModel> extends AppCompatActivity implements ViewInterface<Model> {

    private Model model = null;
    private Map<Integer, View> viewMap = new HashMap<>();

    public Model getViewModel() {
        return model;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModel.createModel(this);
    }

    public void notifyDataSetChanged() {
        if (getViewModel() != null) {
            int viewType = getViewModel().viewType;
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
        if (getViewModel() != null) {
            getViewModel().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getViewModel() != null) {
            getViewModel().onPause();
        }
    }

}
