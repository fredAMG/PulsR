package com.example.fred_liu.pulsr.Me;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class ChangepasswordFragment extends DialogFragment {
    public ChangepasswordFragment() {
        // Required empty public constructor
    }
    Button btn_change_password, btn_cancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);

        btn_change_password = (Button) view.findViewById(R.id.btn_change_password);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);


        btn_change_password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }



    public interface OnFragmentInteractionListener {
    }
}
