package com.example.learn.data.api;

import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder newRequest = originalRequest.newBuilder();

        String token = DataStoreSingleton.getInstance().getValueSync(DatastoreConst.ACC_TOKEN);

        if(token != null && token != "") {
            newRequest.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(newRequest.build());
    }
}

