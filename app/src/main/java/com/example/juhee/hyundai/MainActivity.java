package com.example.juhee.hyundai;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> mTownAdapter = null;
    ArrayList<String> town_str = null;
    String mStationType;
    Integer mcityPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mListview ;
        ListViewAdapter mAdapter;

        mAdapter = new ListViewAdapter() ;

        mListview = (ListView) findViewById(R.id.stationlistview1);
        mListview.setAdapter(mAdapter);

        final List<String> cityList = new ArrayList<String>(Arrays.asList("강원","경기","경남","경북","광주","대구","대전","부산","서울","울산","인천","전남","전북","제주","충남","충북"));
        String[] car_str=getResources().getStringArray(R.array.carSpinnerArray);
        String[] city_str=getResources().getStringArray(R.array.citySpinnerArray);
        town_str = new ArrayList<String>(Arrays.asList("---구,군 선택---"));

        ArrayAdapter<String> mCarAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,car_str);
        final ArrayAdapter<String> mCityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,city_str);
        mTownAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,town_str);

        Spinner mCarSpinner = (Spinner)findViewById(R.id.vehical);
        final Spinner mCitySpinner = (Spinner)findViewById(R.id.city);
        final Spinner mTownSpinner = (Spinner)findViewById(R.id.town);
        final Button mStationBtn = (Button)findViewById(R.id.searchstation);

        mCarSpinner.setAdapter(mCarAdapter);
        mCitySpinner.setAdapter(mCityAdapter);
        mTownSpinner.setAdapter(mTownAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateandTime = sdf.format(new Date());
        Log.e("seconds~~~~~~~~~~~~~~~~", currentDateandTime);

        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                mcityPosition = position;
                if (position!=0) {
                    ShowTownSpinner mTownSpinner = new ShowTownSpinner();
                    mTownSpinner.execute(cityList.get(position-1));
                }
                else {
                    town_str.clear();
                    town_str.add("---구,군 선택---");
                    mTownAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==4) {
                    mStationType = "상";
                }
                else if ((position==1)||(position==6)) {
                    mStationType = "콤보";
                }
                else {
                    mStationType = "차데모";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        UrlConnection urlconn = new UrlConnection();
                        try {
                            //urlconn.GetSupply(mCitySpinner.getSelectedItem().toString(),mTownSpinner.getSelectedItem().toString(),mStationType);
                            ArrayList<String> mStation = urlconn.GetSupply(cityList.get(mcityPosition-1),mTownSpinner.getSelectedItem().toString(),mStationType);
                            for (int i=0;i<mStation.size();i++) {
                                JSONObject jo= new JSONObject(mStation.get(i));
                                Log.e("******************", String.valueOf(i));
                                Log.e("location",jo.getString("map"));
                                Log.e("holiday",jo.getString("holiday"));
                                Log.e("address",jo.getString("address"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        // 첫 번째 아이템 추가.
        mAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.hyundai),
                "Box", "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        mAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.hyundai),
                "Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        mAdapter.addItem(ContextCompat.getDrawable(this, R.drawable.hyundai),
                "Ind", "Assignment Ind Black 36dp") ;
    }
    private class ShowTownSpinner extends AsyncTask<String, Void, ArrayList<String>> {

        UrlConnection url = new UrlConnection();
        ArrayList<String> items = new ArrayList();


        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                items = url.GetTown(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return items;
        }

        protected void onPostExecute(ArrayList<String> items) {
            town_str.clear();
            town_str.add("---구,군 선택---");
            for (int i =0; i<items.size();i++) {
                town_str.add(items.get(i));
            }
            mTownAdapter.notifyDataSetChanged();
        }
    }
}
