package com.pg_monitor.client.sensoraggrsys;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sunyanan on 1/6/16.
 */



public class LoadData extends AsyncTask<String, String, String> {

    JSONParser jParser=new JSONParser();

    public static String upload_time=null;
    public static int min_interval;

    public static HashMap<String,Location> id_location_map=new HashMap<String,Location>();

    private static String url_construct= "http://52.69.84.252/ondotori_load_data.php";
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
                Ondotori.humidity_collection.newPoints(avg,min,max,last_update_time);
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
        new UpdateData().execute();
    }

}

