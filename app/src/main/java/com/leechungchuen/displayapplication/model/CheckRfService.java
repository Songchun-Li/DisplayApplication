package com.leechungchuen.displayapplication.model;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.leechungchuen.displayapplication.NetService;
import com.leechungchuen.displayapplication.R;
import com.leechungchuen.displayapplication.controller.EventActivity;
import com.leechungchuen.displayapplication.controller.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class CheckRfService extends Service {


    private NetService service;

    private Intent intent = new Intent("com.leechungchuen.displayapplication.controller.RECEIVER");
    public CheckRfService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //This service will keep contact the server to fetch RFID update;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://seniorprj.net/server_war/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NetService.class);
        handler.postDelayed(checkRunnable,10);

        //Then destroy itself to recycle resource
        stopSelf();
        return START_STICKY;
    }

    Handler handler = new Handler();

    Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            //After getting new RFID record, send the name of the swimming penguin back UI Thread with handler


            Call<Penguin> swimPenguinCall = service.getSwim("updaterfid");
            swimPenguinCall.enqueue(new Callback<Penguin>() {
                @Override
                public void onResponse(Call<Penguin> call, Response<Penguin> response) {
                    Penguin swimmingPenguin = response.body();
                    if (swimmingPenguin != null) {
                        //New swimming penguin
                        //msg.what = 1;
                        String penguinName = swimmingPenguin.getName();
                        if ("no".equals(penguinName)){
                            //nothing happen
                            Log.i("Check RFID Service:",penguinName);
                        } else {
                            //Get new penguin in the pool
                            Bundle bundle = new Bundle();
                            intent.putExtra("name", penguinName);
                            sendBroadcast(intent);
                            stopSelf();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Penguin> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            handler.postDelayed(checkRunnable, 2000);
        }
    };




}
