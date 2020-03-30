package com.warys.scrooge.android.ui.authentication.service;


import com.warys.scrooge.android.ui.authentication.consumer.model.request.LoginRequest;
import com.warys.scrooge.android.ui.authentication.consumer.model.request.UserRequest;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.LoginResponse;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.UserResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PublicUsersApiService {

    @POST("public/login")
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST("public/register")
    Single<UserResponse> register(@Body UserRequest request);
}
