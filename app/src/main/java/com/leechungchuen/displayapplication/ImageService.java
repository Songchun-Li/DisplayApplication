package com.leechungchuen.displayapplication;

import android.graphics.Bitmap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {
    @GET("/{path}")
    Call<Bitmap> getImage(@Path("path") String imageURL);
}
