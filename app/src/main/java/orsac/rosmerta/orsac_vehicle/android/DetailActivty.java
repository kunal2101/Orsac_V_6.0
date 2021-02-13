package orsac.rosmerta.orsac_vehicle.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.orsac.AllTracking;
import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.orsac.MapTrackingActivity;

/**
 * Created by Diwash Choudhary on 2/6/2017.
 */
public class DetailActivty extends Activity implements View.OnClickListener {
    LinearLayout pie_chart, half_pie;
    TextView mapview;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private ImageView icon_tool;
    String etpno;
    MyTextView etp_title, code_title, source, destination, dates;
    TextView start_day, valid_date, circles, min_name, quitity, vehi_num, devi_id, tranpo_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orsac_admin_third_deta_copy);
        Intent inty = getIntent();
        etpno = inty.getStringExtra("etpNo");
        etp_title = (MyTextView) findViewById(R.id.etp_title);
        code_title = (MyTextView) findViewById(R.id.code_title);
        source = (MyTextView) findViewById(R.id.source);
        destination = (MyTextView) findViewById(R.id.destination);
        start_day = (TextView) findViewById(R.id.start_day);
        valid_date = (TextView) findViewById(R.id.valid_date);
        circles = (TextView) findViewById(R.id.circles);
        quitity = (TextView) findViewById(R.id.quitity);
        vehi_num = (TextView) findViewById(R.id.vehi_num);
        devi_id = (TextView) findViewById(R.id.devi_id);
        tranpo_name = (TextView) findViewById(R.id.tranpo_name);

        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        icon_tool = (ImageView) findViewById(R.id.icon_tool);
       // icon_tool.setImageResource(R.drawable.ic_oie_transparent_q);
        icon_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivty.this, AllTracking.class);
                intent.putExtra("etpno",etpno);
                startActivity(intent);
            }
        });
        new Authentication().execute();
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText(" Detail ");
       /* pie_chart = (LinearLayout)findViewById(R.id.pie_chart);
        half_pie = (LinearLayout)findViewById(R.id.half_pie);
        mapview= (TextView)findViewById(R.id.mapview);
        pie_chart.setOnClickListener(this);
        half_pie.setOnClickListener(this);
        mapview.setOnClickListener(this);
*/
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(DetailActivty.this,
                    "Loading...", false, null);

          /*  progressDialog.setMessage("Loading....");
            progressDialog.show();
          */  // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;

            /*PreferenceHelper preferenceHelper = new PreferenceHelper(ActivityLogin.this);
            try{
                getDeviceToken = preferenceHelper.getGcm_id();
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                getDeviceToken = "";
            }*/

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("username", user_names));
            nameValuePairs.add(new BasicNameValuePair("password", passwords));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/etpwiselive?etpno=" + etpno);

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
                        JSONArray jsonArray = new JSONArray(getResponse);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        etp_title.setText("ETP NO - " + jsonObject.getString("etpno"));
                        code_title.setText(jsonObject.getString("code"));
                        source.setText(jsonObject.getString("source"));
                        destination.setText(jsonObject.getString("destination"));
                        start_day.setText(" Start Time -" + jsonObject.getString("starttime"));
                        valid_date.setText("Valid Upto - " + jsonObject.getString("passvalid"));
                        circles.setText(jsonObject.getString("circle"));
                        min_name.setText(jsonObject.getString("minename"));

                        quitity.setText(jsonObject.getString("quantity"));
                        vehi_num.setText(jsonObject.getString("vehicleno"));
                        devi_id.setText(jsonObject.getString("deviceid"));
                        tranpo_name.setText(jsonObject.getString("tranpo_name"));

                        AndyUtils.removeCustomProgressDialog();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                }
            });
            AndyUtils.removeCustomProgressDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case  R.id.pie_chart:
                Intent inty = new Intent(DetailActivty.this,PieChart.class);
                startActivity(inty);
                break;
            case  R.id.half_pie:
                Intent intent = new Intent(DetailActivty.this,HalfPieChartActivity.class);
                startActivity(intent);
                break;*/
            case R.id.mapview:
                Intent intent = new Intent(DetailActivty.this, MapTrackingActivity.class);
                intent.putExtra("etpno",etpno);
                startActivity(intent);
                break;


        }
    }
}
