package com.example.henrymatidios.wayngalan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LandingPage extends AppCompatActivity{

    private FirebaseAuth mAuth;

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
                startActivity(new Intent(view.getContext(), LoginActivity.class));
            }
        });
    }
}
