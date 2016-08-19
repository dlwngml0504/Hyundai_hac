package com.example.juhee.hyundai;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> mTownAdapter = null;
    ArrayList<String> town_str = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mCarSpinner.setAdapter(mCarAdapter);
        mCitySpinner.setAdapter(mCityAdapter);
        mTownSpinner.setAdapter(mTownAdapter);

        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (position!=0) {
                    ShowTownSpinner mTownSpinner = new ShowTownSpinner();
                    mTownSpinner.execute(cityList.get(position-1));
                    /*final UrlConnection urlconn = new UrlConnection();
                    new Thread() {
                        public void run () {
                            try {
                                ArrayList<String> mTownList = null;
                                mTownList = (ArrayList<String>) urlconn.GetTown(cityList.get(position-1));
                                town_str.clear();
                                town_str.add("---구,군 선택---");
                                for (int i =0; i<mTownList.size();i++) {
                                    Log.e("*********",mTownList.get(i));
                                    town_str.add(mTownList.get(i));
                                }
                                //mTownAdapter.notifyDataSetChanged();
                                Log.e("++++++++++++++",town_str.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
*/
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

        mTownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if (position!=0) {
                    Log.e("@@@@@@@@",mTownSpinner.getSelectedItem().toString());
                    UrlConnection urlconn = new UrlConnection();
                    try {
                        ArrayList<String> mTownList = null;
                        mTownList = (ArrayList<String>) urlconn.GetTown(mCitySpinner.getSelectedItem().toString());
                        Log.e("*********",mTownList.toString());
                        town_str.clear();
                        town_str.add("---구,군 선택---");
                        for (int i =0; i<mTownList.size();i++) {
                            Log.e("*********",mTownList.get(i));
                            town_str.add(mTownList.get(i));
                        }
                        mTownAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    town_str.clear();
                    town_str.add("---구,군 선택---");
                    mTownAdapter.notifyDataSetChanged();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jo = new JSONObject();
                int type;
                if (position==4) {
                    type = 1;
                }
                else if ((position==1)||(position==6)) {
                    type = 2;
                }
                else {
                    type = 3;
                }

                try {
                    jo.put("province",mCitySpinner.getSelectedItem().toString()+" "+mTownSpinner.getSelectedItem().toString());
                    jo.put("type",type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("@@@@@@@@@@",jo.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
