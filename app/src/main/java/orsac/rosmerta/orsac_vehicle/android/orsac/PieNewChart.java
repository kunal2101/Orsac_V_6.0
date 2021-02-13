package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Diwash Choudhary on 3/18/2017.
 */
public class PieNewChart extends AppCompatActivity {
    ArrayList<String> labels;
    ArrayList<Entry> entries;
    PieChart pieChart;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private RadioGroup rgp;
    static ArrayList<HashMap<String,String>> companydataList;
    static ArrayList<String> companyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_new);

        pieChart = (PieChart) findViewById(R.id.chart);
        labels = new ArrayList<String>();
        entries = new ArrayList<>();

        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_title.setText("Device Detail");

        rgp = (RadioGroup) findViewById(R.id.radiogroup);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });


       new GetCompanyDetail().execute();

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                try {
                    int grpParkingSelectedId = group.getCheckedRadioButtonId();
                    RadioButton parkingRB = (RadioButton) findViewById(grpParkingSelectedId);
                    String getComname = parkingRB.getText().toString().trim();
                    labels.clear();
                    entries.clear();
                    for(int pqr=0; pqr < companydataList.size(); pqr++){
                        HashMap<String,String> map = companydataList.get(pqr);
                        if(map.get("KEY_COMPANY_NAME").equalsIgnoreCase(getComname)){

                            labels.add(map.get("KEY_SOLD_DEVICE"));
                            labels.add(map.get("KEY_AVAILABLE_DEVICE"));

                            int totDevice = Integer.parseInt(map.get("KEY_TOTAL_DEVICE"));
                            int available_dev = Integer.parseInt(map.get("KEY_AVAILABLE_DEVICE"));
                            int sold_device = Integer.parseInt(map.get("KEY_SOLD_DEVICE"));

                            int available_percentage = (available_dev * 100) / totDevice;
                            int sold_percentage = (sold_device * 100) / totDevice;


                            entries.add(new Entry(sold_percentage, 0));
                            entries.add(new Entry(available_percentage, 1));
                            PieDataSet dataset = new PieDataSet(entries, "");

                            int[] colorarr = {Color.parseColor("#e42424"), Color.parseColor("#303F9F")};

                            PieData data = new PieData(labels, dataset);
                            //dataset.setColors(ColorTemplate.PASTEL_COLORS);
                            dataset.setColors(colorarr);

                            pieChart.setDescription("");
                            pieChart.setData(data);
                            pieChart.animateY(5000);
                            pieChart.setCenterTextSize(25);
                            data.setValueTextSize(15f);
                            data.setValueTextColor(Color.parseColor("#ffffff"));
                            pieChart.setDrawSliceText(false);
                           // data.setDrawValues(false);
                            pieChart.getLegend().setEnabled(false);

                            pieChart.setDescription("");
                            pieChart.setData(data);

                            pieChart.animateY(5000);

                            pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
                            pieChart.setDrawSliceText(true);
                            data.setDrawValues(false);
                            pieChart.getLegend().setEnabled(false);

                        }
                    }
                }catch (Exception ev){
                    System.out.print(ev.getMessage());
                }
            }
        });


        //entries.add(new Entry(44f, 0));
        //entries.add(new Entry(76f, 1));

       /* PieDataSet dataset = new PieDataSet(entries, "");
        labels.add("1721");
      labels.add("5827");*/

       /* int[] colorarr = {Color.parseColor("#e42424"), Color.parseColor("#303F9F")};

        PieData data = new PieData(labels, dataset);
        //dataset.setColors(ColorTemplate.PASTEL_COLORS);
        dataset.setColors(colorarr);
        pieChart.setDescription("");
        pieChart.setData(data);
        pieChart.animateY(5000);
        pieChart.setCenterTextSize(25);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.parseColor("#ffffff"));*/

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //onBackPressed();
                Intent inty = new Intent(PieNewChart.this,NavigationActivity.class);
                startActivity(inty);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }


    class GetCompanyDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(PieNewChart.this,
                    " Please wait...", false, null);

        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = "http://vts.orissaminerals.gov.in/andorsac/rest/CallService/companywisedevicecount?controlid=0&companyid=30001";
                HttpGet httppost = new HttpGet(url);

                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
           // progressDialog.dismiss();
            try{
                //JSONObject jsonObj = new JSONObject(getResponse);
                //String success = jsonObj.getString("Success");

               // if(success.equalsIgnoreCase("true")){
                companydataList = new ArrayList<>();
                companyList = new ArrayList<>();
                    JSONArray jarray = new JSONArray(getResponse);
                JSONObject dataObj = null;
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                         dataObj = jarray.getJSONObject(i);

                        map.put("KEY_COMPANY_NAME", dataObj.getString("companyname"));
                        map.put("KEY_AVAILABLE_DEVICE", dataObj.getString("availdevices"));
                        map.put("KEY_SOLD_DEVICE", dataObj.getString("solddevices"));
                        map.put("KEY_TOTAL_DEVICE", dataObj.getString("totaldevices"));
                        companydataList.add(map);
                        companyList.add(dataObj.optString("companyname"));

                        RadioButton rbn = new RadioButton(PieNewChart.this);
                        rbn.setId(i+1);
                        rbn.setText(dataObj.getString("companyname"));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
                        rbn.setLayoutParams(params);
                        rgp.addView(rbn);

                    //}
                        AndyUtils.removeCustomProgressDialog();


                    }
                if(jarray.length() > 0) {
                    RadioButton selectedRB = (RadioButton) findViewById(1);
                    selectedRB.setChecked(true);
                }

            }catch (Exception ev){

            }

        }
    }

}



//                          http://209.190.15.26/andorsac/rest/CallService/companywisedevicecount?controlid=0&companyid=30001


