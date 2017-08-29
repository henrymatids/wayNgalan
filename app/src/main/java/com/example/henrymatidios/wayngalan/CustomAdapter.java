package com.example.henrymatidios.wayngalan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.henrymatidios.wayngalan.models.LogsInfo;

import java.util.List;

/**
 * Created by Henry Matidios on 15/08/2017.
 */

public class CustomAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView mSpinnerItem;
        ImageView mNotificationImage;
        EditText mAlertEditText;
        EditText mLocationEditText;
        EditText mDateEditText;
        EditText mTimeEditText;
    }
    private Context context;
    private List<?> mData;
    private LayoutInflater layoutInflater;
    private int mImage;

    CustomAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    CustomAdapter(Context context, List<LogsInfo> mData, int image) {
        this.context = context;
        this.mData = mData;
        this.mImage = image;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        TextView spinnerItem;
        ImageView mImage;
        EditText mAlert;
        EditText mLocation;
        EditText mDate;
        EditText mTime;

        holder = new ViewHolder();

        if(convertView == null) {
            if(context instanceof AddNewUserActivity) {
                convertView = layoutInflater.inflate(R.layout.spinner_item, parent, false);

                holder.mSpinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);

                convertView.setTag(holder);

            } else if (context instanceof LogsActivity) {
                convertView = layoutInflater.inflate(R.layout.listview_logs, parent, false);
                holder = new ViewHolder();

                holder.mNotificationImage = (ImageView) convertView.findViewById(R.id.imageView);
                holder.mAlertEditText = (EditText) convertView.findViewById(R.id.alert_editText);
                holder.mLocationEditText = (EditText) convertView.findViewById(R.id.location_editText);
                holder.mDateEditText = (EditText) convertView.findViewById(R.id.date_editText);
                holder.mTimeEditText = (EditText) convertView.findViewById(R.id.time_editText);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(context instanceof AddNewUserActivity) {
            spinnerItem = holder.mSpinnerItem;
            spinnerItem.setText(mData.get(position).toString());

        } else if (context instanceof  LogsActivity) {
            mImage = holder.mNotificationImage;
            mAlert = holder.mAlertEditText;
            mLocation = holder.mLocationEditText;
            mDate = holder.mDateEditText;
            mTime = holder.mTimeEditText;

            LogsInfo logs = (LogsInfo) mData.get(position);

            mImage.setImageResource(this.mImage);
            mAlert.setText(context.getString(R.string.alert_gas_leak));
            mLocation.setText(logs.getLocation());
            mDate.setText(logs.getDate());
            mTime.setText(logs.getTime());
        }
        return convertView;
    }
}
