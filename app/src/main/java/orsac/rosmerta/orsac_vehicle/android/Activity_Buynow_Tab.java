package orsac.rosmerta.orsac_vehicle.android;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.fragment.FragmentSimRenewal;
import orsac.rosmerta.orsac_vehicle.android.fragment.Fragment_AMC;
import orsac.rosmerta.orsac_vehicle.android.fragment.Fragment_New_Device_Purchas;
import orsac.rosmerta.orsac_vehicle.android.adapter.TabAdapter;


public class Activity_Buynow_Tab extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView pimage;
    TextView pName;
    private ImageView img_back;
    private TextView txt_title;
    String  stopName;
    int stopId;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    String com_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new___history__tab);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        try {
            Intent inty = getIntent();
            com_name = inty.getStringExtra("com_name");
        }catch (Exception e){
            e.getMessage();
        }
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("Payment");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_New_Device_Purchas(), "New Device");
        adapter.addFragment(new Fragment_AMC(), "AMC's");

        adapter.addFragment(new FragmentSimRenewal(), "Sim Renewal");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

}
