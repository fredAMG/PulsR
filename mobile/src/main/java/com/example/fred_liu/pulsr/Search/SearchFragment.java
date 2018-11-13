package com.example.fred_liu.pulsr.Search;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fred_liu.pulsr.Notification.ParkingSpot;
import com.example.fred_liu.pulsr.Notification.SpotAdapter;
import com.example.fred_liu.pulsr.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    ListView searchListView;
    SwipeRefreshLayout swipeRefreshLayout2;
    ArrayList<UserDetail> userDetailArrayList = new ArrayList<UserDetail>(1);
    int index = 0;
    UserAdapter userAdapter;
    private String[] d_date = new String[1];
    private String[] d_email = new String[1];
    private String[] d_name = new String[1];

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        searchListView = (ListView) view.findViewById(R.id.searchListView);
        swipeRefreshLayout2 = view.findViewById(R.id.swipeRefresh2);


        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                userAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_detail, userDetailArrayList);
                searchListView.setAdapter(userAdapter);

                swipeRefreshLayout2.setRefreshing(false);
            }
        };
        swipeRefreshLayout2.setOnRefreshListener(onRefreshListener);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



            }
        });

        userDetailArrayList.add(new UserDetail(
                d_name[index] = "fred",
                d_email[index]= "fredliu311@gmail.com",
                d_date[index]= "2018"));

        userAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_detail, userDetailArrayList);
        searchListView.setAdapter(userAdapter);


        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}

