package com.pg_monitor.client.sensoraggrsys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    final private static String unit="\u00b0C";

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
        title.setText(R.string.ondotori_temp);
        title.setTextColor(getResources().getColor(R.color.temp_title));
        View line1 = rootview.findViewById(R.id.title_line1);
        line1.setBackgroundColor(getResources().getColor(R.color.temp_line1));
        View line2 = rootview.findViewById(R.id.title_line2);
        line2.setBackgroundColor(getResources().getColor(R.color.temp_line2));

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
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (((cur_value - MAX_SCALE) > (255-ALPHA_BASE)) ?(cur_value-MAX_SCALE):(255-ALPHA_BASE)));
                }else if(cur_value!=NO_VAL){
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(ALPHA_BASE + (((MIN_SCALE-cur_value) > (255-ALPHA_BASE)) ?(MIN_SCALE-cur_value):(255-ALPHA_BASE)));
                }else{
                    ondotori_pixel.setBackgroundColor(temp_color_chart.getPixel(0, 10));
                    ondotori_pixel.getBackground().setAlpha(0);
                }
                ondotori_pixel.setLayoutParams(new TableRow.LayoutParams(PIXEL_DIM, PIXEL_DIM));
                ondotori_pixel.setOnTouchListener(new MyOnTouchListener(getActivity(), "(" + i + "," + j + ")",cur_value, unit,NO_VAL));
                ondotori_row.addView(ondotori_pixel);
            }
            ondotori_table.addView(ondotori_row,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        ImageView scale_chart = (ImageView)rootview.findViewById(R.id.ondotori_scale);
        scale_chart.setImageResource(R.drawable.temp_color_chart);
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


        GraphView graph = (GraphView) rootview.findViewById(R.id.graph);
        DataPoint[] points;
        LineGraphSeries<DataPoint> series_avg = new LineGraphSeries<DataPoint>(points=new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 6),
                new DataPoint(2, 5),
                new DataPoint(3, 4),
                new DataPoint(4, 8)
        });
        LineGraphSeries<DataPoint> series_min = new LineGraphSeries<DataPoint>(points=new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 1),
                new DataPoint(2, -3),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        LineGraphSeries<DataPoint> series_max = new LineGraphSeries<DataPoint>(points=new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 6),
                new DataPoint(2, 6),
                new DataPoint(3, 7),
                new DataPoint(4, 9)
        });




        series_avg.setColor(getResources().getColor(R.color.temp_title));
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
                MainActivity.sToast = Toast.makeText(getActivity(), "AVG: " + dataPoint.getY() + " @Last " + dataPoint.getX() + "minutes", Toast.LENGTH_SHORT);
                MainActivity.sToast.show();
            }
        });

        frameLayout.addView(rootview);
        return frameLayout;
    }


}
