package com.develop.xdk.df.rechargemachine.http;

import android.util.Log;

import com.develop.xdk.df.rechargemachine.entity.HttpResult;
import com.google.gson.Gson;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 返回包体数据处理
 */
class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
            String response = value.string();
            //httpResult 只解析result字段
            HttpResult httpResult = gson.fromJson(response, HttpResult.class);
            if (httpResult.getCode()== 0) {
                throw new ApiException(httpResult.getMsg());
            }
            return gson.fromJson(response, type);
    }
}
