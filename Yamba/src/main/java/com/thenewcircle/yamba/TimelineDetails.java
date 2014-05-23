package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;

/**
 * Created by geoff on 5/22/14.
 */
public class TimelineDetails extends Fragment {

    private static final String TAG = "Yamaba." + TimelineDetails.class.getSimpleName();
    private TextView textUser;
    private TextView textStatus;
    private TextView textTime;

    private Long id = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " );
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_details, container, false);

        textUser =(TextView) layout.findViewById(R.id.textUsername);
        textStatus = (TextView) layout.findViewById(R.id.textStatus);
        textTime = (TextView) layout.findViewById(R.id.textTime);

        Activity activity = getActivity();
        if(activity != null)
            showDetails();

        return layout;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " );
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if(activity != null)
            showDetails();
    }

    public void updateView(Long id) {
        Log.d(TAG, "updateView " + id);

        setId(id);

        Activity activity = getActivity();
       if(activity != null)
            showDetails();
    }

    public void setId(Long id) {
        if (id != null) {
            this.id = id;
        }
    }


    private void showDetails() {
        Log.d(TAG, "showDetails - " + id);
        if (id==null)
            return;

        Uri uri = ContentUris.withAppendedId(TimelineContract.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        if(!cursor.moveToFirst()) {
            Log.e(TAG, "no data for id " + id);
            return;
        }

        String user = cursor.getString(cursor.getColumnIndex(USER));
        String status = cursor.getString(cursor.getColumnIndex(MESSAGE));
        Long createdAt = cursor.getLong(cursor.getColumnIndex(TIME_CREATED));

        textUser.setText(user);
        textStatus.setText(status);
        SimpleDateFormat sdf = new SimpleDateFormat();


        textTime.setText(sdf.format(new Date(createdAt)));
    }
}