package com.thenewcircle.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StatusActivity extends Activity {

    public static final int MAX_CHARS = 80;
    private EditText editStatus;
    private Button buttonSubmit;
    private TextView charsRemaining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText) findViewById(R.id.editText);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        charsRemaining = (TextView) findViewById(R.id.textMaxChars);

        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                int numLeft = MAX_CHARS - editStatus.length();
                charsRemaining.setText(numLeft +"");

                if (numLeft < 0)
                    editStatus.setTextColor(getResources().getColor(R.color.warning));
                else
                    editStatus.setTextColor(getResources().getColor(R.color.normal));

                buttonSubmit.setEnabled(numLeft > -1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
