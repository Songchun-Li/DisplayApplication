package com.leechungchuen.displayapplication;

import com.leechungchuen.displayapplication.model.Penguin;
import com.leechungchuen.displayapplication.model.PenguinList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetService {
    @GET("disp")
    Call<PenguinList> getJson(@Query("action") String action);

    @POST("disp")
     Call<Penguin> getJsonWJson(@Query("action") String action, @Body RequestBody body);

    @GET("disp")
    Call<Penguin> getSwim(@Query("action") String action);

}
