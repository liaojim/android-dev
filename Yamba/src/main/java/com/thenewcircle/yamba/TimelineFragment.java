package com.thenewcircle.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.thenewcircle.yamba.TimelineContract.Columns.*;
/**
 * Created by geoff on 5/22/14.
 */
public class TimelineFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static String[] FROM = {MESSAGE,         USER,          TIME_CREATED };
    public static int[] TO      = {R.id.textStatus, R.id.textUser, R.id.textTimeCreated};
    private SimpleCursorAdapter adapter;

    public interface  DisplayDetails {
        public void showDetails(Long id);
    }

    private SimpleCursorAdapter.ViewBinder rowViewBinder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int i) {
            if(view.getId() == R.id.textTimeCreated) {
                TextView textTimeCreated = (TextView) view;
                Long time = cursor.getLong(i);
                CharSequence friendlyTime = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), 0);
                textTimeCreated.setText(friendlyTime);
                return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.timeline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
        /*
        switch (item.getItemId()) {
            case R.id.refresh:
                Intent refreshIntent = new Intent(getActivity(), TimelineService.class);
                getActivity().startService(refreshIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_timeline, null, FROM, TO,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        adapter.setViewBinder(rowViewBinder);

        getLoaderManager().initLoader(0,null,this);
        setListAdapter(adapter); // This is what I forgot!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), TimelineContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(getActivity() instanceof DisplayDetails) {
            DisplayDetails displayDetails = (DisplayDetails) getActivity();
            displayDetails.showDetails(id);
        }
        super.onListItemClick(l, v, position, id);
    }



}