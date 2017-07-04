package com.liangmayong.mvvm.core;

import android.os.Handler;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LiangMaYong on 2017/7/4.
 */
public class ViewModel {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private boolean isChanged = false;
    private boolean isRunning = false;
    private boolean isWatching = false;
    private Handler handler = new Handler();
    private ViewWatch viewWatch = null;

    private final ViewInterface viewModelInterface;
    public int viewType = 0;

    public ViewModel(ViewInterface viewModelInterface) {
        this.viewModelInterface = viewModelInterface;
        viewWatch = new ViewWatch(this, new ViewWatch.OnWatch() {
            @Override
            public void onChanged() {
                changed();
            }
        });
        notifyDataSetChanged();
    }

    void onResume() {
        if (!isRunning) {
            isRunning = true;
            notifyDataSetChanged();
        }
    }

    void onPause() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void changed() {
        this.isChanged = true;
    }

    private void notifyDataSetChanged() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isWatching) {
                    isWatching = true;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewWatch.watch();
                            isWatching = false;
                        }
                    });
                }
                if (isChanged) {
                    isChanged = false;
                    if (viewModelInterface != null) {
                        viewModelInterface.notifyDataSetChanged();
                    }
                }
                if (isRunning) {
                    handler.postDelayed(this, 20);
                }
            }
        }, 20);
    }

    static <T extends ViewModel> T createModel(ViewInterface<T> object) {
        try {
            Class<?> modelClass = getModelClass(object);
            Constructor constructor = modelClass.getConstructor(ViewInterface.class);
            return (T) constructor.newInstance(object);
        } catch (Exception e) {
        }
        return null;
    }

    //////////////////////////////////////////////////////
    ////////// Private Static
    //////////////////////////////////////////////////////

    private static Class<?> getModelClass(Object object) {
        Type type = null;
        Type superType = object.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) superType).getActualTypeArguments();
            type = types[0];
        }
        return getRawType(type);
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
            return null;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            return null;
        }
    }

}
