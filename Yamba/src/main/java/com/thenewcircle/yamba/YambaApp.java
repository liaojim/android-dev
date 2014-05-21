package com.thenewcircle.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by jiliao on 5/21/14.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "Yamba."+YambaApp.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String INTERVAL = "interval";
    private AlarmManager alarmManager;
    private Intent timelineIntent;
    private PendingIntent timelinePending;
    private YambaClient client = null;
    //private SharedPreferences prefs;

    private String userName;
    private String password;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        getPreferences();
        setTimer();
    }

    /*public YambaClient getYambaClient(){
        if (client == null) {
            client = new YambaClient(userName, password);

            //Log.d(TAG,"username="+ userName + " password="+password);
        }
        return client;

    }*/


    private void getPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString(USERNAME, "student");
        password = prefs.getString(PASSWORD, "password");
        client = new YambaClient(userName, password);
    }

    public YambaClient getClient() {
        if(client == null) getPreferences();
        return client;
    }

    private void setTimer() {
        Log.d(TAG, "setTimer");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Long interval = Long.valueOf(prefs.getString(INTERVAL, "0"));
        Log.d(TAG, "internval is set to " + interval);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent timelineIntent = new Intent(this, TimelineService.class);
        PendingIntent timelinePending = PendingIntent.getService(this, 0, timelineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(interval == 0) {
            alarmManager.cancel(timelinePending);
        }
        else {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval, interval, timelinePending);
        }

        /*
        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timelineIntent = new Intent(this, TimelineService.class);
        timelinePending = PendingIntent.getService(this, 0, timelineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 30000, 30000, timelinePending); // repeat every 30 seconds
        */
    }

    /*public void stopAlarmManager()  {
        alarmManager.cancel(timelinePending);
    }*/

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
        if(key.equals(PASSWORD) || key.equals(USERNAME)) {
            getPreferences();
        }
        if(key.equals(INTERVAL)) {
            setTimer();
        }
    }
}
