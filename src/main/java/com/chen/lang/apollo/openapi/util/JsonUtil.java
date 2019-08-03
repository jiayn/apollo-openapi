package com.chen.lang.apollo.openapi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-22 17:46
 */
public class JsonUtil {

    private static Gson gson = (new GsonBuilder()).setPrettyPrinting()
        .serializeNulls()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();

    /**
     * GSON 美化输出
     * @param object
     * @return
     */
    public static String toPrettyFormat(Object object) {
        //字符串处理
        if (object instanceof String) {
            JsonParser jsonPar = new JsonParser();
            JsonElement jsonEl = jsonPar.parse((String) object);
            String prettyJson = gson.toJson(jsonEl);
            return prettyJson;
        }

        return gson.toJson(object);
    }

    public static <T> T json2Obj(String jsonStr, Class<T> type) {
        return gson.fromJson(jsonStr, type);
    }



}
