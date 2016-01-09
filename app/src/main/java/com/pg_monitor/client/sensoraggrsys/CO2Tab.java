package com.pg_monitor.client.sensoraggrsys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sunyanan on 1/4/16.
 */
public class CO2Tab extends Fragment {
    final public static int TABLE_WIDTH=10;
    final public static int TABLE_HEIGHT=10;
    final public static int PIXEL_DIM=65;


    private int chart_width;
    final private static int NO_VAL=-99;
    final private static int ALPHA_BASE=150;
    final private static int MIN_SCALE=300;
    final private static int MAX_SCALE=5000;
    final private static String unit="ppm";

    private FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        frameLayout = new FrameLayout(getActivity());

        View rootview = inflater.inflate(R.layout.content_ondotori, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.ondotori_title);
        title.setText(Html.fromHtml("CO<sub>2</sub>"));
        title.setTextColor(getResources().getColor(R.color.co2_title));
        View line1 = rootview.findViewById(R.id.title_line1);
        line1.setBackgroundColor(getResources().getColor(R.color.co2_line1));
        View line2 = rootview.findViewById(R.id.title_line2);
        line2.setBackgroundColor(getResources().getColor(R.color.co2_line2));

        TableLayout ondotori_table = (TableLayout)rootview.findViewById(R.id.ondotori_table);
        ondotori_table.getLayoutParams().width=PIXEL_DIM*TABLE_WIDTH;
        ondotori_table.getLayoutParams().height=PIXEL_DIM*TABLE_HEIGHT;
        Bitmap co2_color_chart =  BitmapFactory.decodeResource(getResources(),
                R.drawable.co2_color_chart);
        chart_width=co2_color_chart.getWidth();
        for(int i=0;i<TABLE_HEIGHT;i++){
            TableRow ondotori_row = new TableRow(getActivity());
            ondotori_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for(int j=0;j<TABLE_WIDTH;j++){
                View ondotori_pixel=new View(getActivity());
                ondotori_pixel.setId(TABLE_WIDTH * i + j);
                SensorData cur_sensor=Ondotori.co2_collection.getSensorAtLocation(i,j);
                int cur_value;
                if(cur_sensor==null){
                    cur_value=NO_VAL;
                }else{
                    cur_value=(int)cur_sensor.getValue();
                }
                if(cur_value<=MAX_SCALE&&cur_value>=MIN_SCALE) {
                    ondotori_pixel.setBackgroundColor(co2_color_chart.getPixel(cur_value * chart_width / (MAX_SCALE-MIN_SCALE) -1, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE);
                }else if(cur_value>MAX_SCALE){
                    ondotori_pixel.setBackgroundColor(co2_color_chart.getPixel(chart_width-1, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (((cur_value - MAX_SCALE) > (255-ALPHA_BASE)) ?(cur_value-MAX_SCALE):(255-ALPHA_BASE)));
                }else if(cur_value!=NO_VAL){
                    ondotori_pixel.setBackgroundColor(co2_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (((MIN_SCALE-cur_value) > (255-ALPHA_BASE)) ?(MIN_SCALE-cur_value):(255-ALPHA_BASE)));
                }else{
                    ondotori_pixel.setBackgroundColor(co2_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(0);
                }
                ondotori_pixel.setLayoutParams(new TableRow.LayoutParams(PIXEL_DIM, PIXEL_DIM));
                if(cur_sensor!=null) {
                    ondotori_pixel.setOnTouchListener(new MyOnTouchListener(getActivity(), cur_sensor.getLocation().getName(), (int)cur_value, unit, NO_VAL));
                }
                ondotori_row.addView(ondotori_pixel);
            }
            ondotori_table.addView(ondotori_row,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        ImageView scale_chart = (ImageView)rootview.findViewById(R.id.ondotori_scale);
        scale_chart.setImageResource(R.drawable.co2_color_chart);
        TextView min_scale = (TextView)rootview.findViewById(R.id.ondotori_min_scale);
        min_scale.setText(MIN_SCALE + unit);
        TextView max_scale = (TextView)rootview.findViewById(R.id.ondotori_max_scale);
        max_scale.setText(MAX_SCALE + unit);

        TextView unit_text_view=(TextView)rootview.findViewById(R.id.ondotori_avg_unit);
        unit_text_view.setText(unit);
        unit_text_view=(TextView)rootview.findViewById(R.id.ondotori_min_unit);
        unit_text_view.setText(unit);
        unit_text_view=(TextView)rootview.findViewById(R.id.ondotori_max_unit);
        unit_text_view.setText(unit);

        if(Ondotori.co2_collection.get_avg_points().length>0) {
            TextView avg_text_view = (TextView) rootview.findViewById(R.id.ondotori_avg_value);
            TextView min_text_view = (TextView) rootview.findViewById(R.id.ondotori_min_value);
            TextView max_text_view = (TextView) rootview.findViewById(R.id.ondotori_max_value);
            TextView last_update_time_text_view = (TextView) rootview.findViewById(R.id.update_time);
            if(Ondotori.co2_collection.getLast_update_time()!=null){
                avg_text_view.setText(""+(int)Ondotori.co2_collection.getCurrentAvg().doubleValue());
                min_text_view.setText(""+(int)Ondotori.co2_collection.getCurrentMin().doubleValue());
                max_text_view.setText(""+(int)Ondotori.co2_collection.getCurrentMax().doubleValue());
                last_update_time_text_view.setText(Ondotori.co2_collection.getLast_update_time());
            }

        }
        GraphView graph = (GraphView) rootview.findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series_avg = new LineGraphSeries<DataPoint>(Ondotori.co2_collection.get_avg_points());
        LineGraphSeries<DataPoint> series_min = new LineGraphSeries<DataPoint>(Ondotori.co2_collection.get_min_points());
        LineGraphSeries<DataPoint> series_max = new LineGraphSeries<DataPoint>(Ondotori.co2_collection.get_max_points());


        series_avg.setColor(getResources().getColor(R.color.co2_title));
        series_avg.setTitle("AVG");
        series_avg.setThickness(10);
        series_max.setColor(getResources().getColor(R.color.trans_dark_red));
        series_max.setTitle("MAX");
        series_min.setColor(getResources().getColor(R.color.trans_dark_blue));
        series_min.setTitle("MIN");

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Last Hour (min)");
        graph.getLegendRenderer().setVisible(true);

        graph.addSeries(series_avg);
        graph.addSeries(series_max);
        graph.addSeries(series_min);

        series_avg.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                if (MainActivity.sToast != null) {
                    MainActivity.sToast.cancel();
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();

                String reportDate = DateStrCal.sub_sec_time(df.format(today), (int) dataPoint.getX() * MainActivity.min_interval);
                MainActivity.sToast = Toast.makeText(getActivity(), "AVG: " + (int)dataPoint.getY() + " @" + reportDate, Toast.LENGTH_SHORT);
                MainActivity.sToast.show();
            }
        });

        frameLayout.addView(rootview);
        return frameLayout;
    }


}
