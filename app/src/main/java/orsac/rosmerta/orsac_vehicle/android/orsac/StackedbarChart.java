package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by qr on 20/3/17.
 */
public class StackedbarChart extends AppCompatActivity {

    List<ChartData> value;
    //StackedBarChart stackBarChart;
    Float[] value3,value4;
    String[] companyname ={"COMP1","COMP2","jhgxfjc","jbjbj","jbjbj"};
    String[] symbol = {"A","B","C","D","E","F","G","H"};
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    ArrayList<String> msgArr = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stackedbar);
            tool_title = (MyTextView) findViewById(R.id.tool_title);
            tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
            tool_title.setText("Device Sale Details");
            tool_back_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onbackClick();
                }
            });
            Bundle b=this.getIntent().getExtras();
            float[] array= {12,23,45,87,89 };
            float[] array_int={112,123,415,187,189 };

//            float[] array=b.getFloatArray("key_avail");
//            float[] array_int=b.getFloatArray("key_inst");
           // companyname =b.getStringArray("key_comp");

           // stackBarChart = (StackedBarChart) findViewById(R.id.chart);

             value = new ArrayList<>();
            value3 = new Float[array.length];
            value4 = new Float[array_int.length];

            for(int ind=0;ind<array.length;ind++){
               float temp = array[ind];
                float temp_int = array_int[ind];
               value3[ind]=temp;
                value4[ind]=temp_int;
            }

            Float[] value1 = {5f, 10f, 16f, 10f, 4f};
            Float[] value2 = {70f, 10f, 6f, 2f, 5f};

            List<String> h_lables = new ArrayList<>();
            for(int ain = 0 ; ain < companyname.length; ain++){
                h_lables.add(symbol[ain]);

                String temp = symbol[ain]+" - "+companyname[ain];
                msgArr.add(temp);
            }


            TextView description = (TextView)findViewById(R.id.description);
            description.setText(TextUtils.join("\n",msgArr));

           /* value.add(new ChartData(value3, "Available"));
            value.add(new ChartData(value4, "Installed"));

            stackBarChart.setHorizontal_label(h_lables);
            stackBarChart.setData(value);
            stackBarChart.setHorizontalStckedBar(false);
            stackBarChart.setDescription("");
stackBarChart.animate();
            stackBarChart.getAnimation();*/
            //setGesture
        }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    /*class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(StackedbarChart.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    Float[] value1 = {5f, 10f, 16f, 10f, 4f};
                    Float[] value2 = {70f, 10f, 6f, 2f, 5f};

                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        try {
                            value = new ArrayList<>();
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                            for (int aind = 0; aind < jsonArray.length(); aind++) {
                                JSONObject jsObj = jsonArray.getJSONObject(aind);
                                String availabledevices = jsObj.getString("availabledevices");
                                String installedDevices = jsObj.getString("installedDevices");
                            }
                            stackBarChart = (StackedBarChart) findViewById(R.id.chart);
                            List<String> h_lables = new ArrayList<>();
                            h_lables.add("A");
                            h_lables.add("B");
                            h_lables.add("C");
                            h_lables.add("D");
                            h_lables.add("E");

                            value.add(new com.numetriclabz.numandroidcharts.ChartData(value1, "Available"));
                            value.add(new com.numetriclabz.numandroidcharts.ChartData(value2, "Installed"));

                            stackBarChart.setHorizontal_label(h_lables);
                            stackBarChart.setData(value);
                            stackBarChart.setHorizontalStckedBar(true);
                            stackBarChart.setDescription("");

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                    progressDialog.dismiss();
                }
            });
        }

    }*/   /*Testing Asyn same work like Login_info method */

}
