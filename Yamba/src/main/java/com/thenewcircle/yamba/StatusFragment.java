package com.thenewcircle.yamba;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
public class StatusFragment extends Fragment {

    private EditText editStatus;
    private TextView charsRemaining;
    private MenuItem submitItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                getActivity().startService(statusIntent);
                editStatus.getText().clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}