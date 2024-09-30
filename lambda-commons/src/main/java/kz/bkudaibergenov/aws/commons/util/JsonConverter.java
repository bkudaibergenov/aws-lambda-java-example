package kz.bkudaibergenov.aws.commons.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonConverter {

    public static String convertToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T convertToObject(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

}
