package com.example.fred_liu.pulsr.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWeek;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textDate = view.findViewById(R.id.textDate);
        final DatePicker datePicker = view.findViewById(R.id.datePicker);
        final Switch switch1 = view.findViewById(R.id.switch1);



        final Calendar c = Calendar.getInstance();
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        textDate.setText(StringData());


        switch1.setChecked(false);
        datePicker.setVisibility(View.GONE);

        final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch1.isChecked()){
                    datePicker.setVisibility(View.VISIBLE);

                }else{
                    datePicker.setVisibility(View.GONE);
                    mYear = String.valueOf(datePicker.getYear());
                    mMonth = String.valueOf(datePicker.getMonth()+1);
                    mDay = String.valueOf(datePicker.getDayOfMonth());
                    String day = mYear+"-"+mMonth+"-"+mDay;
                    try {
                        mWeek = String.valueOf(dayForWeek(day)+1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textDate.setText(StringData());
                }
            }
        };
        switch1.setOnCheckedChangeListener(onCheckedChangeListener);



        return view;
    }

    public interface OnFragmentInteractionListener {
    }


    public static String StringData(){

        if("1".equals(mMonth)){
            mMonth ="Jan";
        }else if("2".equals(mMonth)){
            mMonth ="Feb";
        }else if("3".equals(mMonth)){
            mMonth ="Mar";
        }else if("4".equals(mMonth)){
            mMonth ="Apr";
        }else if("5".equals(mMonth)){
            mMonth ="May";
        }else if("6".equals(mMonth)){
            mMonth ="Jun";
        }else if("7".equals(mMonth)){
            mMonth ="Jul";
        }else if("8".equals(mMonth)){
            mMonth ="Aug";
        }else if("9".equals(mMonth)){
            mMonth ="Sep";
        }else if("10".equals(mMonth)){
            mMonth ="Oct";
        }else if("11".equals(mMonth)){
            mMonth ="Nov";
        }else if("12".equals(mMonth)){
            mMonth ="Dec";
        }

        if("8".equals(mWeek)){
            mWeek ="Sun";
        }else if("2".equals(mWeek)){
            mWeek ="Mon";
        }else if("3".equals(mWeek)){
            mWeek ="Tue";
        }else if("4".equals(mWeek)){
            mWeek ="Wed";
        }else if("5".equals(mWeek)){
            mWeek ="Thu";
        }else if("6".equals(mWeek)){
            mWeek ="Fri";
        }else if("7".equals(mWeek)){
            mWeek ="Sat";
        }
        return mYear + " " + mWeek + ", "+ mMonth + " " + mDay+" ";
    }


    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
}
