package com.example.fred_liu.pulsr.Notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fred_liu.pulsr.GCMService;
import com.example.fred_liu.pulsr.MainActivity;
import com.example.fred_liu.pulsr.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationsFragment extends Fragment {

    ListView notificationListView;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final String NOTIFICATION_MESSAGE = "notification_message";
    ArrayList<ParkingSpot> parkingSpots = new ArrayList<ParkingSpot>(1);
    SpotAdapter spotAdapter;
    int index = 0;

    private String[] nDate = new String[1];
    private String[] nTime = new String[1];
    private String[] nLocation = new String[1];
    private String[] nHeart_Rate = new String[1];
    private String[] nSteps = new String[1];
    private String[] nCals = new String[1];
    private String[] nSongs = new String[1];


    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        notificationListView = view.findViewById(R.id.notificationListView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        TypedArray nImgs = getResources().obtainTypedArray(R.array.Image_parkingSpot);
        Bitmap nBitmap = BitmapFactory.decodeResource(getResources(), nImgs.getResourceId(0, 0));
        parkingSpots.add(new ParkingSpot(
                nBitmap,
                nDate[0]= "2018-11-11",
                nTime[0] = "5:00pm",
                nLocation[0]= "Blacksburg",
                nHeart_Rate[0]= "32",
                nSteps[0]= "4.2",
                nCals[0]= "1320",
                nSongs[0]= "Goal accomplished!"));
        spotAdapter = new SpotAdapter(getActivity(), R.layout.fragment_notification_detail, parkingSpots);
        notificationListView.setAdapter(spotAdapter);


        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                spotAdapter = new SpotAdapter(getActivity(), R.layout.fragment_notification_detail, parkingSpots);
                notificationListView.setAdapter(spotAdapter);

                swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



            }
        });

        IntentFilter filter = new IntentFilter(NOTIFICATION_MESSAGE);
        getActivity().registerReceiver(broadcastReceiver, filter);

        return view;
    }

    public interface OnFragmentInteractionListener {
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notification_message = intent.getStringExtra("notification_message");

            TypedArray nImgs = getResources().obtainTypedArray(R.array.Image_parkingSpot);
            Bitmap nBitmap = BitmapFactory.decodeResource(getResources(), nImgs.getResourceId(0, 0));

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormatDate.format(new Date());

            SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
            String now = simpleDateFormatTime.format(calendar.getTime());

            parkingSpots.add(new ParkingSpot(
                    nBitmap,
                    nDate[index]= date,
                    nTime[index] = now,
                    nLocation[index]= "Blacksburg",
                    nHeart_Rate[index]= notification_message,
                    nSteps[index]= notification_message,
                    nCals[index]= notification_message,
                    nSongs[index]= notification_message));

            spotAdapter = new SpotAdapter(getActivity(), R.layout.fragment_notification_detail, parkingSpots);
            notificationListView.setAdapter(spotAdapter);
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            try {
                getActivity().unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
