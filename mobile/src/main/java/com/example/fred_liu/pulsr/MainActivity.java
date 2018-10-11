package com.example.fred_liu.pulsr;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.fred_liu.pulsr.Home.HomeFragment;
import com.example.fred_liu.pulsr.Me.LoginFragment;
import com.example.fred_liu.pulsr.Me.MeFragment;
import com.example.fred_liu.pulsr.Notification.NotificationsFragment;
import com.example.fred_liu.pulsr.Search.SearchFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.location.LocationServices;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener{


    FragmentTransaction fragmentTransaction;

    // save files
//    private String mFilePath;
//    private FileInputStream is = null;
//    private  GoogleApiClient mClient;
//    private static final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new HomeFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new SearchFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new NotificationsFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_me:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, new MeFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, new HomeFragment());
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        buildGoogleApiClient();
//        mClient.connect();
    }

//
//    //Important: you have to have the user see the dialog with permission info to enable Google Fit API.
//    // To trigger this dialog we have to try to access Google FIT info inside the activity.
//    private void buildGoogleApiClient() {
//        // Create the Google API Client
//        mClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API) //location services API
//                .addApi(Fitness.RECORDING_API) // Recording fitness info
//                .addApi(Fitness.HISTORY_API) //retrieving fitness results
//                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE)) //permissions
//                .addConnectionCallbacks(  //callbacks inside anonymous listener
//                        new GoogleApiClient.ConnectionCallbacks() {
//
//                            @Override
//                            public void onConnected(Bundle bundle) {
//                                Log.i(TAG, "Connected!!!");
//                                // Now you can make calls to the Fitness APIs.  What to do?
//                                // Subscribe to some data sources!
//                                subscribe();
//
//                                //start service here
//                                //mTaskFragment.startServicesIfNeeded();
//
//                            }
//
//                            @Override
//                            public void onConnectionSuspended(int i) {
//                                // If your connection to the sensor gets lost at some point,
//                                // you'll be able to determine the reason and react to it here.
//                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
//                                    Log.w(TAG, "Connection lost.  Cause: Network Lost.");
//                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
//                                    Log.w(TAG, "Connection lost.  Reason: Service Disconnected");
//                                }
//                            }
//                        }
//                )
//                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(ConnectionResult result) {
//                        Log.w(TAG, "Google Play services connection failed. Cause: " +
//                                result.toString());
//
//                    }
//                })
//                .build();
//    }
//
//    public void subscribe() {
//        // To create a subscription, invoke the Recording API. As soon as the subscription is
//        // active, fitness data will start recording.
//        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        if (status.isSuccess()) {
//                            if (status.getStatusCode()
//                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
//                                Log.i(TAG, "Existing subscription for activity detected.");
//                            } else {
//                                Log.i(TAG, "Successfully subscribed!" + status.getStatusCode());
//                            }
//                        } else {
//                            Log.w(TAG, "There was a problem subscribing.");
//                        }
//                    }
//                });
//    }
//
//    DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
//            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//            .setType(DataSource.TYPE_DERIVED)
//            .setStreamName("estimated_steps")
//            .setAppPackageName("com.google.android.gms")
//            .build();
//
//    // Setting a start and end date using a range of 1 week before this moment.
//    Calendar cal = Calendar.getInstance();
//    Date now = new Date();
//    cal.setTime(now);
//    long endTime = cal.getTimeInMillis();
//    cal.add(Calendar.WEEK_OF_YEAR, -1);
//    long startTime = cal.getTimeInMillis();
//
//    java.text.DateFormat dateFormat = getDateInstance();
//    Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
//    Log.i(TAG, "Range End: " + dateFormat.format(endTime));

//    DataReadRequest readRequest =
//            new DataReadRequest.Builder()
//                    // The data request can specify multiple data types to return, effectively
//                    // combining multiple data queries into one call.
//                    // In this example, it's very unlikely that the request is for several hundred
//                    // datapoints each consisting of a few steps and a timestamp.  The more likely
//                    // scenario is wanting to see how many steps were walked per day, for 7 days.
//                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                    // Analogous to a "Group By" in SQL, defines how data should be aggregated.
//                    // bucketByTime allows for a time span, whereas bucketBySession would allow
//                    // bucketing by "sessions", which would need to be defined in code.
//                    .bucketByTime(1, TimeUnit.DAYS)
//                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                    .build();
//
//    private class VerifyDataTask extends AsyncTask<Void, Void, Void> {
//        protected Void doInBackground(Void... params) {
//
//            long total = 0;
//
//            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
//            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
//            if (totalResult.getStatus().isSuccess()) {
//                DataSet totalSet = totalResult.getTotal();
//                total = totalSet.isEmpty()
//                        ? 0
//                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//            } else {
//                Log.w(TAG, "There was a problem getting the step count.");
//            }
//
//            Log.i(TAG, "Total steps: " + total);
//
//            return null;
//        }
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
