package com.example.fred_liu.pulsr.Notification;


import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fred_liu.pulsr.R;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    ListView notificationListView;
    SwipeRefreshLayout swipeRefreshLayout;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        notificationListView = (ListView) view.findViewById(R.id.notificationListView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SpotAdapter spotAdapter = new SpotAdapter(getActivity(), R.layout.fragment_notification_detail, getData());

                notificationListView.setAdapter(spotAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        SpotAdapter spotAdapter = new SpotAdapter(getActivity(), R.layout.fragment_notification_detail, getData());

        notificationListView.setAdapter(spotAdapter);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



            }
        });


        return view;
    }

    private ArrayList<ParkingSpot> getData() {
        final ArrayList<ParkingSpot> parkingSpot = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.Image_parkingSpot);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            parkingSpot.add(new ParkingSpot(bitmap,
                    getResources().getStringArray(R.array.DateList)[i],
                    getResources().getStringArray(R.array.TimeList)[i],
                    getResources().getStringArray(R.array.LocationList)[i],
                    getResources().getStringArray(R.array.HeartRateList)[i],
                    getResources().getStringArray(R.array.StepsList)[i],
                    getResources().getStringArray(R.array.CalsList)[i],
                    getResources().getStringArray(R.array.PlayingList)[i]));
        }
        return parkingSpot;
    }

    public interface OnFragmentInteractionListener {
    }

}
