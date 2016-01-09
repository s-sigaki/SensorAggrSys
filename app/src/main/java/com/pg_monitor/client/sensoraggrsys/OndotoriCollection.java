package com.pg_monitor.client.sensoraggrsys;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by sunyanan on 1/6/16.
 */
public class OndotoriCollection {

    private ArrayList<SensorData> sensors;
    private ArrayList<Double> avg_points;
    private ArrayList<Double> min_points;
    private ArrayList<Double> max_points;


    private int type;
    private String start_time=null;
    private String last_update_time=null;
    public static int time_unit=60;

    public OndotoriCollection(int type){
        sensors = new ArrayList<SensorData>();
        avg_points = new ArrayList<Double>();
        min_points = new ArrayList<Double>();
        max_points = new ArrayList<Double>();
        this.type = type;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    private void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public ArrayList<SensorData> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<SensorData> sensors) {
        this.sensors = sensors;
    }

    private DataPoint[] getDataPointArray(ArrayList<Double> points){
        DataPoint[] points_arr = new DataPoint[points.size()];
        for(int i=0;i<points.size();i++){
            points_arr[i]=new DataPoint(i,points.get(i));
        }
        return points_arr;
    }
    private SensorData getContainedSensor(SensorData sensordata){
        for(int i=0;i<sensors.size();i++){
            if(sensors.get(i).isDevice(sensordata)){
                return sensors.get(i);
            }
        }
        return null;
    }

    public DataPoint[] get_avg_points(){
        return getDataPointArray(avg_points);
    }
    public DataPoint[] get_min_points(){
        return getDataPointArray(min_points);
    }
    public DataPoint[] get_max_points(){
        return getDataPointArray(max_points);
    }

    public int getType() {
        return type;
    }

    public SensorData getSensorAtLocation(int x,int y){
        for(int i=0;i<sensors.size();i++){
            if(sensors.get(i).isOnLocation(x,y)){
                return sensors.get(i);
            }
        }
        return null;
    }

    private void updateSensor(SensorData sensor_data){
        SensorData added_sensor= getContainedSensor(sensor_data);
        if(added_sensor==null){
            added_sensor=new SensorData(sensor_data);
            sensors.add(added_sensor);
        }else{
            added_sensor.updateValue(sensor_data);
        }
    }

    public void updateSensorList(ArrayList<SensorData> sensor_data_list){
        for(int i=0;i<sensor_data_list.size();i++){
            updateSensor(sensor_data_list.get(i));
        }
    }
    public void updateDataPoint(double avg,double min,double max, String update_time_str){
        if((last_update_time!=null)&&last_update_time.equals(update_time_str)){
            Log.d("OndotoriCollection", "no update data ("+last_update_time+" - "+update_time_str+")");
            return; // No update
        }
        Log.d("OndotoriCollection", "update some data (" + last_update_time + " - " + update_time_str+")");
        avg_points.add(0,new Double(avg));
        min_points.add(0,new Double(min));
        max_points.add(0,new Double(max));
        if(avg_points.size()>Ondotori.MAX_POINT_NUM){
            avg_points.remove(avg_points.size() - 1);
            min_points.remove(min_points.size() - 1);
            max_points.remove(max_points.size() - 1);
        }
        setLast_update_time(update_time_str);
    }

    public void newPoints(double[] avg_values,double[] min_values,double[] max_values,String update_time){
        if((start_time!=null))return;
        for(int i=0;i<avg_values.length;i++){
            avg_points.add(0,new Double(avg_values[i]));
            min_points.add(0,new Double(min_values[i]));
            max_points.add(0,new Double(max_values[i]));
        }
        setStart_time(update_time);

    }

    public boolean hasValues(){
        return avg_points.size()!=0;
    }
    public Double getCurrentAvg(){
        if(!hasValues())return null;
        return avg_points.get(0);
    }
    public Double getCurrentMin(){
        if(!hasValues())return null;
        return min_points.get(0);
    }
    public Double getCurrentMax(){
        if(!hasValues())return null;
        return max_points.get(0);
    }

    public String getBoundTime() {
        try {
            if (getLast_update_time() == null) {

                return URLEncoder.encode(getStart_time(), "UTF-8");

            } else {
                return URLEncoder.encode(getLast_update_time(), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
