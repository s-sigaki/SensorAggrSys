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

    private static String url_update = "http://10.0.2.2:8080/get_data.php";
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
    private static final String TAG_LAST_UPDATE = "last_update_time";


    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... args) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_update, "GET", params);
        Log.d("Message: ", json.toString());

        try {
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
            for(int i=0;i<last_records.length();i++){
                JSONObject last_record=last_records.getJSONObject(i);
                UniqueID id=new UniqueID(last_record.getString(TAG_BASE_SERIAL),
                        last_record.getString(TAG_REMOTE_SERIAL));
                SensorData sensordata = new SensorData(type,id,last_record.getDouble(TAG_VALUE),last_record.getString(TAG_LAST_UPDATE));
                sensor_data_list.add(sensordata);
            }
            double avg=detail_obj.getDouble(TAG_AVG);
            double min=detail_obj.getDouble(TAG_MIN);
            double max=detail_obj.getDouble(TAG_MAX);

            switch (type) {
                case Ondotori.TEMPERATURE:
                    Ondotori.temperature_collection.updateSensorList(sensor_data_list);
                    Ondotori.temperature_collection.updateDataPoint(avg,min,max);
                    break;
                case Ondotori.HUMIDITY:
                    Ondotori.humidity_collection.updateSensorList(sensor_data_list);
                    Ondotori.humidity_collection.updateDataPoint(avg, min, max);
                    break;
                case Ondotori.CO2:
                    Ondotori.co2_collection.updateSensorList(sensor_data_list);
                    Ondotori.co2_collection.updateDataPoint(avg, min, max);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}