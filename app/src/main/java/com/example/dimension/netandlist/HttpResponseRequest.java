package com.example.dimension.netandlist;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpResponseRequest {
    public Response ReturnResponse(String path)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder().url(path).build();
            Response response = okHttpClient.newCall(request).execute();
            return response;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
