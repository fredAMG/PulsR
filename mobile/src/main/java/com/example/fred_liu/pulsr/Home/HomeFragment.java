package com.example.fred_liu.pulsr.Home;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fred_liu.pulsr.Home.Calendar.CalendarFragment;
import com.example.fred_liu.pulsr.Notification.ParkingSpot;
import com.example.fred_liu.pulsr.Notification.SpotAdapter;
import com.example.fred_liu.pulsr.R;
import com.example.fred_liu.pulsr.Timer.TimerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.MapView;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.jjoe64.graphview.series.PointsGraphSeries;


public class HomeFragment extends Fragment implements OnDataPointListener, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mClient;
    private static final String TAG = "HomeFragment";
    private static final String AUTH_PENDING = "auth_state_pending";
    protected boolean authInProgress = false;
    private Calendar calendar;
    private float heart_rate;
    private long daily_steps;
    private float daily_cals;


    TextView textDate, currentHeartRate, currentSteps, currentCals, textLocation;
    GraphView heartRateGraph, stepsRateGraph, calsRateGraph;
    FloatingActionButton start_workout;
    Switch switch1, switch2, switch3, switch4, switch5;
    FrameLayout map_frame;
    FrameLayout decoView_frame;
    FrameLayout calendar_frame;
    FragmentTransaction fragmentTransaction;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWeek;
    private PointsGraphSeries<DataPoint> heartRateSeries;
    private PointsGraphSeries<DataPoint> stepsSeries;
    private PointsGraphSeries<DataPoint> calsSeries;
    private Timer mTimer;
    LinearLayout linearLayoutHeartRates, linearLayoutSteps, linearLayoutCals;





    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textDate = view.findViewById(R.id.textDate);
        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);
        switch4 = view.findViewById(R.id.switch4);
        switch5 = view.findViewById(R.id.switch5);



        decoView_frame = view.findViewById(R.id.decoView_frame);
        map_frame = view.findViewById(R.id.map_frame);
        calendar_frame = view.findViewById(R.id.calendar_frame);

        textLocation = view.findViewById(R.id.textLocation);


        currentHeartRate =  view.findViewById(R.id.currentHeartRate);
        currentSteps =  view.findViewById(R.id.currentSteps);
        currentCals =  view.findViewById(R.id.currentCals);

        heartRateGraph = view.findViewById(R.id.heartRateGraph);
        stepsRateGraph = view.findViewById(R.id.stepsGraph);
        calsRateGraph = view.findViewById(R.id.calsGraph);
        start_workout = view.findViewById(R.id.start_workout);

        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MMMM-dd");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        textDate.setText(currentDate);

        switch1.setChecked(false);
        calendar_frame.setVisibility(View.GONE);

        switch2.setChecked(false);
        map_frame.setVisibility(View.GONE);

        switch3.setChecked(false);
        heartRateGraph.setVisibility(View.GONE);

        switch4.setChecked(false);
        stepsRateGraph.setVisibility(View.GONE);

        switch5.setChecked(false);
        calsRateGraph.setVisibility(View.GONE);

        linearLayoutHeartRates = view.findViewById(R.id.linearLayoutHeartRates);
        linearLayoutHeartRates.setVisibility(View.GONE);

        linearLayoutSteps = view.findViewById(R.id.linearLayoutSteps);
        linearLayoutSteps.setVisibility(View.GONE);

        linearLayoutCals = view.findViewById(R.id.linearLayoutCals);
        linearLayoutCals.setVisibility(View.GONE);


        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.calendar_frame, new CalendarFragment());
        fragmentTransaction.commit();

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_frame, new MapFragment());
        fragmentTransaction.commit();

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.decoView_frame, new DecoviewFragment());
        fragmentTransaction.commit();

        textLocation.setClickable(true);
        textLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, new MapFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch1.isChecked()){
                    calendar_frame.setVisibility(View.VISIBLE);
                }
                else if(!switch1.isChecked()) {
                    calendar_frame.setVisibility(View.GONE);
                }
            }
        };

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch2.isChecked()) {
                    map_frame.setVisibility(View.VISIBLE);
                }
                else if(!switch2.isChecked()) {
                    map_frame.setVisibility(View.GONE);
                }
            }
        };
        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener3 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch3.isChecked()){
                    heartRateGraph.setVisibility(View.VISIBLE);
                }
                else if(!switch1.isChecked()) {
                    heartRateGraph.setVisibility(View.GONE);
                }
            }
        };
        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener4 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch4.isChecked()){
                    stepsRateGraph.setVisibility(View.VISIBLE);
                }
                else if(!switch1.isChecked()) {
                    stepsRateGraph.setVisibility(View.GONE);
                }
            }
        };
        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener5 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch5.isChecked()){
                    calsRateGraph.setVisibility(View.VISIBLE);
                }
                else if(!switch1.isChecked()) {
                    calsRateGraph.setVisibility(View.GONE);
                }
            }
        };

        switch1.setOnCheckedChangeListener(onCheckedChangeListener1);
        switch2.setOnCheckedChangeListener(onCheckedChangeListener2);
        switch3.setOnCheckedChangeListener(onCheckedChangeListener3);
        switch4.setOnCheckedChangeListener(onCheckedChangeListener4);
        switch5.setOnCheckedChangeListener(onCheckedChangeListener5);


        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mClient = new GoogleApiClient.Builder(this.getActivity())
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
                .enableAutoManage(this.getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mTimer = new Timer();

        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat 1/2 minute.

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                StepsDataTask stepsDataTask = new StepsDataTask();
                stepsDataTask.execute();
                HeartRateDataTask heartRateDataTask = new HeartRateDataTask();
                heartRateDataTask.execute();
                CalsDataTask calsDataTask = new CalsDataTask();
                calsDataTask.execute();
                updateUI();
            }
        },delay,period);

        guessCurrentPlace();

        start_workout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DistanceActivity.class);
                String message = "start";
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        return view;
    }


    private void guessCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mClient, null);
        result.setResultCallback( new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult( PlaceLikelihoodBuffer likelyPlaces ) {

                PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                String content = "";
                if( placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty( placeLikelihood.getPlace().getName() ) )
                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n";
                if( placeLikelihood != null )
                    content += "Percent change of being there: " + (int) ( placeLikelihood.getLikelihood() * 100 ) + "%";

                textLocation.setText(placeLikelihood.getPlace().getName());

                Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                likelyPlaces.release();
            }
        });
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
//                for( DataSource dataSource : dataSourcesResult.getDataSources() ) {
//
//                    if( DataType.TYPE_STEP_COUNT_CADENCE.equals( dataSource.getDataType() ) ) {
//                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CADENCE);
//                    }
//                }
            }
        };

        Fitness.SensorsApi.findDataSources(mClient, dataSourcesRequest)
                .setResultCallback(dataSourcesResultCallback);

    }

