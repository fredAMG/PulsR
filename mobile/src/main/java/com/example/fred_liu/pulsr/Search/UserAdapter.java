package com.example.fred_liu.pulsr.Search;

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

public class UserAdapter extends ArrayAdapter<UserDetail> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<UserDetail> data = new ArrayList<UserDetail>();

    public UserAdapter(Context context, int layoutResourceId, ArrayList<UserDetail> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserAdapter.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserAdapter.ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.d_name);
            holder.email = (TextView) row.findViewById(R.id.d_email);
            holder.date = (TextView) row.findViewById(R.id.d_date);
            row.setTag(holder);
        } else {
            holder = (UserAdapter.ViewHolder) row.getTag();
        }


        UserDetail item = data.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.date.setText(item.getDate());
        return row;
    }

    class ViewHolder {
        TextView name;
        TextView email;
        TextView date;

    }
}
