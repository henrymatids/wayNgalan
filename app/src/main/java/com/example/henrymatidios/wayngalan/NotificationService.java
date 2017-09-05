package com.example.henrymatidios.wayngalan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class NotificationService extends JobService {

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        return START_NOT_STICKY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onStartJob(JobParameters params) {

        DatabaseReference dbRef = Utils.getDatabase().getReference("Notification");
        dbRef.keepSynced(true);

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> mData = (Map<String, String>) dataSnapshot.getValue();

                if(mData != null)
                {
                    if (mData.get("isOpen").equals("false")) {
                        showNotification();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;

    }

    public void showNotification(){
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_alert)
                .setContentTitle("Gas Leak Alert!")
                .setTicker("Gas Leak Alert!")
                .setAutoCancel(true)
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine("console 1")
//                        .addLine("console 5")
//                        .setBigContentTitle("2 Gas Leak Alert/s")
//                        .setSummaryText("Gas Leak!"))
    //                .setGroup("Gas leak alert")             //newly added  05/09/17
    //                .setGroupSummary(true)                  //newly added  05/09/17
                .setContentText("Hello Notification!");

        Intent notificationIntent = new Intent(this, LogsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }
}
