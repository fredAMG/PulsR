package com.example.fred_liu.pulsr.Me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;


public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }
    Button btn_login;
    TextView tv_register, tv_forgot_password;
    FragmentTransaction fragmentTransaction;
    EditText et_email, et_password;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btn_login = (Button) view.findViewById(R.id.btn_login);
        tv_register = (TextView) view.findViewById(R.id.tv_register);
        tv_forgot_password = (TextView) view.findViewById(R.id.tv_forgot_password);



        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, new MeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });




        return view;
    }

    private void showDialog() {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DialogFragment changepasswordFragment = new ChangepasswordFragment();
        changepasswordFragment.show(fragmentTransaction, "change password");
    }

    public interface OnFragmentInteractionListener {
    }
}
