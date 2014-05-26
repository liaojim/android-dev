package com.thenewcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class StatusService extends IntentService {

    public static final String TAG="Yamba."+StatusService.class.getSimpleName();
    public static final String STATUS = "status";
    public static final String STUDENT = "student";
    public static final String PASSWORD = "password";
    private static final int ID = 100;
    public static final String LAT = "lat";
    public static final String LON = "lon";
    private YambaClient client;

    public StatusService() {
        super("StatusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (intent != null) {
            String status = intent.getStringExtra(STATUS);
            if(intent.hasExtra(LAT)) {
                Log.d(TAG, "onHandleIntent - there is lat and lon from intent extra");
                postStatus(status, intent.getDoubleExtra(LAT, 0), intent.getDoubleExtra(LON, 0));
            }
            else {
                Log.d(TAG, "onHandleIntent - no lat and lon from intent extra");
                postStatus(status, null, null);
            }
        }    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username=prefs.getString(STUDENT, STUDENT);
        String password=prefs.getString(PASSWORD, PASSWORD);
        client = new YambaClient(username, password);

    }

    private void postStatus(String status, Double lat, Double lon) {
        Log.d(TAG, "posting: " + status + " lat:" + lat + " lon:" + lon);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentText(status);
        try {
            YambaClient client = ((YambaApp)getApplication()).getClient();
            if(lat == null) {
                Log.d(TAG, "aclling client.postStatus w/o lat, and lon");
                client.postStatus(status);
            }
            else {
                Log.d(TAG, "aclling client.postStatus witgh lat, and lon " + " lat:" + lat + " lon:" + lon);
                client.postStatus(status, lat, lon);
            }
            builder.setContentTitle("Posted");
        } catch (YambaClientException e) {
            Log.e(TAG, "Unable to post Status " + status, e);
            builder.setContentTitle("Error posting message");
            builder.setContentInfo(e.getMessage());
        }
        finally {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(ID, builder.getNotification());
        }
    }


}
