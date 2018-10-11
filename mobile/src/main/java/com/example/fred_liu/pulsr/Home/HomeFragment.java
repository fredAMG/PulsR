package com.example.fred_liu.pulsr.Home;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fred_liu.pulsr.R;
import com.google.android.gms.common.ConnectionResult;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;


public class HomeFragment extends Fragment implements OnDataPointListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final int REQUEST_OAUTH = 1;
    private GoogleApiClient mClient;
    private static final String TAG = "HomeFragment";
    private static final String AUTH_PENDING = "auth_state_pending";
    protected boolean authInProgress = false;
    final Calendar currentTime = Calendar.getInstance();
    private long daily_steps;
    TextView textDate, currentHeartRate, currentSteps, currentCals;
    GraphView heartRateGraph, stepsRateGraph, calsRateGraph;
    DatePicker datePicker;
    Switch switch1;


    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mTime;
    private static String mWeek;

    private Timer mTimer;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textDate = view.findViewById(R.id.textDate);
        datePicker = view.findViewById(R.id.datePicker);
        switch1 = view.findViewById(R.id.switch1);

        currentHeartRate =  view.findViewById(R.id.currentHeartRate);
        currentSteps =  view.findViewById(R.id.currentSteps);
        currentCals =  view.findViewById(R.id.currentCals);

        heartRateGraph = view.findViewById(R.id.heartRateGraph);
        stepsRateGraph = view.findViewById(R.id.stepsGraph);
        calsRateGraph = view.findViewById(R.id.calsGraph);


        mYear = String.valueOf(currentTime.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(currentTime.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(currentTime.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mTime = String.valueOf(currentTime.get(Calendar.AM_PM));
        mWeek = String.valueOf(currentTime.get(Calendar.DAY_OF_WEEK));

        textDate.setText(StringData());


        switch1.setChecked(false);
        datePicker.setVisibility(View.GONE);

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch1.isChecked()){
                    datePicker.setVisibility(View.VISIBLE);

                }else{
                    datePicker.setVisibility(View.GONE);
                    mYear = String.valueOf(datePicker.getYear());
                    mMonth = String.valueOf(datePicker.getMonth()+1);
                    mDay = String.valueOf(datePicker.getDayOfMonth());
                    String day = mYear+"-"+mMonth+"-"+mDay;
                    try {
                        mWeek = String.valueOf(dayForWeek(day)+1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textDate.setText(StringData());
                }
            }
        };
        switch1.setOnCheckedChangeListener(onCheckedChangeListener);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .build();


        mTimer = new Timer();

        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat 1/2 minute.

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                VerifyDataTask task1 = new VerifyDataTask();
                task1.execute();
                updateUI();
            }
        },delay,period);

        return view;
    }

    private class VerifyDataTask extends AsyncTask<Void, Void, Void> {
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

    private void updateUI(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentSteps.setText(String.valueOf(daily_steps));

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        DataSourcesRequest dataSourcesRequest = new DataSourcesRequest.Builder()
                .setDataTypes( DataType.TYPE_STEP_COUNT_CUMULATIVE )
                .setDataSourceTypes( DataSource.TYPE_RAW )
                .build();


        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for( DataSource dataSource : dataSourcesResult.getDataSources() ) {
                    if( DataType.TYPE_STEP_COUNT_CUMULATIVE.equals( dataSource.getDataType() ) ) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        Fitness.SensorsApi.findDataSources(mClient, dataSourcesRequest)
                .setResultCallback(dataSourcesResultCallback);
    }


    @Override
    public void onDataPoint(com.google.android.gms.fitness.data.DataPoint dataPoint) {
        for( final Field field : dataPoint.getDataType().getFields() ) {
            final Value value = dataPoint.getValue( field );
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    heartRateGraph = getView().findViewById(R.id.heartRateGraph);
                    stepsRateGraph = getView().findViewById(R.id.stepsGraph);
                    calsRateGraph = getView().findViewById(R.id.calsGraph);


                    LineGraphSeries<DataPoint> heartRateSeries = new LineGraphSeries<>(new DataPoint[] {

                            new DataPoint(0, 61),
                            new DataPoint(1, 61),
                            new DataPoint(2, 62),
                            new DataPoint(3, 61),
                            new DataPoint(4, 61),
                            new DataPoint(5, 61)
                    });

                    LineGraphSeries<DataPoint> stepsSeries = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, 5),
                            new DataPoint(1, 10),
                            new DataPoint(2, 15),
                            new DataPoint(3, 20),
                            new DataPoint(4, 30),
                            new DataPoint(5, 33)
                    });

                    LineGraphSeries<DataPoint> calsSeries = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, 100),
                            new DataPoint(1, 130),
                            new DataPoint(2, 170),
                            new DataPoint(3, 200),
                            new DataPoint(4, 230),
                            new DataPoint(5, 253)
                    });

                    heartRateGraph.addSeries(heartRateSeries);
                    stepsRateGraph.addSeries(stepsSeries);
                    calsRateGraph.addSeries(calsSeries);

                    currentHeartRate =  getView().findViewById(R.id.currentHeartRate);
                    currentHeartRate.setText("Field: " + field.getName() + " Value: " + 62);

