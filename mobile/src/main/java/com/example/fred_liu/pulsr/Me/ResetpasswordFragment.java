package com.example.fred_liu.pulsr.Me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;


public class ResetpasswordFragment  extends DialogFragment {
    public ResetpasswordFragment() {
        // Required empty public constructor
    }
    Button btn_reset_password;
    TextView tv_message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resetpassword, container, false);

        btn_reset_password = (Button) view.findViewById(R.id.btn_reset_password);



        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });



        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}
