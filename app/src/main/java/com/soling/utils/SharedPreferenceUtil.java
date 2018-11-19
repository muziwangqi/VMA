package com.soling.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SharedPreferenceUtil {

    public static final String fileName = "shareData";

    //判断数据类型存执
    public static void put(Context context, String string, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (object instanceof String) {
            editor.putString(string, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(string, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(string, (Boolean) object);
        } else if (object instanceof Long) {
            editor.putLong(string, (Long) object);
        } else if (object instanceof Float) {
            editor.putFloat(string, (Float) object);
        } else {
            editor.putString(string, object.toString());
        }
        SharedPreferenceCompat.apply(editor);
    }

    //取值
    public static Object get(Context context, String string, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (object instanceof String) {
            return sharedPreferences.getString(string, (String) object);
        } else if (object instanceof Integer) {
            return sharedPreferences.getInt(string, (Integer) object);
        } else if (object instanceof Long) {
            return sharedPreferences.getLong(string, (Long) object);
        } else if (object instanceof Float) {
            return sharedPreferences.getFloat(string, (Float) object);
        } else if (object instanceof Boolean) {
            return sharedPreferences.getBoolean(string, (Boolean) object);
        }
        return null;
    }

    //移除
    public static void remove(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(string);
        SharedPreferenceCompat.apply(editor);
    }

    //移除所有
    public static void clear(Context context, String string) {
        SharedPreferences sharedPreference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        SharedPreferenceCompat.apply(editor);
    }

    //判断string是否已存在
    public static boolean contains(Context context, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.contains(string);
    }

    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    private static class SharedPreferenceCompat {
        private static final Method exitApply = findApplyMethod();

        private static Method findApplyMethod() {
            try {
                Class cls = SharedPreferences.Editor.class;
                return cls.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static void apply(SharedPreferences.Editor editor) {
            if (exitApply != null) {
                try {
                    exitApply.invoke(editor);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    ;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            editor.commit();
        }
    }
}
