package orsac.rosmerta.orsac_vehicle.android;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.data.Mapping;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.List;

import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.JumpLine;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;


public class RadarChartActivity  extends AppCompatActivity {
    PreferenceHelper preferenceHelper;
    List<DataEntry> data;
    Cartesian vertical;
    AnyChartView anyChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);
        preferenceHelper = new PreferenceHelper(this);
         anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

         vertical = AnyChart.vertical();

        vertical.animation(true)
                .title("Datewise OMVTS Vehicle Search ");

        data = new ArrayList<>();
        data.add(new CustomDataEntry("2020-07-06", 141749, 14112));
      new Last_90_days().execute (  );


    }

    private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number jumpLine) {
            super(x, value);
            setValue("jumpLine", jumpLine);
        }
    }
    class Last_90_days extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            try {
                HttpClient httpclient = new DefaultHttpClient ();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
                   HttpGet httppost = new HttpGet("http://vts.odishaminerals.gov.in/rtlorsacandroid/rest/CallService/vehiclesHitDateWise?dateRange=12&vehicleNo=");
              //  HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA+"?dataRange=0");
                  // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {



                        JSONObject jsonObject_new = new JSONObject (getResponse);

                        JSONArray jsonArray = jsonObject_new.getJSONArray ( "data" );

                        for(int i = 0 ; i < jsonArray.length ();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject ( i );
                            data.add(new CustomDataEntry(jsonObject.getString ( "datetime" ) ,Integer.parseInt ( jsonObject.getString ( "overallSearch" ) ),Integer.parseInt ( jsonObject.getString ( "distinctSearch" ) )));

                        }

                        Set set = Set.instantiate();
                        set.data(data);
                        Mapping barData = set.mapAs("{ x: 'x', value: 'value' }");
                        Mapping jumpLineData = set.mapAs("{ x: 'x', value: 'jumpLine' }");

                        Bar bar = vertical.bar(barData);
                        bar.labels().format("{%Value} hits");

                        JumpLine jumpLine = vertical.jumpLine(jumpLineData);
                        jumpLine.stroke("2 #60727B");
                        jumpLine.labels().enabled(false);

                        vertical.yScale().minimum(0d);

                        vertical.labels(true);

                        vertical.tooltip()
                                .displayMode(TooltipDisplayMode.UNION)
                                .positionMode(TooltipPositionMode.POINT)
                                .unionFormat(
                                        "function() {\n" +
                                                "      return 'Total Hits: ' + this.points[1].value + ' mln' +\n" +
                                                "        '\\n' + 'Distinct Hits: ' + this.points[0].value + ' mln';\n" +
                                                "    }");

                        vertical.interactivity().hoverMode(HoverMode.BY_X);

                        vertical.xAxis(true);
                        vertical.yAxis(true);
                        vertical.yAxis(0).labels().format("{%Value} hits");

                        anyChartView.setChart(vertical);



                    } catch (Exception ev) {
                        AndyUtils.removeCustomProgressDialog();
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }
    }

}