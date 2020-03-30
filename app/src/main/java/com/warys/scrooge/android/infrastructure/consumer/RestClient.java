package com.warys.scrooge.android.infrastructure.consumer;

import com.warys.scrooge.android.infrastructure.service.ApiServiceLoader;
import com.warys.scrooge.android.infrastructure.consumer.model.ErrorResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.annotation.Annotation;

import static com.warys.scrooge.android.ui.authentication.consumer.AuthConstant.API_SERVER_URL;

public class RestClient implements ApiServiceLoader {

    public static RestClient INSTANCE;

    private Retrofit retrofit;
    private final String baseUrl;

    public static RestClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestClient();
            INSTANCE.init();
        }

        return INSTANCE;
    }

    private RestClient() {
        this.baseUrl = API_SERVER_URL;
    }

    public void init() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }


    public <T> T load(Class<T> service) {
        if (retrofit == null) {
            throw new IllegalStateException("Rest client not initialized yet");
        }
        return retrofit.create(service);
    }

    public Converter<ResponseBody, ErrorResponse> getResponseBodyConverter(Class<ErrorResponse> errorResponseClass, Annotation[] annotations) {
        return retrofit.responseBodyConverter(errorResponseClass, annotations);
    }
}
