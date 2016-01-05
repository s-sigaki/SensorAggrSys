package com.pg_monitor.client.sensoraggrsys;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sunyanan on 1/4/16.
 */
public class CO2Tab extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.content_ondotoriv2, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.ondotori_title);
        title.setText(R.string.ondotori_co2);
        FragmentTabHost mTabHost = (FragmentTabHost)rootview.findViewById(android.R.id.tabhost);
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
        return rootview;
    }
}
