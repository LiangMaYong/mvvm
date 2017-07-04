package com.liangmayong.mvvm.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiangMaYong on 2017/7/4.
 */
public abstract class ViewFragment<Model extends ViewModel> extends Fragment implements ViewInterface<Model> {

    private FrameLayout rootLayout = null;
    private Model model = null;
    private Map<Integer, View> viewMap = new HashMap<>();

    public Model getViewModel() {
        return model;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModel.createModel(this);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = new FrameLayout(container.getContext());
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getViewModel() != null) {
            getViewModel().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getViewModel() != null) {
            getViewModel().onPause();
        }
    }

    @Override
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

    private void setContentView(View view) {
        if (view.getParent() != null) {
            rootLayout.removeAllViews();
            rootLayout.addView(view);
        }
    }
}