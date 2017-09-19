package com.example.henrymatidios.wayngalan;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LandingPage extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private Intent myServiceIntent;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
        getUserCredentials();

        Button mAccountButton = (Button) findViewById(R.id.accounts_button);
        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountType.equals("0")){
                    Intent intent = new Intent(v.getContext(), AccountsActivity.class);
                    intent.putExtra("EXTRA_ACCOUNT_TYPE", accountType);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), AccountDetails.class);
                    intent.putExtra("EXTRA_ACCOUNT_TYPE", accountType);
                    startActivity(intent);
                }
            }
        });

        Button mLogsButton = (Button) findViewById(R.id.logs_button);
        mLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LogsActivity.class));
            }
        });

        Button mLogOutButton = (Button) findViewById(R.id.logout_button);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                if (myServiceIntent != null) {
                    stopService(myServiceIntent);
                    Toast.makeText(LandingPage.this, "Service Stopped", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        createService();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void createService() {
        //Start notification service
        ComponentName myServiceComponent = new ComponentName(getApplicationContext(), NotificationService.class);
        myServiceIntent = new Intent(this, NotificationService.class);
        startService(myServiceIntent);

        //Schedule notification service
        JobInfo.Builder mBuilder = new JobInfo.Builder(0, myServiceComponent);
        mBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.schedule(mBuilder.build());
    }

    @SuppressWarnings("unchecked")
    public void getUserCredentials() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            String userID = user.getUid();
            DatabaseReference dbRef = Utils.getDatabase(true).getReference("Accounts");

            dbRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, ?> snapshotValue = (HashMap<String, ?>) dataSnapshot.getValue();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(snapshotValue != null){
                        accountType = (String)snapshotValue.get("type");
                        UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                .setDisplayName(snapshotValue.get("name").toString())
                                .build();
                        if(user != null) {
                            user.updateProfile(userProfile);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
