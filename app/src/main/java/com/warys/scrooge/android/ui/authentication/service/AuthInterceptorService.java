package com.warys.scrooge.android.ui.authentication.service;

import okhttp3.*;

import java.io.IOException;

import static com.warys.scrooge.android.ui.authentication.consumer.AuthConstant.*;

public class AuthInterceptorService implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Headers headers = new Headers.Builder()
                .add(AUTHORIZATION, BEARER)
                .add(USER_AGENT, "Android")
                .build();

        Request newRequest = originalRequest.newBuilder()
                .addHeader(AUTHORIZATION, BEARER) //Adds a header with name and value.
                .addHeader(USER_AGENT, "Android")
                .cacheControl(CacheControl.FORCE_CACHE) // Sets this request's Cache-Control header, replacing any cache control headers already present.
                .headers(headers) //Removes all headers on this builder and adds headers.
                .method(originalRequest.method(), originalRequest.body()) // Adds request method and request body
                .removeHeader(AUTHORIZATION) // Removes all the headers with this name
                .build();

        return chain.proceed(newRequest);
    }
}

