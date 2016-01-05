package com.pg_monitor.client.sensoraggrsys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by sunyanan on 1/4/16.
 */
public class TempTab extends Fragment {

    final public static int TABLE_WIDTH=10;
    final public static int TABLE_HEIGHT=10;
    final public static int PIXEL_DIM=75;


    private int[] value;
    private int chart_width;
    final private static int NO_VAL=-99;
    final private static int ALPHA_BASE=150;
    final private static int MIN_SCALE=0;
    final private static int MAX_SCALE=50;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.content_ondotori, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.ondotori_title);
        title.setText(R.string.ondotori_temp);

        TableLayout ondotori_table = (TableLayout)rootview.findViewById(R.id.ondotori_table);
        ondotori_table.getLayoutParams().width=PIXEL_DIM*TABLE_WIDTH;
        ondotori_table.getLayoutParams().height=PIXEL_DIM*TABLE_HEIGHT;
        Bitmap temp_color_chart =  BitmapFactory.decodeResource(getResources(),
                R.drawable.temp_color_chart);
        chart_width=temp_color_chart.getWidth();

        value = new int[TABLE_WIDTH*TABLE_HEIGHT];
        for(int i=0;i<value.length;i++){
            value[i]=NO_VAL;
        }
        value[2*TABLE_WIDTH]=-10;
        value[2*TABLE_WIDTH+1]=15;
        value[3*TABLE_WIDTH]=20;
        value[4*TABLE_WIDTH]=100;
        value[5*TABLE_WIDTH]=50;
        value[6*TABLE_WIDTH]=40;
        value[7*TABLE_WIDTH]=30;
        for(int i=0;i<TABLE_HEIGHT;i++){
            TableRow ondotori_row = new TableRow(getActivity());
            ondotori_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for(int j=0;j<TABLE_WIDTH;j++){
                View ondotori_pixel=new View(getActivity());
                ondotori_pixel.setId(TABLE_WIDTH * i + j);
                int cur_value=value[TABLE_WIDTH*i+j];
                if(cur_value<=MAX_SCALE&&cur_value>=MIN_SCALE) {
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(cur_value * chart_width / (MAX_SCALE-MIN_SCALE) -1, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE);
                }else if(cur_value>MAX_SCALE){
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(chart_width-1, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (cur_value - MAX_SCALE) > (255-ALPHA_BASE) ?(cur_value-MAX_SCALE):(255-ALPHA_BASE));
                }else if(cur_value!=NO_VAL){
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (MIN_SCALE-cur_value) > (255-ALPHA_BASE) ?(MIN_SCALE-cur_value):(255-ALPHA_BASE));
                }else{
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(0);
                }
                ondotori_pixel.setLayoutParams(new TableRow.LayoutParams(PIXEL_DIM, PIXEL_DIM));
                ondotori_row.addView(ondotori_pixel);
            }
            ondotori_table.addView(ondotori_row,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        ImageView scale_chart = (ImageView)rootview.findViewById(R.id.ondotori_scale);
        scale_chart.setImageResource(R.drawable.temp_color_chart);
        TextView min_scale = (TextView)rootview.findViewById(R.id.ondotori_min_scale);
        min_scale.setText(MIN_SCALE+"");
        TextView max_scale = (TextView)rootview.findViewById(R.id.ondotori_max_scale);
        max_scale.setText(MAX_SCALE+"");
        return rootview;

        /* version 3 tabs

        View rootview = inflater.inflate(R.layout.content_ondotoriv2, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.ondotori_title);
        title.setText(R.string.ondotori_temp);
        final FragmentTabHost mTabHost = (FragmentTabHost)rootview.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);



        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Last", null),
                TempLastTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Avg", null),
                TempLastTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Min/Max", null),
                TempLastTab.class, null);
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++) {
            mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
        }
        return rootview;

        */
    }


}
