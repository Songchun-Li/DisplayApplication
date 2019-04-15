package com.leechungchuen.displayapplication.controller;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leechungchuen.displayapplication.NetService;
import com.leechungchuen.displayapplication.R;
import com.leechungchuen.displayapplication.model.Penguin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventActivity extends AppCompatActivity {


    private ImageView eventImage;
    private ProgressBar countdownBar;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //View decorView = this.getWindow().getDecorView();
        //decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_event);

        //Initialize the views
        initView();

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            loadContent(name);
        }

        handler.postDelayed(closeRunnable, 10000);

    }

    public void initView(){
        ImageView waveView1 = (ImageView) findViewById(R.id.event_swimming_gif);
        ImageView waveView2 = (ImageView) findViewById(R.id.event_swimming_gif2);
        eventImage = ((ImageView) findViewById(R.id.event_pic));
        TextView tagView = (TextView) findViewById(R.id.event_swimming_tag);
        countdownBar = (ProgressBar) findViewById(R.id.event_progress);


        Animation animation= AnimationUtils.loadAnimation(this, R.anim.wave_translate);
        waveView1.startAnimation(animation);

        Animation animation2= AnimationUtils.loadAnimation(this, R.anim.wave_translate_comp);
        waveView2.startAnimation(animation2);
        AssetManager mgr = getAssets();
        Typeface mediumItalic = Typeface.createFromAsset(mgr, "font/tisasans_mi.otf");
        tagView.setTypeface(mediumItalic);
    }

    public void loadContent(String name) {
        //get penguin info based on name
        JSONObject penguinJson = new JSONObject();
        try {
            penguinJson.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://seniorprj.net/server_war/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService service = retrofit.create(NetService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), penguinJson.toString());
        Call<Penguin> penguinInfoCall = service.getJsonWJson("searchpenguin", body);

        countdownBar.setProgress(0);
        countdownBar.setMax(10000);
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countdownBar.setProgress(countdownBar.getProgress()+50);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 50);

        penguinInfoCall.enqueue(new Callback<Penguin>() {
            @Override
            public void onResponse(Call<Penguin> call, Response<Penguin> response) {
                Penguin penguin = response.body();
                if (penguin != null) {
                    Log.i("onResponse: ","gender:" + penguin.getGender());
                    Log.i("onResponse: ","band color1: " +penguin.getBandcolor1());
                    Log.i("onResponse: ","band color2: " +penguin.getBandcolor2());
                    Log.i("onResponse: ","species: " +penguin.getSpecies());
                    Log.i("onResponse: ","hatch year: " +penguin.getHatchyear());
                    Log.i("onResponse: ","imageUrl: " +penguin.getImageUrl());


                    Bundle infoBundle = new Bundle();
                    infoBundle.putString("name", penguin.getName());
                    infoBundle.putString("species",penguin.getSpecies());
                    infoBundle.putString("gender", penguin.getGender());
                    infoBundle.putString("bandcolor1", penguin.getBandcolor1());
                    infoBundle.putString("bandcolor2",penguin.getBandcolor2());
                    infoBundle.putString("funfact",penguin.getFunfact() );
                    infoBundle.putString("personality",penguin.getPersonality());
                    infoBundle.putInt("hatchyear", penguin.getHatchyear());


                    InfoFrag newFrag = InfoFrag.newInstance(infoBundle);
                    //Load fragment
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.event_info_layout, newFrag).commitAllowingStateLoss();

                    //Load fragment
                    String allUrl = "https://seniorprj.net/" + penguin.getImageUrl();
                    PicFragment newPicFrag = PicFragment.newInstance(allUrl);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.event_pic_layout, newPicFrag).commitAllowingStateLoss();

                    /*Glide.with(EventActivity.this).load(allUrl)
                            .dontAnimate()
                            .into(eventImage);
                    Log.i("main", "allUrl: " + allUrl);*/

                }

            }
            @Override
            public void onFailure(Call<Penguin> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    Handler handler = new Handler();

    Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
}
