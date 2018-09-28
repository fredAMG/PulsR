package com.example.fred_liu.pulsr.Me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.fred_liu.pulsr.R;


public class MeFragment extends Fragment {
    public MeFragment() {
        // Required empty public constructor
    }

    ImageView icon;
    ListView userListView;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        icon = (ImageView) view.findViewById(R.id.icon);
        userListView = (ListView) view.findViewById(R.id.userListView);

        String[] user = getResources().getStringArray(R.array.UserStatusOut);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, user);

        userListView.setAdapter(adapter);

        icon.setBackgroundResource(R.drawable.ic_me_black_24dp);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (position == 0) {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    LoginFragment loginFragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.content, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                if (position == 1) {

                }
                if (position == 2) {

                }
                if (position == 3) {

                }
                if (position == 4) {
//                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    HistoryFragment historyFragment = new HistoryFragment();
//                    fragmentTransaction.replace(R.id.content, historyFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                }
                if (position == 5) {

                }


            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}