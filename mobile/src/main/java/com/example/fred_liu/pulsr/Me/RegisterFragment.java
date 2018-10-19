package com.example.fred_liu.pulsr.Me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;


public class RegisterFragment extends Fragment {
    public RegisterFragment() {
        // Required empty public constructor
    }
    Button btn_register;
    TextView tv_login;
    FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btn_register = (Button) view.findViewById(R.id.btn_register);
        tv_login = (TextView) view.findViewById(R.id.tv_login);




        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, new LoginFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}
