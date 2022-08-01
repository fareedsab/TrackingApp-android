package com.snappretail.trackingapp.Interface;

import com.snappretail.trackingapp.Utils.URLS;
import com.snappretail.trackingapp.model.login_model.LoginModel;
import com.snappretail.trackingapp.model.relocation_model.RepLocationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyWebService {
    @FormUrlEncoded
    @POST(URLS.Get_LOGIN_URL)
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<LoginModel> getLogingetUserLogin(
            @Field("username") String userName
            , @Field("password") String password
            , @Field("applicationId") String applicationID
            , @Field("latitude") String lat
            , @Field("longitude") String lng

    );
    @FormUrlEncoded
    @POST(URLS.Get_REPLOCATION_URL)
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<RepLocationModel> getLocation(
            @Field("timestamp") String userName
            , @Field("latitude") String password
            , @Field("longitude") String applicationID
            , @Field("representativeID") String lat
            , @Field("applicationID") String lng

    );
}
