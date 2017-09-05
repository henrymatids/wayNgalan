package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.User;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        ListView mListView = (ListView) findViewById(R.id.view_users_listView);

        List<User> mUserList = new ArrayList<>();
        User newUser = new User();

        newUser.setName("HENRY S. MATIDIOS JR.");
        newUser.setType("Admin");
        mUserList.add(newUser);

        CustomAdapter adapter = new CustomAdapter(ViewUsersActivity.this, mUserList, R.mipmap.ic_default_profile_picture);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Choice no."+position, Toast.LENGTH_LONG).show();
            }
        });
    }
}
