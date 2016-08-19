package com.example.juhee.hyundai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchNearStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_near_station);

        String[] car_str = getResources().getStringArray(R.array.carSpinnerArray);
        ArrayAdapter<String> mCarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, car_str);
        Spinner mCarSpinner = (Spinner) findViewById(R.id.vehical);
        mCarSpinner.setAdapter(mCarAdapter);


        mCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jo = new JSONObject();
                int type;
                if (position == 4) {
                    type = 1;
                } else if ((position == 1) || (position == 6)) {
                    type = 2;
                } else {
                    type = 3;
                }

                try {
                    //jo.put("province",mCitySpinner.getSelectedItem().toString()+" "+mTownSpinner.getSelectedItem().toString());
                    jo.put("type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
