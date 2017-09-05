package com.example.henrymatidios.wayngalan;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Henry Matidios
 * @since 30/08/2017
 */

public class Utils {
    private static FirebaseDatabase mDatabase;
    private static FirebaseUser user;
    public static FirebaseDatabase getDatabase(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static FirebaseUser getUser() {
        if(user == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        return user;
    }
}