//    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
//        SensorRequest request = new SensorRequest.Builder()
//                .setDataSource( dataSource )
//                .setDataType( dataType )
//                .setSamplingRate( 3, TimeUnit.SECONDS )
//                .build();
//        Fitness.SensorsApi.add(mClient, request, this)
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        if (status.isSuccess()) {
//                            Log.e("GoogleFit", "SensorApi successfully added");
//                        } else {
//                            Log.e("GoogleFit", "adding status: " + status.getStatusMessage());
//                        }
//                    }
//                });
//    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO move to subclass so that we can disable UI components, etc., in the event that the service is inaccessible
        // If your connection to the client gets lost at some point,
        // you'll be able to determine the reason and react to it here.
        if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Log.i(TAG, "GoogleApiClient connection lost. Reason: Network lost.");
        } else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Log.i(TAG, "GoogleApiClient connection lost. Reason: Service disconnected");
        }
    }

    private static final int REQUEST_OAUTH = 1;


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( !authInProgress ) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult( this.getActivity(), REQUEST_OAUTH );
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

    private class HeartRateDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            float total = 0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_HEART_RATE_BPM);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_MAX).asFloat();
            } else {
                Log.w(TAG, "There was a problem getting the heart rate.");
            }

            Log.i(TAG, "Heart Rate: " + total);

            heart_rate = total;

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
                Log.w(TAG, "There was a problem getting the step count.");
            }

            Log.i(TAG, "Total steps: " + total);

            daily_steps = total;

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

            daily_cals = total;

            return null;
        }
    }

    int refreshTime = 0;
    private void updateUI(){

        // here you check the value of getActivity() and break up if needed
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentHeartRate.setText(String.format("%.0f", heart_rate));
                    currentSteps.setText(String.valueOf(daily_steps));
                    currentCals.setText(String.format("%.0f", daily_cals));

                    heartRateSeries = new PointsGraphSeries<>(new DataPoint[] {

                            new DataPoint(0, heart_rate)
                    });

                    stepsSeries = new PointsGraphSeries<>(new DataPoint[] {
                            new DataPoint(0,daily_steps)
                    });

                    calsSeries = new PointsGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, daily_cals)
                    });


                    heartRateSeries.appendData(new DataPoint(refreshTime++, heart_rate),true, 60);
                    // set manual X bounds
                    heartRateGraph.getViewport().setYAxisBoundsManual(true);
                    heartRateGraph.getViewport().setMinY(0);
                    heartRateGraph.getViewport().setMaxY(100);

                    heartRateGraph.getViewport().setXAxisBoundsManual(true);
                    heartRateGraph.getViewport().setMinX(0);
                    heartRateGraph.getViewport().setMaxX(60);

                    // enable scaling and scrolling
                    heartRateGraph.getViewport().setScalable(true);
                    heartRateGraph.getViewport().setScalableY(true);
                    heartRateGraph.addSeries(heartRateSeries);


                    stepsSeries.appendData(new DataPoint(refreshTime++, daily_steps),true, 60);
                    // set manual X bounds
                    stepsRateGraph.getViewport().setYAxisBoundsManual(true);
                    stepsRateGraph.getViewport().setMinY(daily_steps - 50);
                    stepsRateGraph.getViewport().setMaxY(daily_steps + 50);

                    stepsRateGraph.getViewport().setXAxisBoundsManual(true);
                    stepsRateGraph.getViewport().setMinX(0);
                    stepsRateGraph.getViewport().setMaxX(60);

                    // enable scaling and scrolling
                    stepsRateGraph.getViewport().setScalable(true);
                    stepsRateGraph.getViewport().setScalableY(true);
                    stepsRateGraph.addSeries(stepsSeries);



                    calsSeries.appendData(new DataPoint(refreshTime++, daily_cals),true, 60);
                    // set manual X bounds
                    calsRateGraph.getViewport().setYAxisBoundsManual(true);
                    calsRateGraph.getViewport().setMinY(daily_cals - 500);
                    calsRateGraph.getViewport().setMaxY(daily_cals + 500);

                    calsRateGraph.getViewport().setXAxisBoundsManual(true);
                    calsRateGraph.getViewport().setMinX(0);
                    calsRateGraph.getViewport().setMaxX(60);

                    // enable scaling and scrolling
                    calsRateGraph.getViewport().setScalable(true);
                    calsRateGraph.getViewport().setScalableY(true);
                    calsRateGraph.addSeries(calsSeries);



                    if(refreshTime > 60) {
                        refreshTime = 0;
                        heartRateGraph.removeAllSeries();
                        stepsRateGraph.removeAllSeries();
                        calsRateGraph.removeAllSeries();
                    }
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
        mTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        mClient.stopAutoManage(getActivity());
        mClient.disconnect();
    }


    public interface OnFragmentInteractionListener {
    }

}
