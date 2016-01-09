package com.pg_monitor.client.sensoraggrsys;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by sunyanan on 1/6/16.
 */
public class UpdateData extends AsyncTask<String, String, String> {

    JSONParser jParser = new JSONParser();

    private static String base_url_update = "http://52.69.84.252/ondotori_update_data.php";
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


    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... args) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        try{
            String temp_last_time=Ondotori.temperature_collection.getBoundTime();
            String humid_last_time=Ondotori.humidity_collection.getBoundTime();
            String co2_last_time=Ondotori.co2_collection.getBoundTime();





        String url_update = base_url_update+"?last_temp_time=\'"+temp_last_time+"\'&last_humid_time=\'"+humid_last_time+"'&last_co2_time=\'"+co2_last_time+"\'";

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
                    Ondotori.temperature_collection.updateDataPoint(avg, min, max, last_update_time);
                    break;
                case Ondotori.HUMIDITY:
                    Ondotori.humidity_collection.updateSensorList(sensor_data_list);
                    Ondotori.humidity_collection.updateDataPoint(avg, min, max, last_update_time);
                    break;
                case Ondotori.CO2:
                    Ondotori.co2_collection.updateSensorList(sensor_data_list);
                    Ondotori.co2_collection.updateDataPoint(avg, min, max, last_update_time);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}