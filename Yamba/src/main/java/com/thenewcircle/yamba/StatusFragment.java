package com.thenewcircle.yamba;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by geoff on 5/23/14.
 */
public class StatusFragment extends Fragment implements LocationListener {

    private static final String TAG = "yamba."+StatusFragment.class.getSimpleName();
    private EditText editStatus;
    private TextView charsRemaining;
    private MenuItem submitItem;
    private Location location=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider = locationManager.getBestProvider(criteria, true);

        locationManager.requestLocationUpdates(provider, 2000, 2, this);
    }

    @Override
    public void onPause() {
        // Turn Location service off.
        super.onPause();
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_status, container, false);
        editStatus = (EditText) layout.findViewById(R.id.editStatus);
        charsRemaining = (TextView) layout.findViewById(R.id.textCharsRemaining);

        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int numLeft = StatusActivity.MAX_CHARS - s.length();
                if(numLeft < 10) {
                    charsRemaining.setTextColor(getResources().getColor(R.color.warning));
                }
                else {
                    charsRemaining.setTextColor(getResources().getColor(R.color.normal));
                }
                charsRemaining.setText(numLeft + "");
                submitItem.setEnabled(s.length() > 0 && numLeft > -1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.status, menu);
        submitItem = menu.findItem(R.id.submit);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.submit:
                Intent statusIntent = new Intent(getActivity(), StatusService.class);
                statusIntent.putExtra(StatusService.STATUS, editStatus.getText().toString());
                if(location != null) {
                    statusIntent.putExtra(StatusService.LAT, location.getLatitude());
                    statusIntent.putExtra(StatusService.LON, location.getLongitude());
                }

                getActivity().startService(statusIntent);
                editStatus.getText().clear();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged " + location.getLatitude() + ", " + location.getLongitude());
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}