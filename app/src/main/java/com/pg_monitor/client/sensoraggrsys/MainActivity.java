package com.pg_monitor.client.sensoraggrsys;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);



        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.temp_icon)
                        .setContentDescription(R.string.ondotori_temp)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.humid_icon)
                        .setContentDescription(R.string.ondotori_humid)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.co2_icon)
                        .setContentDescription(R.string.ondotori_co2)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setIcon(R.mipmap.motion_icon)
                        .setContentDescription(R.string.motion)
                        .setTabListener(this));



/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        LinearLayout temp_option_layout = (LinearLayout)findViewById(R.id.temp_option_layout);
        temp_option_layout.setBackgroundColor(getResources().getColor(R.color.soft_background));

        ImageView temp_option= (ImageView)findViewById(R.id.temp_button);
        temp_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetOptionBackground();
                LinearLayout cur_option = (LinearLayout) findViewById(R.id.temp_option_layout);
                cur_option.setBackgroundColor(getResources().getColor(R.color.soft_background));


            }
        });

        ImageView humid_option= (ImageView)findViewById(R.id.humid_button);
        humid_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetOptionBackground();
                LinearLayout cur_option = (LinearLayout)findViewById(R.id.humid_option_layout);
                cur_option.setBackgroundColor(getResources().getColor(R.color.soft_background));



            }
        });

        ImageView co2_option= (ImageView)findViewById(R.id.co2_button);
        co2_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetOptionBackground();
                LinearLayout cur_option = (LinearLayout)findViewById(R.id.co2_option_layout);
                cur_option.setBackgroundColor(getResources().getColor(R.color.soft_background));



            }
        });

        ImageView motion_option= (ImageView)findViewById(R.id.motion_button);
        motion_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetOptionBackground();
                LinearLayout cur_option = (LinearLayout)findViewById(R.id.motion_option_layout);
                cur_option.setBackgroundColor(getResources().getColor(R.color.soft_background));



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void resetOptionBackground(){
        LinearLayout temp_option_layout = (LinearLayout)findViewById(R.id.temp_option_layout);
        LinearLayout humid_option_layout = (LinearLayout)findViewById(R.id.humid_option_layout);
        LinearLayout co2_option_layout = (LinearLayout)findViewById(R.id.co2_option_layout);
        LinearLayout motion_option_layout = (LinearLayout)findViewById(R.id.motion_option_layout);
        temp_option_layout.setBackgroundColor(getResources().getColor(R.color.soft_blue));
        humid_option_layout.setBackgroundColor(getResources().getColor(R.color.soft_blue));
        co2_option_layout.setBackgroundColor(getResources().getColor(R.color.soft_blue));
        motion_option_layout.setBackgroundColor(getResources().getColor(R.color.soft_blue));
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {


        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new TempTab();
                case 1:
                    return new HumidTab();
                case 2:
                    return new CO2Tab();
                case 3:
                    return new MotionTab();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }


}