//                    currentSteps =  getView().findViewById(R.id.currentSteps);
//                    currentSteps.setText("Field: " + field.getName() + " Value: " + value);
//                    Toast.makeText(getActivity(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();

                    currentCals =  getView().findViewById(R.id.currentCals);
                    currentCals.setText("Field: " + field.getName() + " Value: " + 850);

                }
            });
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_OAUTH ) {
            authInProgress = false;
            if( resultCode == getActivity().RESULT_OK ) {
                if( !mClient.isConnecting() && !mClient.isConnected() ) {
                    mClient.connect();
                }
            } else if( resultCode == getActivity().RESULT_CANCELED ) {
                Log.e( "GoogleFit", "RESULT_CANCELED" );
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {

        SensorRequest request = new SensorRequest.Builder()
                .setDataSource( dataSource )
                .setDataType( dataType )
                .setSamplingRate( 3, TimeUnit.SECONDS )
                .build();

        Fitness.SensorsApi.add(mClient, request, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e("GoogleFit", "SensorApi successfully added");
                        } else {
                            Log.e("GoogleFit", "adding status: " + status.getStatusMessage());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        Fitness.SensorsApi.remove( mClient, this )
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mClient.disconnect();
                        }
                    }
                });
    }


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


    public interface OnFragmentInteractionListener {
    }


    public static String StringData(){

        if("1".equals(mMonth)){
            mMonth ="Jan";
        }else if("2".equals(mMonth)){
            mMonth ="Feb";
        }else if("3".equals(mMonth)){
            mMonth ="Mar";
        }else if("4".equals(mMonth)){
            mMonth ="Apr";
        }else if("5".equals(mMonth)){
            mMonth ="May";
        }else if("6".equals(mMonth)){
            mMonth ="Jun";
        }else if("7".equals(mMonth)){
            mMonth ="Jul";
        }else if("8".equals(mMonth)){
            mMonth ="Aug";
        }else if("9".equals(mMonth)){
            mMonth ="Sep";
        }else if("10".equals(mMonth)){
            mMonth ="Oct";
        }else if("11".equals(mMonth)){
            mMonth ="Nov";
        }else if("12".equals(mMonth)){
            mMonth ="Dec";
        }

        if("8".equals(mWeek)){
            mWeek ="Sun";
        }else if("2".equals(mWeek)){
            mWeek ="Mon";
        }else if("3".equals(mWeek)){
            mWeek ="Tue";
        }else if("4".equals(mWeek)){
            mWeek ="Wed";
        }else if("5".equals(mWeek)){
            mWeek ="Thu";
        }else if("6".equals(mWeek)){
            mWeek ="Fri";
        }else if("7".equals(mWeek)){
            mWeek ="Sat";
        }
        return mYear + " " + mWeek + ", "+ mMonth + " " + mDay+" ";
    }


    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
}
