package com.pg_monitor.client.sensoraggrsys;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by sunyanan on 1/5/16.
 */
public class MyOnTouchListener implements View.OnTouchListener
{

    FragmentActivity activity;
    String location_name;
    double value;
    double null_value;
    String unit;
    public MyOnTouchListener (FragmentActivity in_activity,String in_location_name,double in_value,String in_unit,double in_null_value) {
        this.activity=in_activity;
        this.location_name=in_location_name;
        this.value=in_value;
        this.unit=in_unit;
        this.null_value=in_null_value;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (MainActivity.sToast != null) {
            MainActivity.sToast.cancel();
        }
        if(Double.compare(value,null_value)==0){
            MainActivity.sToast=null;
            return false;
        }
        MainActivity.sToast=Toast.makeText(activity,location_name+"\n"+value+unit,Toast.LENGTH_LONG);
        MainActivity.sToast.setGravity(Gravity.CENTER,0,0);
        MainActivity.sToast.show();

        return true;
    }

};