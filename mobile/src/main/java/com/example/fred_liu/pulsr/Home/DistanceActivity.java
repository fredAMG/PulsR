package com.example.fred_liu.pulsr.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.fred_liu.pulsr.MainActivity;
import com.example.fred_liu.pulsr.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


import static android.os.SystemClock.uptimeMillis;

public class DistanceActivity extends AppCompatActivity{

    private DecoView distance_DecoView;
    private final float mSeriesMax = 5f;
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    private TextView dis_timer, dis_distance, dis_cals;
    FloatingActionButton dis_stop, dis_pause, dis_start;

    private long startTime=0L, timeInMillsecond=0L,timeSwapBuff=0L,updateTime=0L;
    int secs;
    int mins;
    int hours;

    final Handler customHandler = new Handler();

    private enum TimerState{
        Stopped, Paused, Running
    }
    private TimerState timerState = TimerState.Stopped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        distance_DecoView = findViewById(R.id.distance_DecoView);
        dis_timer = findViewById(R.id.dis_timer);
        dis_distance = findViewById(R.id.dis_distance);
        dis_cals = findViewById(R.id.dis_cals);
        dis_start = findViewById(R.id.dis_start);
        dis_pause = findViewById(R.id.dis_pause);
        dis_stop = findViewById(R.id.dis_stop);



        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();
        createEvents();

        Runnable updateTimerThread = new Runnable() {
            @Override
            public void run() {
                timeInMillsecond = uptimeMillis()-startTime;
                updateTime = timeSwapBuff+timeInMillsecond;
                secs = (int)(updateTime/1000);
                mins=secs/60;
                hours=mins/60;
                secs%=60;
                mins%=60;
                dis_timer.setText(String.format("%02d",mins)+":"
                        +String.format("%02d",secs));
                customHandler.postDelayed(this, 0);
            }
        };

        dis_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerState = timerState.Running;
                startTime = uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                startUI();
                updateButtons();
            }
        });
        dis_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerState = timerState.Paused;
                timeSwapBuff+=timeInMillsecond;
                customHandler.removeCallbacks(updateTimerThread);
                pauseUI();
                updateButtons();

            }
        });
        dis_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerState = timerState.Stopped;
                startTime = 0L;
                timeInMillsecond = 0L;
                timeSwapBuff =0L;
                updateTime =0L;
                secs = 0;
                mins = 0;
                hours = 0;

                dis_timer.setText("00:00");
                stopUI();
                updateButtons();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

    }

    private void updateButtons(){
        if (timerState == timerState.Running) {
            dis_start.setEnabled(false);
            dis_pause.setEnabled(true);
            dis_stop.setEnabled(false);
        }
        else if (timerState == timerState.Stopped){
            dis_start.setEnabled(true);
            dis_pause.setEnabled(false);
            dis_stop.setEnabled(false);
        }
        else if (timerState == timerState.Paused){
            dis_start.setEnabled(true);
            dis_pause.setEnabled(false);
            dis_stop.setEnabled(true);

        }
    }


    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = distance_DecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, 60, 0)
                .setInitialVisibility(false)
                .build();

        mSeries1Index = distance_DecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, 1500, 0)
                .setInitialVisibility(false)
                .build();

        mSeries2Index = distance_DecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, 5, 0)
                .setInitialVisibility(false)
                .build();

        mSeries3Index = distance_DecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        distance_DecoView.executeReset();

        distance_DecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        distance_DecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        distance_DecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(2000)
                .setDelay(1350)
                .build());

        distance_DecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(2000)
                .setDelay(1450)
                .build());
    }

    private void startUI() {
        distance_DecoView.addEvent(new DecoEvent.Builder(60)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .setDuration(60000)
                .build());
    }

    private void pauseUI(){
        distance_DecoView.stopNestedScroll();


    }

    private void stopUI(){
        distance_DecoView.deleteAll();
        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();
        createEvents();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        intent.putExtra("message", "loginStatus_from_DistanceActivity");
        intent.putExtra("loginStatus", 1);

        startActivity(intent);
    }
}
