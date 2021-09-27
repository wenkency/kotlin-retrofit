package com.lven.retrofit.utils;

import android.text.TextUtils;

import androidx.collection.ArrayMap;

import com.lven.retrofit.api.FieldToJson;
import com.lven.retrofit.core.RestCreator;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 请求参数封装类
 */
public class RestUtils {

    /**
     * 请求共用方法
     */
    public static Map<String, Object> getParams(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new ArrayMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name == null || name.contains("$") || name.equals("serialVersionUID")) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    // 根据标识转成Json
                    FieldToJson toJson = field.getAnnotation(FieldToJson.class);
                    if (toJson != null) {
                        map.put(name, RestCreator.INSTANCE.getGson().toJson(value));
                    } else {
                        map.put(name, String.valueOf(value));
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 请求共用方法
     *
     * @param params String[] 数组参数. 格式: {"key", value, "key", value...}
     */
    public static Map<String, Object> getParams(String... params) {
        Map<String, Object> map = new ArrayMap<>();
        if (params == null) {
            return map;
        }
        // 业务的参数
        String key = null;
        String value = null;
        for (int i = 0; i < params.length; i++) {
            if (i % 2 == 0) {
                key = params[i];// 取得Key
            } else {
                value = params[i];// 取得值
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                map.put(key, value);//添加到集合
            }
        }
        key = null;
        value = null;
        return map;
    }
}