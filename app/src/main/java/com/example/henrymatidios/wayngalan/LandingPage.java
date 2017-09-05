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

public class LandingPage extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private Intent myServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        Button mAccountButton = (Button) findViewById(R.id.accounts_button);
        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AccountsActivity.class));
            }
        });

        Button mNotificationButton = (Button) findViewById(R.id.notification_button);
        mNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), NotificationActivity.class));
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
                    Toast.makeText(LandingPage.this, "SERVICE STOPPED", Toast.LENGTH_SHORT).show();
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

    public void getUserCredentials() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null) {
//            String userID = user.getUid();
//        }
    }
}
