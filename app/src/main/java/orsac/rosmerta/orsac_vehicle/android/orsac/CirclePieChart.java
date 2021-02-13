package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by qr on 20/3/17.
 */
public class CirclePieChart extends AppCompatActivity {
    //String[] games = new String[] { "Arya Omn", "Rosmerta","Atlanta", "Fastrackerz"};
    private GraphicalView mChart;
   // Double[] value3;
    String[] cpnyName;
    double[] tripCount;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circlepiechart);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_title.setText("Companywise Details");
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        Bundle b=this.getIntent().getExtras();

        tripCount = b.getDoubleArray("TRIPCOUNT");
        cpnyName = b.getStringArray("COMPANY_NAME");

       /* value3 = new Double[tripCount.length];
        cpnyName = new String[compy_name.length];

        for(int ind=0;ind<tripCount.length;ind++){
            double temp = tripCount[ind];
            String temp_int = compy_name[ind];
            value3[ind]=temp;
            cpnyName[ind]=temp_int;
        }*/

        drawChart();
    }

    public void drawChart(){

        // Pie Chart Section Value
        //double[] played_match = { 20,15,20.8,90};

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.GRAY,Color.YELLOW,Color.RED,Color.BLACK,Color.WHITE};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries("Sports CLub");
       for (int i = 0; i < tripCount.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(cpnyName[i],tripCount[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < tripCount.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            //Adding colors to the chart
            defaultRenderer.setBackgroundColor(Color.BLACK);
            defaultRenderer.setLabelsTextSize(30f);
            defaultRenderer.setLegendTextSize(30f);
            defaultRenderer.setApplyBackgroundColor(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("");
        defaultRenderer.setChartTitleTextSize(10);
        defaultRenderer.setZoomButtonsVisible(false);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        chartContainer.removeAllViews();
        mChart = ChartFactory.getPieChartView(getBaseContext(), distributionSeries, defaultRenderer);
        defaultRenderer.setClickEnabled(true);//
        defaultRenderer.setSelectableBuffer(10);


        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {

                    // Getting the name of the clicked slice
                    int seriesIndex = seriesSelection.getPointIndex();
                    String selectedSeries=cpnyName[seriesIndex];

                    // Getting the value of the clicked slice
                    double value = seriesSelection.getXValue();
                    DecimalFormat dFormat = new DecimalFormat("#.#");

                    // Displaying the message
                  /*  Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " : "  + Double.valueOf(dFormat.format(value)) + " % " ,
                            Toast.LENGTH_SHORT).show();*/
                }
            }
        });
        // drawing pie chart

        // remove any views before u paint the chart
        //chartContainer.removeAllViews();
        // adding the view to the linearlayout
        chartContainer.addView(mChart);

    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

}
