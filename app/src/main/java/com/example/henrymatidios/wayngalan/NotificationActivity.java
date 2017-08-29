package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ListView mNotificationList = (ListView) findViewById(R.id.notification_list);

        List<String> mNotificationTest = new ArrayList<String>();
        mNotificationTest.add("Logs 1");
        mNotificationTest.add("Logs 2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotificationActivity.this,android.R.layout.simple_selectable_list_item, mNotificationTest);
        mNotificationList.setAdapter(adapter);
    }
}
