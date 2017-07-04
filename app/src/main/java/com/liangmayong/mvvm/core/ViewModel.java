package com.liangmayong.mvvm.core;

import android.os.Handler;
import android.util.Pair;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LiangMaYong on 2017/7/4.
 */
public class ViewModel {

    private static String[] baseFields = {"viewType"};

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Map<Object, Boolean> objectChangedMap = new HashMap<>();

    private boolean isRun = false;
    private boolean checking = false;
    private Handler handler = new Handler();
    private ViewModelInterface viewModelInterface;
    public int viewType = 0;

    public ViewModel(ViewModelInterface viewModelInterface) {
        this.viewModelInterface = viewModelInterface;
        notifyDataSetChanged();
    }

    void onResume() {
        isRun = true;
        notifyDataSetChanged();
    }

    void onPause() {
        isRun = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void notifyDataSetChanged() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isChanged(ViewModel.this)) {
                    objectChangedMap.put(ViewModel.this, false);
                    viewModelInterface.notifyDataSetChanged();
                }
                if (isRun) {
                    handler.postDelayed(this, 20);
                }
            }
        }, 20);
    }

    private boolean isChanged(final Object object) {
        if (!isRun) {
            return false;
        }
        if (!checking) {
            checking = true;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    checkValueChanged(object);
                    checking = false;
                }
            });
        }
        if (objectChangedMap.containsKey(object)) {
            return objectChangedMap.get(object);
        }
        return false;
    }

    private Map<Object, Boolean> objectInitMap = new HashMap<>();
    private Map<Object, List<Field>> objectFieldMap = new HashMap<>();
    private Map<Object, List<Object>> objectValueMap = new HashMap<>();

    private void checkValueChanged(Object object) {
        if (!objectInitMap.containsKey(object) || !objectInitMap.get(object)) {
            Pair<List<Field>, List<Object>> pair = getModelFields(object);
            objectFieldMap.put(object, pair.first);
            objectValueMap.put(object, pair.second);
            objectInitMap.put(object, true);
            objectChangedMap.put(object, true);
        } else {
            List<Field> fieldList = objectFieldMap.get(object);
            List<Object> valueList = objectValueMap.get(object);
            boolean isChanged = false;
            for (int i = 0; i < fieldList.size(); i++) {
                boolean flag;
                Object newValue = null;
                Object oldValue = null;
                try {
                    newValue = fieldList.get(i).get(object);
                } catch (Exception e) {
                }
                try {
                    oldValue = valueList.get(i);
                } catch (Exception e) {
                }
                if (newValue == null && oldValue == null) {
                    flag = false;
                } else {
                    if (newValue != null && newValue.equals(oldValue)) {
                        flag = isValueChanged(newValue, 2);
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    valueList.remove(i);
                    valueList.add(i, newValue);
                }
                isChanged = isChanged || flag;
            }
            objectChangedMap.put(object, isChanged);
        }
    }

    private boolean isValueChanged(Object object, int level) {
        if (!objectInitMap.containsKey(object) || !objectInitMap.get(object)) {
            Pair<List<Field>, List<Object>> pair = getModelFields(object);
            objectFieldMap.put(object, pair.first);
            objectValueMap.put(object, pair.second);
            objectInitMap.put(object, true);
            return true;
        } else {
            List<Field> fieldList = objectFieldMap.get(object);
            List<Object> valueList = objectValueMap.get(object);
            boolean isChanged = false;
            for (int i = 0; i < fieldList.size(); i++) {
                boolean flag;
                Object newValue = null;
                Object oldValue = null;
                try {
                    newValue = fieldList.get(i).get(object);
                } catch (Exception e) {
                }
                try {
                    oldValue = valueList.get(i);
                } catch (Exception e) {
                }
                if (newValue == null && oldValue == null) {
                    flag = false;
                } else {
                    if (newValue != null && newValue.equals(oldValue)) {
                        if (level > 0) {
                            flag = isValueChanged(newValue, level - 1);
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    valueList.remove(i);
                    valueList.add(i, newValue);
                }
                isChanged = isChanged || flag;
            }
            return isChanged;
        }
    }


    public static <T extends ViewModel> T createModel(ViewModelInterface<T> object) {
        Class<?> modelClass = getModelClass(object);
        try {
            Constructor constructor = modelClass.getConstructor(ViewModelInterface.class);
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

    private static Map<Class, List<Field>> classListMap = new HashMap<>();

    private static Pair<List<Field>, List<Object>> getModelFields(Object object) {
        List<Field> fieldList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        if (object == null) {
            return Pair.create(fieldList, valueList);
        }
        if (classListMap.containsKey(object.getClass())) {
            fieldList = classListMap.get(object.getClass());
            for (int i = 0; i < fieldList.size(); i++) {
                try {
                    valueList.add(fieldList.get(i).get(object));
                } catch (Exception e) {
                    valueList.add(null);
                }
            }
            return Pair.create(fieldList, valueList);
        } else {
            Class clazz;
            for (clazz = object.getClass(); clazz != ViewModel.class && clazz != null; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fieldList.add(fields[i]);
                    try {
                        valueList.add(fields[i].get(object));
                    } catch (Exception e) {
                        valueList.add(null);
                    }
                }
            }
            if (clazz == ViewModel.class) {
                for (int i = 0; i < baseFields.length; i++) {
                    try {
                        Field field = clazz.getDeclaredField(baseFields[i]);
                        field.setAccessible(true);
                        fieldList.add(field);
                        valueList.add(field.get(object));
                    } catch (Exception e) {
                        valueList.add(null);
                    }
                }
            }
            classListMap.put(object.getClass(), fieldList);
            return Pair.create(fieldList, valueList);
        }
    }
}
