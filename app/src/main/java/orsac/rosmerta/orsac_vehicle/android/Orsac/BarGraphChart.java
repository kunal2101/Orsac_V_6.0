package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by qr on 20/3/17.
 */
public class BarGraphChart extends AppCompatActivity {
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    List<ChartData> value;
    String[] companyname;//{"COMP1","COMP2"}
    String[] symbol = {"A","B","C","D","E","F","G","H"};
    Float[] value3,value4;
    ArrayList<String> msgArr = new ArrayList<>();
    float[] array_int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bargraphchart);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_title.setText("Installed Device");
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        // BarChart barChart = (BarChart) findViewById(R.id.chart);
        Bundle b=this.getIntent().getExtras();
         array_int=b.getFloatArray("key_inst");
        value = new ArrayList<>();

        value4 = new Float[array_int.length];
        companyname =b.getStringArray("key_comp");

        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.chart);
        /*for(int ind=0;ind<array_int.length;ind++){
            float temp = array_int[ind];
            float temp_int = array_int[ind];
            value3[ind]=temp;
            value4[ind]=temp_int;
        }*/
        ArrayList<BarEntry> entries = new ArrayList<>();

        List<String> h_lables = new ArrayList<>();
        for(int ain = 0 ; ain < companyname.length; ain++){
            h_lables.add(symbol[ain]);
            entries.add(new BarEntry(array_int[ain], ain));
            String temp = symbol[ain]+" - "+companyname[ain];
            msgArr.add(temp);
        }


        TextView description = (TextView)findViewById(R.id.description);
        description.setText(TextUtils.join("\n",msgArr));
/*
        entries.add(new BarEntry(40f, 0));
        entries.add(new BarEntry(2000f, 1));
        entries.add(new BarEntry(1840f, 2));
        entries.add(new BarEntry(1350f, 3));
        entries.add(new BarEntry(4f, 4));*/

        BarDataSet dataset = new BarDataSet(entries, "Installed Device");
        /*ArrayList<String> labels = new ArrayList<String>();
        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("E");
*/
        /* for create Grouped Bar chart
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));

        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);
        */
        int[] colorarr = {Color.parseColor("#3F51B5")};
        dataset.setColors(colorarr);
        BarData data = new BarData(h_lables, dataset);
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(data);
        barChart.animateY(5000);
        barChart.setDescription("");



    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //onBackPressed();
                Intent inty = new Intent(BarGraphChart.this,NavigationActivity.class);
                startActivity(inty);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
