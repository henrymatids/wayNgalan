package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.Logs;
import com.example.henrymatidios.wayngalan.models.LogsInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsActivity extends BaseActivity {
    //Firebase
    FirebaseDatabase database;
    DatabaseReference dbRef;

    List<LogsInfo> mListLogs;
    ListView mListView;
    Map<String, Logs> mLogMap;
    LinearLayout mPbLinearLayout;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Notification");

        mListView = (ListView) findViewById(R.id.logs_listView);
        mListLogs = new ArrayList<>();
        mLogMap = new HashMap<>();
        mPbLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_logs);
        valueEventListener = null;

        getLogs();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LogsActivity.this,"Item no. "+position,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbRef.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("unchecked")
    public void getLogs() {
        mPbLinearLayout.setVisibility(View.VISIBLE);
        valueEventListener = dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLogMap.clear();
                mListLogs.clear();

                if(mLogMap != null) {
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        Map<String, String> mLogs = (Map<String, String>) childSnapshot.getValue();

                        if(mLogs != null)
                        {
                            LogsInfo mLogsInfo = new LogsInfo(mLogs.get("date"), mLogs.get("time"), mLogs.get("location"));
                            mListLogs.add(mLogsInfo);
                        }
                    }

                    CustomAdapter adapter = new CustomAdapter(LogsActivity.this, mListLogs, R.mipmap.ic_redcircle);
                    mListView.setAdapter(adapter);
                }
                mPbLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogsActivity.this, "Database Error: " +databaseError.getCode() , Toast.LENGTH_SHORT).show();
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
