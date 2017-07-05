package com.liangmayong.mvvm.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

/**
 * Created by LiangMaYong on 2017/6/30.
 */
public abstract class ViewActivity<Model extends ViewModel> extends AppCompatActivity implements ViewInterface<Model> {

    private Model model = null;
    private SparseArray<ViewHolder> viewSparseArray = new SparseArray<>();

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
            ViewHolder viewHolder = getViewHolder();
            viewHolder.onChanged();
        }
    }

    public ViewHolder getViewHolder() {
        if (getViewModel() != null) {
            int viewType = getViewModel().viewType;
            ViewHolder view = viewSparseArray.get(viewType);
            if (view == null) {
                view = onCreateViewHolder(viewType);
                viewSparseArray.put(viewType, view);
            }
            if (view != null && view.getView() != null && view.getView().getParent() == null) {
                setContentView(view.getView());
            }
            return view;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getViewHolder() != null) {
            getViewHolder().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getViewHolder() != null) {
            getViewHolder().onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewSparseArray.clear();
    }
}
