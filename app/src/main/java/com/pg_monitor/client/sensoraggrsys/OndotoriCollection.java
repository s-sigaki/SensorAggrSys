package com.pg_monitor.client.sensoraggrsys;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by sunyanan on 1/6/16.
 */
public class OndotoriCollection {

    private ArrayList<SensorData> sensors;
    private ArrayList<Double> avg_values;
    private ArrayList<Double> min_values;
    private ArrayList<Double> max_values;

    private int type;

    public static int time_unit=60;

    public OndotoriCollection(int type){
        ArrayList<SensorData> sensors = new ArrayList<SensorData>();
        ArrayList<DataPoint> avg_points=new ArrayList<DataPoint>();
        ArrayList<DataPoint> min_points=new ArrayList<DataPoint>();
        ArrayList<DataPoint> max_points=new ArrayList<DataPoint>();

        this.type = type;
    }

    public ArrayList<SensorData> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<SensorData> sensors) {
        this.sensors = sensors;
    }

    private DataPoint[] getDataPointArray(ArrayList<Double> values){
        DataPoint[] points_arr = new DataPoint[values.size()];
        for(int i=0;i<points_arr.length;i++){
            points_arr[i]=new DataPoint(i,values.get(i).doubleValue());
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
        return getDataPointArray(avg_values);
    }
    public DataPoint[] get_min_points(){
        return getDataPointArray(min_values);
    }
    public DataPoint[] get_max_points(){
        return getDataPointArray(max_values);
    }

    public int getType() {
        return type;
    }

    public SensorData getSensorAtLocation(int type, int x,int y){
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
    public void updateDataPoint(double avg,double min,double max){
        avg_values.add(0,new Double(avg));
        min_values.add(0,new Double(min));
        max_values.add(0,new Double(max));
    }

    public void newPoints(double[] avg_values,double[] min_values,double[] max_values){
        for(int i=0;i<avg_values.length;i++){
            this.avg_values.add(new Double(avg_values[i]));
            this.min_values.add(new Double(min_values[i]));
            this.max_values.add(new Double(max_values[i]));
        }

    }

}
