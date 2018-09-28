package com.example.fred_liu.pulsr;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.fred_liu.pulsr.Home.HomeFragment;
import com.example.fred_liu.pulsr.Me.LoginFragment;
import com.example.fred_liu.pulsr.Me.MeFragment;
import com.example.fred_liu.pulsr.Notification.NotificationFragment;
import com.example.fred_liu.pulsr.Search.SearchFragment;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener{


    FragmentTransaction fragmentTransaction;

    // save files
    private String mFilePath;
    private FileInputStream is = null;

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
                    fragmentTransaction.replace(R.id.content, new NotificationFragment());
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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
