package com.thenewcircle.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends Activity {

    public static final int MAX_CHARS = 80;
    public static final String TAG="Yamba."+StatusActivity.class.getSimpleName();
    public static final String STATUS = "status";
    private EditText editStatus;
    //private Button buttonSubmit;
    private TextView charsRemaining;
    private MenuItem submitMenu;
    private YambaClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText) findViewById(R.id.editText);
        //buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        charsRemaining = (TextView) findViewById(R.id.textMaxChars);

        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                int numLeft = MAX_CHARS - editStatus.length();
                charsRemaining.setText(numLeft +"");

                //if (numLeft < 0)
                    //submitMenu.setTextColor(getResources().getColor(R.color.warning));
                //else
                    //submitMenu.setTextColor(getResources().getColor(R.color.normal));

                //buttonSubmit.setEnabled(numLeft > -1);
                submitMenu.setEnabled(numLeft > -1 && charSequence.length()>0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        /*buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);

        submitMenu = menu.findItem(R.id.button);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.button:
                // submit;
                Intent statusIntent = new Intent(this, StatusService.class);
                statusIntent.putExtra(STATUS, editStatus.getText().toString());
                startService(statusIntent);
                editStatus.getText().clear();
                return true;
            case R.id.action_settings:
                Intent prefIntent = new Intent(this, PrefsActivity.class);
                startActivity(prefIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postStatus() {
        client = new YambaClient("student", "password");
        String status = editStatus.getText().toString();
        try {

            Log.d(TAG, "posting: " + status);
            client.postStatus(status);

        }catch(YambaClientException e) {
            Log.e(TAG, "Unable to post Status " + status);
        }

    }

}
