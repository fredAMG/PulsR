package com.example.fred_liu.pulsr.Home;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fred_liu.pulsr.MainActivity;
import com.example.fred_liu.pulsr.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.places.Places;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.os.SystemClock.uptimeMillis;

public class DistanceActivity extends AppCompatActivity implements OnDataPointListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mClient;
    private static final String AUTH_PENDING = "auth_state_pending";
    private static final String TAG = "DistanceActivity";
    protected boolean authInProgress = false;
    private float current_dis;
    private float current_cals;
    private long current_steps;


    private float init_dis;
    private float init_cals;
    private long init_steps;


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
        createDataSeries3();
        createDataSeries2();
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

                DistanceActivity.DistanceDataTask distanceDataTask = new DistanceDataTask();
                distanceDataTask.execute();

                DistanceActivity.CalsDataTask calsDataTask = new CalsDataTask();
                calsDataTask.execute();

                DistanceActivity.StepsDataTask stepsDataTask = new StepsDataTask();
                stepsDataTask.execute();

                updateUI();
            }
        };

        dis_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerState = timerState.Running;
                DistanceActivity.InitDistanceDataTask initDistanceDataTask = new InitDistanceDataTask();
                initDistanceDataTask.execute();

                DistanceActivity.InitCalsDataTask initCalsDataTask = new InitCalsDataTask();
                initCalsDataTask.execute();

                DistanceActivity.InitStepsDataTask initStepsDataTask = new InitStepsDataTask();
                initStepsDataTask.execute();
                startTime = uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                startUI();
                onStart();
                updateButtons();
            }
        });
        dis_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerState = timerState.Paused;
                timeSwapBuff+=timeInMillsecond;
                customHandler.removeCallbacks(updateTimerThread);
                pauseUI();
                onPause();
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
                dis_distance.setText("0 km");
                dis_cals.setText("0 cals");
                stopUI();
                onStop();
                updateButtons();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.CONFIG_API)
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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
                .setIndex(mSeries3Index)
                .setDuration(2000)
                .setDelay(1350)
                .build());

        distance_DecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(2000)
                .setDelay(1450)
                .build());
    }

    private void startUI() {
        distance_DecoView.addEvent(new DecoEvent.Builder(60)
                .setIndex(mSeries1Index)
                .setDelay(1250)
                .setDuration(60000)
                .build());
    }

    private void pauseUI(){
        distance_DecoView.clearAnimation();


    }

    private void stopUI(){
        distance_DecoView.deleteAll();
        createBackSeries();
        createDataSeries1();
        createDataSeries3();
        createDataSeries2();
        createEvents();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        intent.putExtra("message", "loginStatus_from_DistanceActivity");
        intent.putExtra("loginStatus", 1);

        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        DataSourcesRequest dataSourcesRequest = new DataSourcesRequest.Builder()
                .setDataTypes( DataType.TYPE_STEP_COUNT_DELTA )
                .setDataTypes( DataType.TYPE_HEART_RATE_BPM )
                .setDataTypes( DataType.TYPE_CALORIES_EXPENDED )
                .setDataSourceTypes( DataSource.TYPE_RAW )
                .build();
        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {

            }
        };

        Fitness.SensorsApi.findDataSources(mClient, dataSourcesRequest)
                .setResultCallback(dataSourcesResultCallback);

    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO move to subclass so that we can disable UI components, etc., in the event that the service is inaccessible
        // If your connection to the client gets lost at some point,
        // you'll be able to determine the reason and react to it here.
        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Log.i(TAG, "GoogleApiClient connection lost. Reason: Network lost.");
        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Log.i(TAG, "GoogleApiClient connection lost. Reason: Service disconnected");
        }
    }

    private static final int REQUEST_OAUTH = 1;


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( !authInProgress ) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult( this, REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {
                Log.e( "GoogleFit", "sendingIntentException " + e.getMessage() );
            }
        } else {
            Log.e( "GoogleFit", "authInProgress" );
        }
    }

    @Override
    public void onDataPoint(com.google.android.gms.fitness.data.DataPoint dataPoint) {

    }

    private class InitDistanceDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            float total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_DISTANCE_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
            } else {
                Log.w(TAG, "There was a problem getting the init_dis.");
            }

            Log.i(TAG, "init_dis: " + total);

            init_dis = total;

            return null;
        }
    }

    private class DistanceDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            float total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_DISTANCE_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
            } else {
                Log.w(TAG, "There was a problem getting the distance.");
            }

            Log.i(TAG, "Distance: " + total);

            current_dis = total;

            return null;
        }
    }

    private class InitCalsDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            float total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
            } else {
                Log.w(TAG, "There was a problem getting the init_cals.");
            }

            Log.i(TAG, "init_cals: " + total);

            init_cals = total;

            return null;
        }
    }

    private class CalsDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            float total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
            } else {
                Log.w(TAG, "There was a problem getting the cals consumption.");
            }

            Log.i(TAG, "cals consumption: " + total);

            current_cals = total;

            return null;
        }
    }

    private class InitStepsDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            long total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            } else {
                Log.w(TAG, "There was a problem getting the init_steps.");
            }

            Log.i(TAG, "init_steps: " + total);

            init_steps = total;

            return null;
        }
    }

    private class StepsDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            long total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            } else {
                Log.w(TAG, "There was a problem getting the current_steps.");
            }

            Log.i(TAG, "current_steps: " + total);

            current_steps = total;

            return null;
        }
    }

    private void updateUI(){

        // here you check the value of getActivity() and break up if needed
        if(this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dis_distance.setText(String.format("%.2f km", current_dis - init_dis));

//                    dis_distance.setText(String.format("%d steps", current_steps - init_steps));

                    dis_cals.setText(String.format("%.0f cals", current_cals - init_cals));
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }
    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mClient.stopAutoManage(this);
        mClient.disconnect();
    }
}
