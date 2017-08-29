package com.example.henrymatidios.wayngalan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountsActivity extends AppCompatActivity {

    //Firebase
    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        //Instantiation of Firebase Variables;
//        db = FirebaseDatabase.getInstance();
//        myRef = db.getInstance().getReference("Users");

        Button mViewUsers = (Button) findViewById(R.id.view_user_button);
        Button mAddNewUser = (Button) findViewById(R.id.add_new_user_button);

        mViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String uid;
//                if(user != null){
//                    uid = user.getUid();
//                    Toast.makeText(AccountsActivity.this, uid,Toast.LENGTH_LONG).show();
//                }
            }
        });

        mAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),AddNewUserActivity.class));
            }
        });
    }

    public void authenticateAccount() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        dbRef.child("Users").child(task.getResult().getUser().getUid()).setValue(mapType);
    }

    public void getAccountType(){

    }
}
