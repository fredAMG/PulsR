package com.example.fred_liu.pulsr.Me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;
import com.example.fred_liu.pulsr.model.Response;
import com.example.fred_liu.pulsr.model.User;
import com.example.fred_liu.pulsr.network.NetworkUtil;
import com.example.fred_liu.pulsr.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MeFragment extends Fragment{
    public MeFragment() {
        // Required empty public constructor
    }

    ImageView icon;
    ListView userListView;
    FragmentTransaction fragmentTransaction;

    public static final String TAG = MeFragment.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private Button mBtChangePassword;
    private Button mBtLogout;

    private ProgressBar mProgressbar;

    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;

    private CompositeSubscription mSubscriptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        loadProfile();

        icon = (ImageView) view.findViewById(R.id.icon);
        userListView = (ListView) view.findViewById(R.id.userListView);

        String[] user = getResources().getStringArray(R.array.UserStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, user);

        userListView.setAdapter(adapter);

        icon.setBackgroundResource(R.drawable.ic_me_black_24dp);

        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvEmail = (TextView) view.findViewById(R.id.tv_email);
        mTvDate = (TextView) view.findViewById(R.id.tv_date);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progress);


        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (position == 0) {
                    logout();
                }
                if (position == 1) {
                    showDialog();

                }
                if (position == 2) {

                }
                if (position == 3) {

                }
            }
        });

        return view;
    }

//    private void showDialog() {
//        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        DialogFragment changepasswordFragment = new ChangepasswordFragment();
//        changepasswordFragment.show(fragmentTransaction, "change password");
//    }

    public interface OnFragmentInteractionListener {
    }



    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, new LoginFragment());
        fragmentTransaction.commit();
    }

    private void showDialog(){

        ChangepasswordFragment changepasswordFragment = new ChangepasswordFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        changepasswordFragment.setArguments(bundle);

        changepasswordFragment.show(getFragmentManager(), ChangepasswordFragment.TAG);
    }

    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(User user) {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(getActivity().findViewById(R.id.content),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}