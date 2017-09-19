package com.example.henrymatidios.wayngalan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.Logs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsActivity extends BaseActivity {

    //Firebase
    DatabaseReference dbRef;

    CustomAdapter adapter;
    List<Logs> mListLogs;
    ListView mListView;
    LinearLayout mPbLinearLayout;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        processIntent(getIntent());

        mListView = (ListView) findViewById(R.id.logs_listView);
        mListLogs = new ArrayList<>();
        mPbLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_logs);
        childEventListener = null;

        getLogs();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText mLocation = (EditText) view.findViewById(R.id.location_editText);
                showMenu(view, mLocation.getText().toString());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbRef.removeEventListener(childEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        dbRef.addChildEventListener(childEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRef.removeEventListener(childEventListener);
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
            Log.e("Error: ", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void getLogs() {
        mListLogs.clear();

        dbRef = Utils.getDatabase(false).getReference("Notification");

        adapter = new CustomAdapter(LogsActivity.this, mListLogs, 0);
        mListView.setAdapter(adapter);

        mPbLinearLayout.setVisibility(View.VISIBLE);

        childEventListener = dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onChildAddedLogs(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeValueFromList(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogsActivity.this, "Database Error: " +databaseError.getCode() , Toast.LENGTH_SHORT).show();
            }

        });
    }

    @SuppressWarnings("unchecked")
    public void onChildAddedLogs(DataSnapshot dataSnapshot) {
        Map<String, String> mLogs = (Map<String, String>) dataSnapshot.getValue();

        if(mLogs != null)
        {
            int image;

            if(mLogs.get("processed").equals("false")) {
                image = R.mipmap.ic_redcircle;
            } else {
                image = R.mipmap.ic_greencircle;
            }

            Logs listLogs = new Logs(dataSnapshot.getKey());
            listLogs.values.setDate(mLogs.get("date"));
            listLogs.values.setLocation(mLogs.get("location"));
            listLogs.values.setTime(mLogs.get("time"));
            listLogs.values.setImage(image);
            mListLogs.add(listLogs);
            ((CustomAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }

        mPbLinearLayout.setVisibility(View.GONE);
    }

    @SuppressWarnings("unchecked")
    public void removeValueFromList(DataSnapshot dataSnapshot) {
        Map<String, String> mLogs = (Map<String, String>) dataSnapshot.getValue();
        String key = dataSnapshot.getKey();

        if(mLogs != null) {
            int image;
            if(mLogs.get("processed").equals("false")) {
                image = R.mipmap.ic_redcircle;
            } else {
                image = R.mipmap.ic_greencircle;
            }
            Logs listLogs = new Logs(dataSnapshot.getKey());
            listLogs.values.setDate(mLogs.get("date"));
            listLogs.values.setLocation(mLogs.get("location"));
            listLogs.values.setTime(mLogs.get("time"));
            listLogs.values.setImage(image);

            mListLogs.remove(listLogs);
        }
        ((CustomAdapter)mListView.getAdapter()).notifyDataSetChanged();
    }

    public void showMenu(final View view, final String location){

        PopupMenu popup = new PopupMenu(LogsActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.logs_popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action:
                        showProgressDialog();
                        markAsRead(view);
                        Intent intent = new Intent(getApplicationContext(), ConsoleSwitch.class);
                        intent.putExtra("EXTRA_CONSOLE_NODE",location);
                        startActivity(intent);
                        break;
                    case R.id.ignore:
                        showProgressDialog();
                        markAsRead(view);
                        Toast.makeText(getApplicationContext(), "Ignore Clicked", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void markAsRead(View view) {

        ImageView mImage = (ImageView) findViewById(R.id.imageView);
        TextView mKey = (TextView) findViewById(R.id.logs_key);
        EditText mLocation = (EditText) findViewById(R.id.location_editText);
        EditText mDate = (EditText) findViewById(R.id.date_editText);
        EditText mTime = (EditText) findViewById(R.id.time_editText);

        mImage.setImageResource(R.mipmap.ic_greencircle);
        String key = mKey.getText().toString();
        String location = mLocation.getText().toString();
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();


        Map<String, String> mObj = new HashMap<>();
        mObj.put("date", date);
        mObj.put("location", location);
        mObj.put("processed", "true");
        mObj.put("time", time);

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/"+key+"/", mObj);

        dbRef.updateChildren(childUpdate, new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
            }
        });
    }

}