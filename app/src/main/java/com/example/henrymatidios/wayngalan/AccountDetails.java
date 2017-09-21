package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountDetails extends AppCompatActivity {

    private TextView mAccountType;
    private TextView mProfileName;
    private TextView mProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        mAccountType = (TextView) findViewById(R.id.details_account_type);
        mProfileName = (TextView) findViewById(R.id.details_profile_name);
        mProfileEmail = (TextView) findViewById(R.id.profile_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            mProfileName.setText(user.getDisplayName());
            mProfileEmail.setText(user.getEmail());
        }
    }
}
