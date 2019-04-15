package com.leechungchuen.displayapplication.controller;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.res.AssetManager;
        import android.graphics.Typeface;
        import android.media.Image;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.leechungchuen.displayapplication.NetService;
        import com.leechungchuen.displayapplication.R;
        import com.leechungchuen.displayapplication.model.CheckRfService;
        import com.leechungchuen.displayapplication.model.Penguin;
        import com.leechungchuen.displayapplication.model.PenguinList;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

        import okhttp3.MediaType;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mainImage;
    private List<Penguin> penguinNameList;
    private int counter = 0;
    private BoardFragment popupBorad;
    private ProgressBar countdownBar;
    private String penguinToPop;

    private Timer timer;
    private TimerTask timerTask;

    private MsgReceiver msgReceiver;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar and status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //View decorView = this.getWindow().getDecorView();
        //decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_main);
        //Initialize views
        initView();


        //Obtain pengunNameList from server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://seniorprj.net/server_war/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService service = retrofit.create(NetService.class);
        Call<PenguinList> penguinListCall = service.getJson("getnames");
        penguinListCall.enqueue(new Callback<PenguinList>() {
            @Override
            public void onResponse(Call<PenguinList> call, Response<PenguinList> response) {
                //Obtain the name list
                Log.i("inretrofit", "Name List Get");
                penguinNameList = response.body().getPenguinList();
            }

            @Override
            public void onFailure(Call<PenguinList> call, Throwable t) {
                t.printStackTrace();
                Log.i("inretrofit", "Name List Failure");
//                Penguin tag = new Penguin();
//                tag.setName("no");
//                penguinNameList.add(tag);
            }
        });
    }

    /**
     * 从不可见到可见
     */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        //When the activity starts, call the service which check the update of RFID
        //Intent serviceIntent = new Intent(MainActivity.this, CheckRfService.class);
        //startService(serviceIntent);

        //register receiver
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.leechungchuen.displayapplication.controller.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);

        serviceIntent = new Intent(MainActivity.this, CheckRfService.class);
        startService(serviceIntent);

        //Start scheduled update
        handler.postDelayed(updateRunnable, 2000);
    }


    public void initView() {
        // initialize views
        mainImage = (ImageView) findViewById(R.id.main_pic);
        countdownBar = (ProgressBar) findViewById(R.id.main_progress);

    }

    public void onBoard(View view) {
        ///shutdown the timer for refreshing UI
        handler.removeCallbacks(updateRunnable);
        //Instantiate Fragment
        String name = penguinToPop;
        popupBorad = BoardFragment.newInstance("Bowie");
        //Load fragment
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.scale_animation, android.R.anim.fade_out)
                .add(R.id.main_fragment_layout, popupBorad).commitAllowingStateLoss();

        // wait for 3 seconds
        // call onJump
        handler.postDelayed(onholdRunnable, 3000);

    }

    public void refreshUI(String name) {
        //get penguin info based on name
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://seniorprj.net/server_war/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService service = retrofit.create(NetService.class);

        JSONObject penguinJson = new JSONObject();
        try {
            penguinJson.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), penguinJson.toString());
        Call<Penguin> penguinInfoCall = service.getJsonWJson("searchpenguin", body);

        penguinInfoCall.enqueue(new Callback<Penguin>() {
            @Override
            public void onResponse(Call<Penguin> call, Response<Penguin> response) {
                Penguin penguin = response.body();
                if (penguin != null) {
                    Bundle infoBundle = new Bundle();
                    infoBundle.putString("name", penguin.getName());
                    infoBundle.putString("species", penguin.getSpecies());
                    infoBundle.putString("gender", penguin.getGender());
                    infoBundle.putString("bandcolor1", penguin.getBandcolor1());
                    infoBundle.putString("bandcolor2", penguin.getBandcolor2());
                    infoBundle.putString("funfact", penguin.getFunfact());
                    infoBundle.putString("personality", penguin.getPersonality());
                    infoBundle.putInt("hatchyear", penguin.getHatchyear());

                    InfoFrag newFrag = InfoFrag.newInstance(infoBundle);
                    //Load fragment
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.appear_translate, android.R.anim.fade_out)
                            .replace(R.id.main_info_layout, newFrag).commitAllowingStateLoss();
                    String allUrl = "https://seniorprj.net/" + penguin.getImageUrl();

                    PicFragment newPicFrag = PicFragment.newInstance(allUrl);
                    //Load fragment
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.appear_translate, android.R.anim.fade_out)
                            .replace(R.id.main_pic_layout, newPicFrag).commitAllowingStateLoss();

                    /*
                    Glide.with(MainActivity.this).load(allUrl).placeholder(R.mipmap.container)
                            .dontAnimate()
                            .into(mainImage);
                            */
                    Log.i("main", "allUrl: " + allUrl);

                    //Prograss bar on upper right
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
                                    countdownBar.setProgress(countdownBar.getProgress() + 50);
                                }
                            });
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask, 0, 50);
                }
            }

            @Override
            public void onFailure(Call<Penguin> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * from visible to invisible
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        //Detect background service and eliminate it
        unregisterReceiver(msgReceiver);
        //remove the routine runnable
        handler.removeCallbacks(onholdRunnable);
    }


    Handler handler = new Handler();

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (penguinNameList != null && penguinNameList.size() > 0) {
                Penguin penguin = penguinNameList.get(counter);
                Log.i("Looping", "index: " + counter + " " + penguinNameList.size());
                if (counter + 1 < penguinNameList.size()) {
                    counter++;
                } else {
                    counter = 0;
                }
                String name = penguin.getName();
/*                if ("no".equalsIgnoreCase(name)) {
                    counter = 0;
                }*/
                refreshUI(name);

            } else {
                Toast.makeText(MainActivity.this, "Loading Data Fail. Reload in 10 seconds", Toast.LENGTH_LONG).show();
                //Try to re-obtain pengunNameList from server
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://seniorprj.net/server_war/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NetService service = retrofit.create(NetService.class);
                Call<PenguinList> penguinListCall = service.getJson("getnames");
                penguinListCall.enqueue(new Callback<PenguinList>() {
                    @Override
                    public void onResponse(Call<PenguinList> call, Response<PenguinList> response) {
                        Log.i("inretrofit", "Name List Get");
                        penguinNameList = response.body().getPenguinList();
                    }

                    @Override
                    public void onFailure(Call<PenguinList> call, Throwable t) {
                        t.printStackTrace();
                        Log.i("inretrofit", "Name List Failure");
                    }
                });
            }
            handler.postDelayed(this, 10000);
        }
    };


    Runnable onholdRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(MainActivity.this, EventActivity.class);
            intent.putExtra("name", penguinToPop);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.scale_animation, android.R.anim.fade_out)
                    .remove(popupBorad).commitAllowingStateLoss();
            startActivity(intent);
        }
    };

    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the swimming penguin and revoke event
            penguinToPop = intent.getStringExtra("name");
            Log.i("Main Thread: ", penguinToPop);
            ///shutdown the timer for refreshing UI
            handler.removeCallbacks(updateRunnable);
            popupBorad = BoardFragment.newInstance(penguinToPop);
            //Load fragment
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.scale_animation, android.R.anim.fade_out)
                    .add(R.id.main_fragment_layout, popupBorad).commitAllowingStateLoss();

            // wait for 3 seconds
            // call onJump
            handler.postDelayed(onholdRunnable, 3000);
        }
    }
}
