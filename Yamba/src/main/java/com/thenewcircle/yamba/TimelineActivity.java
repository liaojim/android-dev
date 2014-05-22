package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import static com.thenewcircle.yamba.YambaApp.*;

public class TimelineActivity extends Activity implements TimelineFragment.DisplayDetails {

    private FrameLayout detailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        FragmentTransaction tx = getFragmentManager().beginTransaction();

        detailsContainer = (FrameLayout) findViewById(R.id.details_fragment_container);
        if(detailsContainer != null) {
            tx.replace(R.id.details_fragment_container, new TimelineDetails(), "details");
        }

        tx.replace(R.id.list_fragment_container, new TimelineFragment(), "list").
                commit();
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
        TimelineDetails details = (TimelineDetails) getFragmentManager().findFragmentByTag("details");
        if(details != null && details.isVisible()) {
            details.updateView(id);
        }
    }

}
