package com.thenewcircle.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


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
    //private static final int LOCATION_ID = 101;
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
            if (intent.hasExtra(STATUS)) { // post status message
                String status = intent.getStringExtra(STATUS);
                if (intent.hasExtra(LAT)) {
                    Log.d(TAG, "onHandleIntent - there is lat and lon from intent extra with status");
                    Double lat = intent.getDoubleExtra(LAT, 0);
                    Double lon = intent.getDoubleExtra(LON, 0);

                    //NotifyUser("Your current address is: " + getAddressFromLocation(lat, lon));
                    postStatus(status, lat, lon);
                } else {
                    Log.d(TAG, "onHandleIntent - no lat and lon from intent extra");
                    postStatus(status, null, null);
                }
            }else { // get current address through GPS
                if (intent.hasExtra(LAT)) {
                    Log.d(TAG, "onHandleIntent - no status - there is lat and lon from intent extra");
                    Double lat = intent.getDoubleExtra(LAT, 0);
                    Double lon = intent.getDoubleExtra(LON, 0);

                    NotifyUser("Your current address is: " + getAddressFromLocation(lat, lon));
                }

            }
        }
    }

    public String getAddressFromLocation(Double lat, Double lon)  {
        Geocoder geocoder = new Geocoder(this.getBaseContext(), Locale.getDefault());
        String fullAddress = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon,1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);
            fullAddress = address + ", " + city + ", " + country;
            //NotifyUser("Current address: " + fullAddress);
            Log.d(TAG, "getAddressFromLocation - address is " + fullAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAddress;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username=prefs.getString(STUDENT, STUDENT);
        String password=prefs.getString(PASSWORD, PASSWORD);
        client = new YambaClient(username, password);

    }

    public void NotifyUser(String s) {
        Log.d(TAG, "NotifyUser: " + s);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentText(s);
        builder.setContentTitle("Posted");

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID, builder.getNotification());
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
