package com.pg_monitor.client.sensoraggrsys;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static String upload_time=null;
    public static int min_interval=-1;

    public static HashMap<String,Location> id_location_map=new HashMap<String,Location>();

    private static String url_construct= "http://52.69.84.252/ondotori_load_data.php";
    private static String base_url_update = "http://52.69.84.252/ondotori_update_data.php";

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    public static Toast sToast=null;
    public static ProgressDialog pDialog;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        if(MainActivity.upload_time==null){
            new LoadData_in().execute();
        }


        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.temp_icon)
                        .setContentDescription(R.string.ondotori_temp)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.humid_icon)
                        .setContentDescription(R.string.ondotori_humid)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.co2_icon)
                        .setContentDescription(R.string.ondotori_co2)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.motion_icon)
                        .setContentDescription(R.string.motion)
                        .setTabListener(this));


    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {


        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new TempTab();
                case 1:
                    return new HumidTab();
                case 2:
                    return new CO2Tab();
                case 3:
                    return new MotionTab();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new UpdateData_in().execute();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    class LoadData_in extends AsyncTask<String, String, String> {

        JSONParser jParser=new JSONParser();

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_TEMPERATURE = "temp";
        private static final String TAG_HUMIDITY = "humid";
        private static final String TAG_CO2 ="co2";
        private static final String TAG_AVG="avg";
        private static final String TAG_MIN="min";
        private static final String TAG_MAX="max";
        private static final String TAG_LAST_UPDATE_TIME = "last_update_time";

        private static final String TAG_UPLOAD_TIME="upload_time";
        private static final String TAG_MIN_INTERVAL = "min_interval";
        private static final String TAG_TIME_INDEX="time_index";

        private static final String LAST_RECORD = "last_record";
        private static final String RECORD_DETAIL="records";

        private static final String TAG_ID_LOCATION="location_id";
        private static final String TAG_BASE_SERIAL = "base_serial";
        private static final String TAG_REMOTE_SERIAL = "remote_serial";
        private static final String TAG_LOCATION_NAME = "location_name";
        private static final String TAG_LOCATION_X = "x";
        private static final String TAG_LOCATION_Y = "y";


        protected void onPreExecute(){
            super.onPreExecute();
            MainActivity.pDialog.setMessage("Loading startup data");
            MainActivity.pDialog.setIndeterminate(false);
            MainActivity.pDialog.setCancelable(false);
            MainActivity.pDialog.show();
        }
        protected String doInBackground(String... args){

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json=jParser.makeHttpRequest(url_construct, "GET", params);
            Log.d("Message: ", json.toString());
            try{
                int success=json.getInt(TAG_SUCCESS);

                if(success == 1){

                    upload_time=json.getString(TAG_UPLOAD_TIME);
                    min_interval=json.getInt(TAG_MIN_INTERVAL);

                    JSONObject last_records=json.getJSONObject(LAST_RECORD);
                    JSONObject last_record_obj=last_records.getJSONObject(TAG_TEMPERATURE);
                    parseLastN(Ondotori.TEMPERATURE,last_record_obj);
                    last_record_obj=last_records.getJSONObject(TAG_HUMIDITY);
                    parseLastN(Ondotori.HUMIDITY,last_record_obj);
                    last_record_obj=last_records.getJSONObject(TAG_CO2);
                    parseLastN(Ondotori.CO2,last_record_obj);

                    JSONArray id_locations=json.getJSONArray(TAG_ID_LOCATION);
                    for(int i=0;i<id_locations.length();i++){
                        JSONObject id_location=id_locations.getJSONObject(i);
                        String id=id_location.getString(TAG_BASE_SERIAL)+"_"+id_location.getString(TAG_REMOTE_SERIAL); // base_remote
                        Location location=new Location(id_location.getString(TAG_LOCATION_NAME),id_location.getInt(TAG_LOCATION_X),id_location.getInt(TAG_LOCATION_Y));
                        id_location_map.put(id,location);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        private void parseLastN(int type, JSONObject last_record_obj){
            JSONArray statistics_points = null;
            try {
                statistics_points = last_record_obj.getJSONArray(RECORD_DETAIL);

                int point_num=statistics_points.length();
                if(point_num<=0)return;
                double[] avg=new double[point_num];
                double[] min=new double[point_num];
                double[] max=new double[point_num];
                for(int i=0;i<point_num;i++) {
                    try {
                        JSONObject point = statistics_points.getJSONObject(i);
                        int time_index = point.getInt(TAG_TIME_INDEX);
                        avg[time_index-1] = point.getDouble(TAG_AVG);
                        min[time_index-1] = point.getDouble(TAG_MIN);
                        max[time_index-1] = point.getDouble(TAG_MAX);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String last_update_time=last_record_obj.getString(TAG_LAST_UPDATE_TIME).trim();
                switch (type){
                    case Ondotori.TEMPERATURE:
                        Ondotori.temperature_collection.newPoints(avg, min, max, last_update_time);
                        Ondotori.temperature_collection.setStart_time(last_update_time);
                        break;
                    case Ondotori.HUMIDITY:
                        Ondotori.humidity_collection.newPoints(avg, min,max,last_update_time);
                        Ondotori.humidity_collection.setStart_time(last_update_time);
                        break;
                    case Ondotori.CO2:
                        Ondotori.co2_collection.newPoints(avg,min,max,last_update_time);
                        Ondotori.co2_collection.setStart_time(last_update_time);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        protected void onPostExecute(String file_url){
            MainActivity.pDialog.dismiss();
            new UpdateData_in().execute();
        }

    }

    class UpdateData_in extends AsyncTask<String, String, String> {

        JSONParser jParser = new JSONParser();


        private static final String TAG_SUCCESS = "success";
        private static final String TAG_UPDATE_RECORD="update_record";
        private static final String TAG_TEMPERATURE = "temp";
        private static final String TAG_HUMIDITY = "humid";
        private static final String TAG_CO2 = "co2";
        private static final String TAG_LAST_RECORD = "last_record";
        private static final String TAG_AVG = "avg";
        private static final String TAG_MIN = "min";
        private static final String TAG_MAX = "max";
        private static final String TAG_BASE_SERIAL = "base_serial";
        private static final String TAG_REMOTE_SERIAL = "remote_serial";
        private static final String TAG_VALUE = "value";
        private static final String TAG_LAST_UPDATE_TIME = "last_update_time";


        protected void onPreExecute(){
            super.onPreExecute();
            MainActivity.pDialog.setMessage("Updating data");
            MainActivity.pDialog.setIndeterminate(false);
            MainActivity.pDialog.setCancelable(false);
            MainActivity.pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            try{
                String temp_last_time=Ondotori.temperature_collection.getBoundTime();
                String humid_last_time=Ondotori.humidity_collection.getBoundTime();
                String co2_last_time=Ondotori.co2_collection.getBoundTime();





                String url_update = base_url_update+"?last_temp_time=\'"+temp_last_time+"\'&last_humid_time=\'"+humid_last_time+"'&last_co2_time=\'"+co2_last_time+"\'&dump=0";

                Log.d("URL",url_update);
                JSONObject json = jParser.makeHttpRequest(url_update, "GET", params);
                Log.d("Message: ", json.toString());


                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    JSONObject update_record = json.getJSONObject(TAG_UPDATE_RECORD);
                    JSONObject detail_update_record=update_record.getJSONObject(TAG_TEMPERATURE);
                    parseDetail(Ondotori.TEMPERATURE,detail_update_record);
                    detail_update_record=update_record.getJSONObject(TAG_HUMIDITY);
                    parseDetail(Ondotori.HUMIDITY,detail_update_record);
                    detail_update_record=update_record.getJSONObject(TAG_CO2);
                    parseDetail(Ondotori.CO2, detail_update_record);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        private void parseDetail(int type, JSONObject detail_obj) {

            ArrayList<SensorData> sensor_data_list=new ArrayList<SensorData>();
            try {
                JSONArray last_records=detail_obj.getJSONArray(TAG_LAST_RECORD);
                if(last_records.length()==0){
                    return;
                }
                Log.d("ParseDetail","Last record Length: "+last_records.length());
                for(int i=0;i<last_records.length();i++){
                    JSONObject last_record=last_records.getJSONObject(i);
                    UniqueID id=new UniqueID(last_record.getString(TAG_BASE_SERIAL),
                            last_record.getString(TAG_REMOTE_SERIAL));
                    SensorData sensordata = new SensorData(type,id,last_record.getDouble(TAG_VALUE));
                    sensor_data_list.add(sensordata);
                }
                double avg=detail_obj.getDouble(TAG_AVG);
                double min=detail_obj.getDouble(TAG_MIN);
                double max=detail_obj.getDouble(TAG_MAX);
                String last_update_time=detail_obj.getString(TAG_LAST_UPDATE_TIME);

                switch (type) {
                    case Ondotori.TEMPERATURE:
                        Ondotori.temperature_collection.updateSensorList(sensor_data_list);
                        Ondotori.temperature_collection.updateDataPoint(avg, min, max,last_update_time);
                        break;
                    case Ondotori.HUMIDITY:
                        Ondotori.humidity_collection.updateSensorList(sensor_data_list);
                        Ondotori.humidity_collection.updateDataPoint(avg, min,max, last_update_time);
                        break;
                    case Ondotori.CO2:
                        Ondotori.co2_collection.updateSensorList(sensor_data_list);
                        Ondotori.co2_collection.updateDataPoint(avg, min, max,last_update_time);
                        break;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        protected void onPostExecute(String file_url){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            MainActivity.pDialog.dismiss();
        }

    }

}



