package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewUsersActivity extends BaseActivity {

    private CustomAdapter adapter;
    private List<User> mAccountList;
    private ListView mListView;
    private ValueEventListener valueEventListener;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        mListView = (ListView) findViewById(R.id.view_users_listView);

        mAccountList = new ArrayList<>();

        populateAccountListView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               showMenu(view, " ");
        }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRef.removeEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbRef.removeEventListener(valueEventListener);
    }
    @SuppressWarnings("unchecked")
    public void populateAccountListView() {

        showProgressDialog();

       dbRef = Utils.getDatabase(true).getReference("Accounts");
//        dbRef.keepSynced(true);

        valueEventListener = dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAccountList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Map<String, String> mAccounts = (Map<String, String>) childSnapshot.getValue();

                    if(mAccounts != null){
                        User mUser = new User(null, mAccounts.get("type"), mAccounts.get("name"), R.mipmap.ic_default_profile_picture);
                        mAccountList.add(mUser);
                    }
                }

                adapter = new CustomAdapter(ViewUsersActivity.this, mAccountList,0);
                mListView.setAdapter(adapter);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewUsersActivity.this, "Network Error has Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showMenu(View view, final String userID){

        PopupMenu popup = new PopupMenu(ViewUsersActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.view_users_popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        Toast.makeText(ViewUsersActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.details:
                        Toast.makeText(ViewUsersActivity.this, "Details", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
