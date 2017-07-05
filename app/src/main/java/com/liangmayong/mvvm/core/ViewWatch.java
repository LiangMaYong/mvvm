package com.liangmayong.mvvm.core;

import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangMaYong on 2017/7/4.
 */
public class ViewWatch {

    private static String[] baseFields = {"viewType"};
    private static Map<Class, List<Class>> checkClassMap = new HashMap<>();

    private Map<Object, Boolean> objectInitMap = new HashMap<>();
    private Map<Object, List<Object>> objectValueMap = new HashMap<>();

    public interface OnWatch {
        void onChanged();
    }

    private Object object;
    private OnWatch onWatch;

    public ViewWatch(Object object, OnWatch watch) {
        this.object = object;
        this.onWatch = watch;
    }

    public void watch() {
        checkValueChanged(object);
    }

    private void checkValueChanged(Object object) {
        if (!objectInitMap.containsKey(object) || !objectInitMap.get(object)) {
            Pair<List<Field>, List<Object>> pair = getFields(object);
            objectFieldMap.put(object.getClass(), pair.first);
            objectValueMap.put(object, pair.second);
            objectInitMap.put(object, true);
            onWatch.onChanged();
        } else {
            List<Field> fieldList = objectFieldMap.get(object.getClass());
            List<Object> valueList = objectValueMap.get(object);
            for (int i = 0; i < fieldList.size(); i++) {
                boolean flag;
                Object newValue;
                Object oldValue;
                try {
                    newValue = fieldList.get(i).get(object);
                } catch (Exception e) {
                    newValue = null;
                }
                try {
                    oldValue = valueList.get(i);
                } catch (Exception e) {
                    oldValue = null;
                }
                if (newValue == null && oldValue == null) {
                    flag = false;
                } else {
                    if (newValue != null && newValue.equals(oldValue)) {
                        flag = isValueChanged(newValue, 1);
                    } else {
                        flag = true;
                    }
                }
                if (flag) {
                    valueList.remove(i);
                    valueList.add(i, newValue);
                    onWatch.onChanged();
                    return;
                }
            }
        }
    }

    private boolean isValueChanged(Object targetValue, int level) {
        if (!objectInitMap.containsKey(targetValue) || !objectInitMap.get(targetValue)) {
            Pair<List<Field>, List<Object>> pair = getFields(targetValue);
            objectFieldMap.put(targetValue.getClass(), pair.first);
            objectValueMap.put(targetValue, pair.second);
            objectInitMap.put(targetValue, true);
            return true;
        } else {
            List<Field> fieldList = objectFieldMap.get(targetValue.getClass());
            List<Object> valueList = objectValueMap.get(targetValue);
            for (int i = 0; i < fieldList.size(); i++) {
                boolean flag;
                Object newValue;
                Object oldValue;
                try {
                    newValue = fieldList.get(i).get(targetValue);
                } catch (Exception e) {
                    newValue = null;
                }
                try {
                    oldValue = valueList.get(i);
                } catch (Exception e) {
                    oldValue = null;
                }
                if (newValue == null && oldValue == null) {
                    flag = false;
                } else {
                    if (newValue != null && newValue.equals(oldValue)) {
                        if (level > 0) {
                            flag = isValueChanged(newValue, level - 1);
                        } else if (checkClassMap.containsKey(object.getClass())) {
                            List<Class> classes = checkClassMap.get(object.getClass());
                            if (classes.contains(newValue.getClass())) {
                                flag = isValueChanged(newValue, level - 1);
                            } else {
                                flag = false;
                            }
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
                    return true;
                }
            }
            return false;
        }
    }

    private static Map<Class, List<Field>> objectFieldMap = new HashMap<>();

    private static Pair<List<Field>, List<Object>> getFields(Object object) {
        List<Field> fieldList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        if (object == null) {
            return Pair.create(fieldList, valueList);
        }
        if (objectFieldMap.containsKey(object.getClass())) {
            fieldList = objectFieldMap.get(object.getClass());
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
            objectFieldMap.put(object.getClass(), fieldList);
            return Pair.create(fieldList, valueList);
        }
    }
}
