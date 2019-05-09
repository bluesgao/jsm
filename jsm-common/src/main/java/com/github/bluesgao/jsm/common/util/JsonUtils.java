package com.github.bluesgao.jsm.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    //不用创建对象,直接使用Gson.就可以调用方法
    private static Gson gson = null;

    //判断gson对象是否存在了,不存在则创建对象
    static {
        if (gson == null) {
            //gson = new Gson();
            //当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
    }

    //无参的私有构造方法
    private JsonUtils() {
    }

    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    public static String object2json(Object object) {
        String str = null;
        if (gson != null) {
            str = gson.toJson(object);
        }
        return str;
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param str
     * @param cls
     * @return
     */
    public static <T> T json2object(String str, Class<T> cls) {
        T t = null;
        if (gson != null) {
            //传入json对象和对象类型,将json转成对象
            t = gson.fromJson(str, cls);
        }
        return t;
    }

    /**
     * json字符串转成list
     *
     * @param str
     * @param cls
     * @return
     */
    public static <T> List<T> json2list(String str, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            //根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
            list = gson.fromJson(str, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * json字符串转成map的
     *
     * @param str
     * @return
     */
    public static <T> Map<String, T> json2map(String str) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(str, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
