package com.pg_monitor.client.sensoraggrsys;

/**
 * Created by sunyanan on 1/6/16.
 */
public class UniqueID {
    private String base_serial;
    private String remote_serial;
    public UniqueID(String base_serial,String remote_serial){
        this.base_serial=base_serial;
        this.remote_serial=remote_serial;
    }

    public String getBase_serial() {
        return base_serial;
    }

    public String getRemote_serial() {
        return remote_serial;
    }

    public boolean isMatch(UniqueID id){
        return base_serial.equals(id.getBase_serial())&&remote_serial.equals(id.getRemote_serial());
    }
}
