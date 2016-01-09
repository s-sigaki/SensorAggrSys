package com.pg_monitor.client.sensoraggrsys;

import android.util.Log;

/**
 * Created by sunyanan on 1/6/16.
 */
public class SensorData {

    private UniqueID id;
    private double value;
    private int type;

    public SensorData(int type, UniqueID id,double value){
        this.id=id;
        this.value=value;
        this.type=type;
    }

    public SensorData(SensorData sensorData){
        this.id=sensorData.getId();
        this.value=sensorData.getValue();
        this.type=sensorData.getType();
    }

    public UniqueID getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }




    public boolean isOnLocation(int x,int y){
        Location location = LoadData.id_location_map.get(id.getBase_serial()+"_"+id.getRemote_serial());
        if(location == null){
            Log.d("Sensor data", "Location null:" + id.getBase_serial() + "_" + id.getRemote_serial());
        }
        return (x==location.getX()) && (y==location.getY());
    }

    public boolean isDevice(SensorData sensordata){
        return this.id.isMatch(sensordata.getId());
    }
    public void updateValue(SensorData sensorData){
        this.value=sensorData.getValue();
        this.type=sensorData.getType();
    }

}
