package com.thenewcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.Date;
import java.util.List;

import static com.marakana.android.yamba.clientlib.YambaClient.TimelineProcessor;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class TimelineService extends IntentService {

    private static final String TAG = "Yamba."+TimelineService.class.getSimpleName();
    private YambaClient client;

    public TimelineService() {
        super("TimelineService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //client = ((YambaApp)getApplication()).getClient();


        //Log.d(TAG,"username="+ userName + " password="+password);


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        List<YambaClient.Status> timeline = null;

        final ContentValues values = new ContentValues();
        final ContentResolver resolver  = getContentResolver();

        Cursor c = resolver.query(TimelineContract.CONTENT_URI, TimelineContract.MAX_TIME_CREATED, null, null, null);
        final long maxTime = c.moveToFirst()?c.getLong(0): Long.MIN_VALUE;


        try {
            client = ((YambaApp)getApplication()).getClient();
            client.fetchFriendsTimeline(new TimelineProcessor(){

                @Override
                public boolean isRunnable() {
                    return true;
                }

                @Override
                public void onStartProcessingTimeline() {
                    Log.d(TAG, "onStartProcessingTimeline");
                }

                @Override
                public void onEndProcessingTimeline() {
                    Log.d(TAG, "onEndProcessingTimeline");
                }

                @Override
                public void onTimelineStatus(long id, Date createdAt, String user, String msg) {
                    Log.d(TAG, "onTimelineStatus " + user + ": " + msg);
                    values.put(ID, id);

                    String notificationMessage=null;
                    String notificationTitle = null;
                    Notification.Builder builder=new Notification.Builder(getBaseContext());
                    builder.setSmallIcon(R.drawable.ic_launcher);

                    long time = createdAt.getTime();
                    if(time > maxTime) {
                        values.put(TIME_CREATED, time);
                        values.put(USER, user);
                        values.put(MESSAGE, msg);
                        resolver.insert(TimelineContract.CONTENT_URI, values);
                        notificationMessage = "ID:" + id + " inserted";
                        notificationTitle = "Data inserted";
                    }
                    else {
                        notificationMessage = "ID:" + id + " already exist in DB";
                        notificationTitle = "DB Insert failed";

                        Log.d(TAG, id + " already inserted");

                    }
                    builder.setContentTitle(notificationTitle);
                    builder.setContentText(notificationMessage);
                    NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(100, builder.getNotification());

                }
            });
        } catch (YambaClientException e) {
            Log.e(TAG,"Unable to get timeline:"+ e.getMessage(),e);

        }
    }

}
