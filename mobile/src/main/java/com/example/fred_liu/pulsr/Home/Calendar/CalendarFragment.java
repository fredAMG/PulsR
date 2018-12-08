package com.example.fred_liu.pulsr.Home.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fred_liu.pulsr.Home.DecoviewDialogFragment;
import com.example.fred_liu.pulsr.Me.ResetpasswordFragment;
import com.example.fred_liu.pulsr.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment{


    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    public static final String TAG = CalendarFragment.class.getSimpleName();

    private String demo = "2018-11-15";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());

        // System.out.println(incrementedDate);


        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        // String[] events = {"run", "10", "50"};
        String incrementedDate = addOneDay(strDate);
        String incrementedDate2 = addOneDay(incrementedDate);

        HomeCollection.date_collection_arr.add(new HomeCollection(demo, "18 mins", "1.5 miles", "200 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(demo), "18 mins", "1.5  miles", "200 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(demo)), "0 mins", "0  miles", "0 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(demo))), "20 mins", "1.75 miles", "440 cals"));
//        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(demo)))), "20 mins", "1.75 miles", "440 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo))))), "0 mins", "0 miles", "0 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo)))))), "20 mins", "1.75 miles", "440 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo))))))), "22 mins", "2.0 miles", "500 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo)))))))), "22 mins", "2.0 miles", "500 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo))))))))), "22 mins", "2.0 miles", "500 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo)))))))))), "22 mins", "2.0 miles", "500 cals"));
        HomeCollection.date_collection_arr.add(new HomeCollection(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(addOneDay(demo))))))))))), "22 mins", "2.0 miles", "500 cals"));

        HomeCollection.date_collection_arr.add(new HomeCollection(strDate, "30 mins", "3.8 miles", "400 cals"));
//        HomeCollection.date_collection_arr.add(new HomeCollection(incrementedDate, "33 mins", "4.0 miles", "410 cals"));
//        HomeCollection.date_collection_arr.add(new HomeCollection(incrementedDate2, "35 mins", "4.26 miles", "440 cals"));



        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(getActivity(), cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) view.findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getContext(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) view.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getContext(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) view.findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, getActivity());
            }

        });


        return view;
    }


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    public static String addOneDay(String date) {
        return LocalDate
                .parse(date)
                .plusDays(3)
                .toString();
    }
}
