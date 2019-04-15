package com.leechungchuen.displayapplication.model;

import android.os.AsyncTask;
import android.util.Log;


import com.leechungchuen.displayapplication.NetService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetPenguinListAsync extends AsyncTask<String, Void, List<Penguin>> {
    @Override
    protected List<Penguin> doInBackground(String... strings) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(strings[0])
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetService service = retrofit.create(NetService.class);
        Call<PenguinList> call = service.getJson("syncpenguin");

        call.enqueue(new Callback<PenguinList>() {
            @Override
            public void onResponse(Call<PenguinList> call, Response<PenguinList> response) {
                List<Penguin> penguinList = response.body().getPenguinList();
                for (int i = 0; i < penguinList.size(); i++) {
                    Penguin penguin = penguinList.get(i);
                    Log.d("AsyncTask",penguin.getName());
                }
            }
            @Override
            public void onFailure(Call<PenguinList> call, Throwable t) {
                t.printStackTrace();
            }

        });


        return null;
    }
}
