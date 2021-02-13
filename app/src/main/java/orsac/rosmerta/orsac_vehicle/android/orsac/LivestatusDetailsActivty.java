package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Diwash Choudhary on 2/6/2017.
 */
public class LivestatusDetailsActivty extends Activity implements View.OnClickListener {
LinearLayout pie_chart,half_pie;
    TextView mapview;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    FrameLayout navi_frame;
    DrawerLayout drawer;
    private  ImageView icon_tool;
String vehicle_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_hint);
        Intent inty = getIntent();
        vehicle_no = inty.getStringExtra("etpNo");

        pie_chart = (LinearLayout)findViewById(R.id.pie_chart);
        half_pie = (LinearLayout)findViewById(R.id.half_pie);
        mapview= (TextView)findViewById(R.id.mapview);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Vehicle Detail ");
        icon_tool = (ImageView) findViewById(R.id.icon_tool);

      //  icon_tool.setImageResource(R.drawable.ic_oie_transparent_q);
        icon_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LivestatusDetailsActivty.this,MapTrackingActivity_Live_Status.class);
                startActivity(intent);
            }
        });

        navi_frame = (FrameLayout) findViewById(R.id.navi_frame);

        drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
        navi_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer( GravityCompat.END);
            }
        });

        pie_chart.setOnClickListener(this);
        half_pie.setOnClickListener(this);
        mapview.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case  R.id.pie_chart:
                Intent inty = new Intent(DetailActivty.this,PieChart.class);
                startActivity(inty);
                break;
            case  R.id.half_pie:
                Intent intent = new Intent(DetailActivty.this,HalfPieChartActivity.class);
                startActivity(intent);
                break;*/
            case  R.id.mapview:
                Intent intent = new Intent(LivestatusDetailsActivty.this,TrackOnGoogleMap.class);
                startActivity(intent);
                break;

        }
    }
    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK,myIntent);
        super.finish();
    }


}
