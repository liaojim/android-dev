package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;

import static com.thenewcircle.yamba.YambaApp.*;

public class TimelineActivity extends Activity implements TimelineFragment.DisplayDetails {

    public static final String ID = "id";
    private static final String TAG = "yamba." + TimelineActivity.class.getSimpleName();
    public static final String DETAILS = "details";
    public static final String LIST = "list";
    private FrameLayout detailsContainer;
    private FrameLayout listContainer;
    private Long id = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && id != null) {
            outState.putLong(ID, id.longValue());
            Log.d(TAG, "onSaveInstanceState id saved "+id);

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            Long id =  savedInstanceState.getLong(ID);
            Log.d(TAG, "onRestoreInstanceState id restored "+id);
            this.id = id;
            if (id!=null)   {
                showDetails(id);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);


        FragmentTransaction tx = getFragmentManager().beginTransaction();

        int orientation = getDeviceOrientation();

        detailsContainer = (FrameLayout) findViewById(R.id.details_fragment_container);
        if(detailsContainer != null ) {
            tx.replace(R.id.details_fragment_container, new TimelineDetails(), DETAILS);
            tx.addToBackStack(DETAILS);
        }

        tx.replace(R.id.list_fragment_container, new TimelineFragment(), LIST);
        tx.commit();
    }

    private int getDeviceOrientation() {
        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        int rotation = getWindow().getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation ==  Surface.ROTATION_270) {
            // landscape mode
            orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        return orientation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.timeline_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
            case R.id.status:
                Intent statusIntent = new Intent(this, StatusActivity.class);
                startActivity(statusIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void showDetails(Long id) {
        Log.d(TAG,"showDetials() for id " + id );

        if (id==null)
            return;

        this.id = id;

        FragmentManager fragmentManager = getFragmentManager();
        //TimelineDetails details = (TimelineDetails) fragmentManager.findFragmentByTag(DETAILS);
        TimelineDetails details = (TimelineDetails) fragmentManager.findFragmentById(R.id.details_fragment_container);

        if(details != null) {
            details.setId(id);
            Log.d(TAG,"showDetials() - details is not null");
        }else {
            Log.d(TAG,"showDetials() - details is null");

        }

        int orientation = getDeviceOrientation();
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Log.d(TAG,"showDetials() - Portrait");

            if(details != null && details.isVisible()) {
                // portiait
                details.updateView(id);
            }else {
                // portrait - replace the list fragment with detail fragment.
                FragmentTransaction tx = fragmentManager.beginTransaction();
                TimelineDetails timelineDetails = new TimelineDetails();
                tx.replace(R.id.list_fragment_container, timelineDetails, DETAILS);
                tx.addToBackStack("Details");
                tx.commit();
                timelineDetails.updateView(id);
            }
        }else {
            // landsape
            Log.d(TAG,"showDetials() - landscape");
            if(details != null /*&& details.isVisible()*/) {
                details.updateView(id);
            }

        }
        Log.d(TAG,"showDetials() - Done");
    }

}
