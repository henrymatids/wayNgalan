package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.Logs;
import com.example.henrymatidios.wayngalan.models.LogsInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsActivity extends BaseActivity {
    //Firebase
    DatabaseReference dbRef;

    CustomAdapter adapter;
    List<LogsInfo> mListLogs;
    ListView mListView;
    Map<String, Logs> mLogMap;
    LinearLayout mPbLinearLayout;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        mListView = (ListView) findViewById(R.id.logs_listView);
        mListLogs = new ArrayList<>();
        mLogMap = new HashMap<>();
        mPbLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_logs);
        valueEventListener = null;

        getLogs();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMenu(view);
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

        dbRef = Utils.getDatabase().getReference("Notification");
        dbRef.keepSynced(true);

        mPbLinearLayout.setVisibility(View.VISIBLE);
        valueEventListener = dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLogMap.clear();
                mListLogs.clear();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Map<String, String> mLogs = (Map<String, String>) childSnapshot.getValue();

                    if(mLogs != null)
                    {
                        LogsInfo mLogsInfo = new LogsInfo(mLogs.get("date"), mLogs.get("time"), mLogs.get("location"));
                        mListLogs.add(mLogsInfo);
                    }

                adapter = new CustomAdapter(LogsActivity.this, mListLogs, R.mipmap.ic_redcircle);

                }
                mListView.setAdapter(adapter);
                mPbLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogsActivity.this, "Database Error: " +databaseError.getCode() , Toast.LENGTH_SHORT).show();
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void showMenu(View view){

        PopupMenu popup = new PopupMenu(LogsActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.logs_popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action:
                        Toast.makeText(getApplicationContext(), "Action Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.ignore:
                        Toast.makeText(getApplicationContext(), "Ignore Clicked", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
