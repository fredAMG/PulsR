package com.example.fred_liu.pulsr.Notification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;

import java.util.ArrayList;

/**
 * Created by fredliu on 12/3/17.
 */

public class SpotAdapter extends ArrayAdapter<ParkingSpot> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ParkingSpot> data = new ArrayList<ParkingSpot>();

    public SpotAdapter(Context context, int layoutResourceId, ArrayList<ParkingSpot> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.date = (TextView) row.findViewById(R.id.date);
            holder.time = (TextView) row.findViewById(R.id.time);
            holder.location = (TextView) row.findViewById(R.id.location);
            holder.heartRate = (TextView) row.findViewById(R.id.heartRate);
            holder.steps = (TextView) row.findViewById(R.id.steps);
            holder.cals = (TextView) row.findViewById(R.id.cals);
            holder.playing = (TextView) row.findViewById(R.id.playing);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ParkingSpot item = data.get(position);
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.location.setText(item.getLocation());
        holder.heartRate.setText(item.getHeartRate());
        holder.steps.setText(item.getSteps());
        holder.cals.setText(item.getCals());
        holder.playing.setText(item.getPlaying());

        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    class ViewHolder {
        TextView date;
        TextView time;
        TextView location;
        TextView heartRate;
        TextView steps;
        TextView cals;
        TextView playing;

        ImageView image;
    }

}
