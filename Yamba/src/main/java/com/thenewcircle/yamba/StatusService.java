package com.thenewcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
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
    private YambaClient client;

    public StatusService() {
        super("StatusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntest()");
        if (intent != null) {
            String status =intent.getStringExtra(STATUS);
            postStatus(status);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        client = new YambaClient(STUDENT, PASSWORD);

    }

    private void postStatus(String status) {
        Log.d(TAG, "postStatus()");

        Notification.Builder builder=new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentText(status);

        try {
            client.postStatus(status);

        }catch (YambaClientException e) {
            Log.e(TAG, "Unable t post status " + status, e);
            //notify user of failure
            builder.setContentTitle("error posting message");
            builder.setContentInfo(e.getMessage());
        }finally {
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(100, builder.getNotification());

        }

    }

}
