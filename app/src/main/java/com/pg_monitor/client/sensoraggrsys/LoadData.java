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

    public static String last_update_time;
    public static int min_interval;

    public static HashMap<UniqueID,Location> id_location_map=new HashMap<UniqueID,Location>();

    private static String url_construct= "http://10.0.2.2:8080/get_data.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAST_UPDATE = "last_update_time";
    private static final String TAG_MIN_INTERVAL = "min_interval";
    private static final String TAG_TEMPERATURE = "temp";
    private static final String TAG_HUMIDITY = "humid";
    private static final String TAG_CO2 ="co2";
    private static final String TAG_LAST_RECORD="last_record";
    private static final String TAG_TIME_INDEX="time_index";
    private static final String TAG_AVG="avg";
    private static final String TAG_MIN="min";
    private static final String TAG_MAX="max";
    private static final String TAG_ID_LOCATION="id_location";
    private static final String TAG_BASE_SERIAL = "base_serial";
    private static final String TAG_REMOTE_SERIAL = "remote_serial";
    private static final String TAG_LOCATION_NAME = "location_name";
    private static final String TAG_LOCATION_X = "location_x";
    private static final String TAG_LOCATION_Y = "location_y";



    protected void onPreExecute(){
        super.onPreExecute();
    }
    protected String doInBackground(String... args){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json=jParser.makeHttpRequest(url_construct, "GET", params);
        Log.d("Message: ", json.toString());

        try{
            int success=json.getInt(TAG_SUCCESS);

            if(success == 1){

                last_update_time=json.getString(TAG_LAST_UPDATE);
                min_interval=json.getInt(TAG_MIN_INTERVAL);

                JSONObject last_records=json.getJSONObject(TAG_LAST_RECORD);
                JSONArray last_n_records=last_records.getJSONArray(TAG_TEMPERATURE);
                parseLastN(Ondotori.TEMPERATURE,last_n_records);
                last_n_records=last_records.getJSONArray(TAG_HUMIDITY);
                parseLastN(Ondotori.HUMIDITY,last_n_records);
                last_n_records=last_records.getJSONArray(TAG_CO2);
                parseLastN(Ondotori.CO2,last_n_records);

                JSONArray id_locations=json.getJSONArray(TAG_ID_LOCATION);
                for(int i=0;i<id_locations.length();i++){
                    JSONObject id_location=id_locations.getJSONObject(i);
                    UniqueID id=new UniqueID(id_location.getString(TAG_BASE_SERIAL),id_location.getString(TAG_REMOTE_SERIAL));
                    Location location=new Location(id_location.getString(TAG_LOCATION_NAME),id_location.getInt(TAG_LOCATION_X),id_location.getInt(TAG_LOCATION_Y));
                    id_location_map.put(id,location);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void parseLastN(int type, JSONArray statistics_points){
        int point_num=statistics_points.length();
        if(point_num<=0)return;
        double[] avg=new double[point_num];
        double[] min=new double[point_num];
        double[] max=new double[point_num];
        for(int i=0;i<point_num;i++){
            try {
                JSONObject point = statistics_points.getJSONObject(i);
                int time_index=point.getInt(TAG_TIME_INDEX);
                avg[time_index]=point.getDouble(TAG_AVG);
                min[time_index]=point.getDouble(TAG_MIN);
                max[time_index]=point.getDouble(TAG_MAX);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        switch (type){
            case Ondotori.TEMPERATURE:
                Ondotori.temperature_collection.newPoints(avg,min,max);
                break;
            case Ondotori.HUMIDITY:
                Ondotori.humidity_collection.newPoints(avg,min,max);
                break;
            case Ondotori.CO2:
                Ondotori.co2_collection.newPoints(avg,min,max);
                break;
        }
    }

}

