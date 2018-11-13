package com.example.fred_liu.pulsr;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fred_liu.pulsr.Home.Calendar.CalendarFragment;
import com.example.fred_liu.pulsr.Home.DecoviewFragment;
import com.example.fred_liu.pulsr.Home.HomeFragment;
import com.example.fred_liu.pulsr.Home.MapFragment;
import com.example.fred_liu.pulsr.Me.ChangepasswordFragment;
import com.example.fred_liu.pulsr.Me.LoginFragment;
import com.example.fred_liu.pulsr.Me.MeFragment;
import com.example.fred_liu.pulsr.Me.RegisterFragment;
import com.example.fred_liu.pulsr.Me.ResetpasswordFragment;
import com.example.fred_liu.pulsr.Notification.NotificationsFragment;
import com.example.fred_liu.pulsr.Search.SearchFragment;
import com.example.fred_liu.pulsr.Timer.TimerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmListenerService;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener,
        DecoviewFragment.OnFragmentInteractionListener,
        ChangepasswordFragment.Listener,
        ResetpasswordFragment.Listener,
        MapFragment.OnMyLocationButtonClickListener,
        MapFragment.OnMapReadyCallBack,
        MapFragment.LocationListener{


    FragmentTransaction fragmentTransaction;
    ResetpasswordFragment resetpasswordFragment;

    // save files
//    private String mFilePath;
//    private FileInputStream is = null;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String REGISTRATION_PROCESS = "registration";
    public static final String MESSAGE_RECEIVED = "message_received";


    public static final String TAG = MainActivity.class.getSimpleName();
    public int loginStatus = 0;
    public int deviceStatus = 0;

    public BottomNavigationView bottomNavigationView;
    private MenuItem notification_switch;
    private static int notification_on_off = 1;


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

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        receiveData();
        registerReceiver();
        if(getIntent().getStringExtra("message") != null) {
            if(getIntent().getStringExtra("message").equals("loginStatus_from_DistanceActivity")) {
                loginStatus = getIntent().getIntExtra("loginStatus",0);
            }
        }

        if (loginStatus == 1) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, new HomeFragment());
            fragmentTransaction.commit();
        }
        else {
            if(deviceStatus == 0){
                registerDevice();
            }

            bottomNavigationView.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, new LoginFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        notification_switch = menu.findItem(R.id.notification_switch);
        if(getNotification_on_off() == 1){
            notification_switch.setIcon(R.drawable.ic_do_not_disturb_black_24dp);
        }
        else {
            notification_switch.setIcon(R.drawable.ic_notifications_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification_switch) {
            if(getNotification_on_off() == 1){
                notification_on_off = 0;
                notification_switch.setIcon(R.drawable.ic_notifications_24dp);
                Toast.makeText(this, "Notification turned off!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                notification_on_off = 1;
                notification_switch.setIcon(R.drawable.ic_do_not_disturb_black_24dp);
                Toast.makeText(this, "Notification turned on!",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static int getNotification_on_off()
    {
        return notification_on_off;
    }

    public void registerDevice() {
        if (checkPlayServices()) {

            startRegisterProcess();
        }
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getAction() != null){
                if(intent.getAction().equals(MESSAGE_RECEIVED)){
                    String message = intent.getStringExtra("message");
                    showAlertDialog(message);
                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String token  = intent.getStringExtra("token");
        Log.d(TAG, "onNewIntent: "+token);

        resetpasswordFragment = (ResetpasswordFragment) getSupportFragmentManager().findFragmentByTag(ResetpasswordFragment.TAG);

        if (resetpasswordFragment != null)
            resetpasswordFragment.setToken(token);
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.container),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onPasswordChanged() {
        showSnackBarMessage("Password Changed Successfully !");
    }

    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        loginStatus = i.getIntExtra("Login_Status", 0);
        deviceStatus = i.getIntExtra("Device_Status", 0);
    }

    public void startRegisterProcess(){

        if(checkPermission()){

            startRegisterService();

        } else {

            requestPermission();
        }

    }

    private void startRegisterService(){

        Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
        intent.putExtra("DEVICE_ID", getDeviceId());
        intent.putExtra("DEVICE_NAME",getDeviceName());
        startService(intent);
    }

    public void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(REGISTRATION_PROCESS);
        intentFilter.addAction(MESSAGE_RECEIVED);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    public void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PulsR Message Received !");
        builder.setMessage(message);
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

        Intent intent = new Intent(NotificationsFragment.NOTIFICATION_MESSAGE);
        intent.putExtra("notification_message",message);
        sendBroadcast(intent);
    }

    private String getDeviceId(){

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private String getDeviceName(){
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        return  deviceMan + " " +deviceName;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(REGISTRATION_PROCESS)){

                String result  = intent.getStringExtra("result");
                String message = intent.getStringExtra("message");
                Log.d(TAG, "onReceive: "+result+message);
                Snackbar.make(findViewById(R.id.container),result + " : " + message,Snackbar.LENGTH_SHORT).show();
            } else if (intent.getAction().equals(MESSAGE_RECEIVED)){

                String message = intent.getStringExtra("message");
                showAlertDialog(message);
            }
        }
    };

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startRegisterService();

                } else {

                    Snackbar.make(findViewById(R.id.container),"Permission Denied, Please allow to proceed !.",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
