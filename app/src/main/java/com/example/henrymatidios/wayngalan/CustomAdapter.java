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
import com.example.henrymatidios.wayngalan.models.User;

import java.util.List;

/**
 * @author Henry Matidios
 * @since 15/08/2017
 */

public class CustomAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView mSpinnerItem;
        ImageView mImage;
        EditText mAlertEditText;
        EditText mLocationEditText;
        EditText mDateEditText;
        EditText mTimeEditText;
        TextView mProfileName;
        TextView mProfileType;
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

    CustomAdapter(Context context, List<?> mData, int image) {
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

        ImageView mImage;
        EditText mAlert;
        EditText mDate;
        EditText mLocation;
        EditText mTime;
        TextView mProfileName;
        TextView mProfileType;
        TextView spinnerItem;
        ViewHolder holder;

        holder = new ViewHolder();

        if(convertView == null) {
            if(context instanceof AddNewUserActivity) {
                convertView = layoutInflater.inflate(R.layout.spinner_item, parent, false);

                holder.mSpinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);

                convertView.setTag(holder);

            } else if (context instanceof LogsActivity) {
                convertView = layoutInflater.inflate(R.layout.listview_logs, parent, false);

                holder.mImage = (ImageView) convertView.findViewById(R.id.imageView);
                holder.mAlertEditText = (EditText) convertView.findViewById(R.id.alert_editText);
                holder.mLocationEditText = (EditText) convertView.findViewById(R.id.location_editText);
                holder.mDateEditText = (EditText) convertView.findViewById(R.id.date_editText);
                holder.mTimeEditText = (EditText) convertView.findViewById(R.id.time_editText);

                convertView.setTag(holder);
            } else if (context instanceof ViewUsersActivity) {
                convertView = layoutInflater.inflate(R.layout.listview_view_users, parent, false);

                holder.mImage = (ImageView) convertView.findViewById(R.id.imageView_profile_picture);
                holder.mProfileName = (TextView) convertView.findViewById(R.id.profile_name);
                holder.mProfileType = (TextView) convertView.findViewById(R.id.profile_account_type);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //POPULATE FIELDS
        if(context instanceof AddNewUserActivity) {
            spinnerItem = holder.mSpinnerItem;
            spinnerItem.setText(mData.get(position).toString());

        } else if (context instanceof  LogsActivity) {
            mImage = holder.mImage;
            mAlert = holder.mAlertEditText;
            mLocation = holder.mLocationEditText;
            mDate = holder.mDateEditText;
            mTime = holder.mTimeEditText;

            LogsInfo logs = (LogsInfo) mData.get(position);

            mImage.setImageResource(logs.getImage());
            mAlert.setText(context.getString(R.string.alert_gas_leak));
            mLocation.setText(logs.getLocation());
            mDate.setText(logs.getDate());
            mTime.setText(logs.getTime());
        } else if (context instanceof ViewUsersActivity){
            mImage = holder.mImage;
            mProfileName = holder.mProfileName;
            mProfileType = holder.mProfileType;

            User mUser = (User) mData.get(position);

            mImage.setImageResource(this.mImage);
            mProfileName.setText(mUser.getName());
            mProfileType.setText(mUser.getType());
        }
        return convertView;
    }
}