package com.pg_monitor.client.sensoraggrsys;

/**
 * Created by sunyanan on 1/6/16.
 */
public class Location {
    private int x;
    private int y;
    private String name;
    public Location(String name, int x, int y){
        this.x=x;
        this.y=y;
        this.name=name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

}
