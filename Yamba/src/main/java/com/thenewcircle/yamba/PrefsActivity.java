package com.thenewcircle.yamba;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class PrefsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        FragmentTransaction tx=getFragmentManager().beginTransaction();
        tx.replace(android.R.id.content, new PrefsFragment());
        tx.commit();
    }

}
