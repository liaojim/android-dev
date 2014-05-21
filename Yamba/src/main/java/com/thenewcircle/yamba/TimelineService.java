package com.thenewcircle.yamba;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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

        client = ((YambaApp)getApplication()).getClient();


        //Log.d(TAG,"username="+ userName + " password="+password);


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        List<YambaClient.Status> timeline = null;

        final ContentValues values = new ContentValues();
        final ContentResolver resolver  = getContentResolver();

        try {
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
                    values.put(TIME_CREATED, createdAt.getTime());
                    values.put(USER, user);
                    values.put(MESSAGE, msg);
                    resolver.insert(TimelineContract.CONTENT_URI, values);
                }
            });
        } catch (YambaClientException e) {
            Log.e(TAG,"Unable to get timeline:"+ e.getMessage(),e);

        }

        if (intent != null) {
        }
    }

}
