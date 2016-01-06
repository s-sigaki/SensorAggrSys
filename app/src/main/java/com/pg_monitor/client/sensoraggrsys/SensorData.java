package com.pg_monitor.client.sensoraggrsys;

/**
 * Created by sunyanan on 1/6/16.
 */
public class SensorData {

    private UniqueID id;
    private double value;
    private int type;
    private String last_update;

    public SensorData(int type, UniqueID id,double value, String last_update){
        this.id=id;
        this.value=value;
        this.type=type;
        this.last_update=last_update;
    }

    public SensorData(SensorData sensorData){
        this.id=sensorData.getId();
        this.value=sensorData.getValue();
        this.type=sensorData.getType();
        this.last_update=sensorData.getLast_update();
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



    public String getLast_update() {
        return last_update;
    }


    public boolean isOnLocation(int x,int y){
        Location location = LoadData.id_location_map.get(id);
        return (x==location.getX()) && (y==location.getY());
    }

    public boolean isDevice(SensorData sensordata){
        return this.id.isMatch(sensordata.getId());
    }
    public void updateValue(SensorData sensorData){
        this.value=sensorData.getValue();
        this.type=sensorData.getType();
        this.last_update=sensorData.getLast_update();
    }

}
