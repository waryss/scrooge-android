package com.warys.scrooge.android.common.consumer;

import com.warys.scrooge.android.authentication.service.PublicUsersApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private Retrofit retrofit;
    private PublicUsersApiService publicUsersApiService;
    private final String baseUrl;
    OkHttpClient baseOkHttpClient;

    RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void init() {
        if (retrofit == null) {
            //OkHttpClient okHttpClient = baseOkHttpClient
            //       .newBuilder()
            //      .addInterceptor(new AuthInterceptorService())
            //      .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    //.client(baseOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public PublicUsersApiService getPublicUsersApiService() {
        if (retrofit == null) {
            throw new IllegalStateException("Rest client not initialized yet");
        }

        if (publicUsersApiService == null) {
            publicUsersApiService = retrofit.create(PublicUsersApiService.class);
        }

        return publicUsersApiService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
