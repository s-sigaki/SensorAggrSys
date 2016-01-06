package com.pg_monitor.client.sensoraggrsys;

/**
 * Created by sunyanan on 1/6/16.
 */
public class Ondotori {
    public static final int TEMPERATURE = 0;
    public static final int HUMIDITY = 1;
    public static final int CO2 = 2;

    public static OndotoriCollection temperature_collection=new OndotoriCollection(TEMPERATURE);
    public static OndotoriCollection humidity_collection=new OndotoriCollection(HUMIDITY);
    public static OndotoriCollection co2_collection=new OndotoriCollection(CO2);

}
