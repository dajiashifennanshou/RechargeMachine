package com.develop.xdk.df.rechargemachine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/6/11.
 */

public class BeanUtil {
    public static SortedMap<String, Object> ClassToMap(Object tempClass) {
        SortedMap<String, Object> map = new TreeMap<>();
        List<Field> fieldList = new ArrayList<>() ;
        Class thecalss = tempClass.getClass();
        while (thecalss != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(thecalss.getDeclaredFields()));
            thecalss = thecalss.getSuperclass(); //得到父类,然后赋给自己
        }
        for (short i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            field.setAccessible(true);
            String fieldName = field.getName();
            // 如果属性名称和属性一样
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class tCls = tempClass.getClass();
            Method getMethod;
            try {
                getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(tempClass, new Object[]{});
                map.put(fieldName, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}


