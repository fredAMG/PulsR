package com.example.fred_liu.pulsr;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.fred_liu.pulsr.Home.HomeFragment;
import com.example.fred_liu.pulsr.Home.MapFragment;
import com.example.fred_liu.pulsr.Me.ChangepasswordFragment;
import com.example.fred_liu.pulsr.Me.LoginFragment;
import com.example.fred_liu.pulsr.Me.MeFragment;
import com.example.fred_liu.pulsr.Me.RegisterFragment;
import com.example.fred_liu.pulsr.Me.ResetpasswordFragment;
import com.example.fred_liu.pulsr.Notification.NotificationsFragment;
import com.example.fred_liu.pulsr.Search.SearchFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener,
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
    public static final String TAG = MainActivity.class.getSimpleName();
    boolean loginStatus = false;

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
                    if (loginStatus) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, new MeFragment());
                        fragmentTransaction.commit();
                    }
                    else {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, new LoginFragment());
                        fragmentTransaction.commit();
                    }

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: "+data);

        resetpasswordFragment = (ResetpasswordFragment) getSupportFragmentManager().findFragmentByTag(ResetpasswordFragment.TAG);

        if (resetpasswordFragment != null)
            resetpasswordFragment.setToken(data);
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
}
