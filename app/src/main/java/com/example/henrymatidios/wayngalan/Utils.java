package com.example.henrymatidios.wayngalan;

import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Henry Matidios
 * @since 30/08/2017
 */

public class Utils {
    private static FirebaseDatabase mDatabase;
    public static FirebaseDatabase getDatabase(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
