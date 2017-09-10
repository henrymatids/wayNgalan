package com.example.henrymatidios.wayngalan;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        processIntent(getIntent());

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

    @Override
    public void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    public void processIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            if(extras.getBoolean("EXTRA_NOTIFICATION_CLICKED")) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(LogsActivity.this, LoginActivity.class));
                    Toast.makeText(this, "You need to sign in first.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch(Exception e) {
            Toast.makeText(this, "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                        int image;
                        if(mLogs.get("processed").equals("false")) {
                            image = R.mipmap.ic_redcircle;
                        } else {
                            image = R.mipmap.ic_greencircle;
                        }
                        LogsInfo mLogsInfo = new LogsInfo(mLogs.get("date"), mLogs.get("time"), mLogs.get("location"), image);
                        mListLogs.add(mLogsInfo);
                    }
                }
                adapter = new CustomAdapter(LogsActivity.this, mListLogs, 0);
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