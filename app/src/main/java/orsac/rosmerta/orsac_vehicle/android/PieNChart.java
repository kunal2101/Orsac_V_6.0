package orsac.rosmerta.orsac_vehicle.android;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;

/**
 * Created by Diwash Choudhary on 3/18/2017.
 */
public class PieNChart extends AppCompatActivity implements View.OnClickListener {
    ImageView home_page;
    private List<Admin_model> array_admin;
    Admin_model admin_model;

    Map map;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private TextView[] dots;
    private AdminPage.MyViewPagerAdapter myViewPagerAdapter;
    String imageUri, imageUri_one, imageUri_two, imageUri_three, imageUri_four;
    int[] imags = {R.drawable.fasttra_two, R.drawable.rose_six, R.drawable.trimmble_three, R.drawable.atlanata_three, R.drawable.arya_three, R.drawable.etrans_two};
    static int i = 0;
    ArrayList<HashMap<String, String>> GetCompanyDetail;
    private GridLayoutManager lLayout;
    private ArrayList<Admin_model> companyList;
    String totalcompanies, totaltripcountcnt, totaldevices, totalinstalled, installedDevices, companyName, totalDevices, availabledevices, tripcount;
    DrawerLayout drawer;
    float value_avai[];
    String value_inst[];
    String value_comap[];
    double[] tripCount;
    String[] compy_name;
    Handler mHandler = new Handler();
    String tot_dev;
    PieChart pieChart;
    TextView fast, rose, trimble, etrans, arya, atlanta,itrangle,ce_sys;
RelativeLayout home_rela;
    String getTemp;
    ArrayList<String> labels;
    int getindex;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_new_activity_twenty_fourthireen_april);
        home_page = (ImageView) findViewById(R.id.home_page);
        home_rela=(RelativeLayout)findViewById(R.id.home_rela);
        fast = (TextView) findViewById(R.id.fast);
        rose = (TextView) findViewById(R.id.rose);
        trimble = (TextView) findViewById(R.id.trimble);
        etrans = (TextView) findViewById(R.id.etran);
        arya = (TextView) findViewById(R.id.arya);
        atlanta = (TextView) findViewById(R.id.atlanta);
        itrangle= (TextView) findViewById(R.id.itrangle);
        ce_sys =(TextView)findViewById(R.id.ce_sys);
        home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PieNChart.this, NavigationActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(PieNChart.this, R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(myIntent, options.toBundle());
            }
        });
        home_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      Intent inty = new Intent(PieNChart.this, NavigationActivity.class);
//                overridePendingTransition( R.anim.move_right_out_activity,R.anim.move_left_in_activity);
                startActivity(inty);
                finish();*/
                Intent myIntent = new Intent(PieNChart.this, NavigationActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(PieNChart.this, R.anim.fade_in, R.anim.fade_out);
                startActivity(myIntent, options.toBundle());
            }
        });
        new Authentication().execute();
        pieChart = (PieChart) findViewById(R.id.chart);
        pieChart.setOnClickListener(PieNChart.this);
        pieChart.setTouchEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setCenterTextSizePixels(BIND_IMPORTANT);
        //pieChart.setCenterTextSize(20);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                String getValue = String.valueOf(e.getVal());
            getindex = e.getXIndex();
                getTemp = labels.get(getindex);
               // Toast.makeText(PieNChart.this, "Company Name:" + getTemp + " Installed Device : " + value_inst[getindex], Toast.LENGTH_SHORT).show();
                comapny_detail();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(14f, 0));
        entries.add(new Entry(20f, 1));
        entries.add(new Entry(14f, 2));
        entries.add(new Entry(14f, 3));
        entries.add(new Entry(14f, 4));
        entries.add(new Entry(20f, 5));
        entries.add(new Entry(14f, 6));
        entries.add(new Entry(14f, 7));

        PieDataSet dataset = new PieDataSet(entries, "");

        labels = new ArrayList<String>();
        labels.add("CE Info Systems Pvt. Ltd.");
        labels.add("iTriangle Infotech Pvt.");
        labels.add("Atlanta");
        labels.add("Arya Omnitalk Wireless Solution Pvt");


        labels.add("Fastrackerz");
        labels.add("Rosmerta Autotech Private Ltd");
        labels.add("Trimble Mobility Solutions India Pvt.");
        labels.add("eTrans Solutions Private");
        PieData data = new PieData(labels, dataset);

        int[] piecharColor = {Color.parseColor("#BF1A21"),
                Color.parseColor("#18B086"),
                Color.parseColor("#FF9901"),
                Color.parseColor("#008081"),
                Color.parseColor("#CCDC39"),
                Color.parseColor("#607D8B"),
                Color.parseColor("#9C27B0"),

                Color.parseColor("#a854d4"),
                Color.parseColor("#F68159")};

        dataset.setColors(piecharColor);
        pieChart.setDescription("");
        pieChart.setData(data);

        pieChart.animateY(5000);

        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
        pieChart.setDrawSliceText(false);
        data.setDrawValues(false);
        pieChart.getLegend().setEnabled(false);

    }

    @Override
    public void onClick(View v) {

    }
private  void  comapny_detail(){
    builder = new AlertDialog.Builder(PieNChart.this);
    builder .setTitle("  Installed Device Detail")

            .setMessage(getTemp + " -- " + value_inst[getindex])
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            /*    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })*/
            .setIcon(R.drawable.ic_launcher);
    builder.show();

}
    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AndyUtils.showCustomProgressDialog(PieNChart.this,
                    "Fetching Information Please wait...", true, null);
            
         /*   progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*/
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

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
                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        HashMap<String, String> map = new HashMap<>();
                        try {
                            tot_dev = jsonObj.getString("totalinstalled");

                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                            value_avai = new float[jsonArray.length()];
                            value_comap = new String[jsonArray.length()];
                            value_inst = new String[jsonArray.length()];
                            tripCount = new double[jsonArray.length()];
                            compy_name = new String[jsonArray.length()];
                            // value_inst = new Float[jsonArray.length()];
                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();
                                value_inst[i] = jsonObj1.getString("installedDevices");
                                //value_comap[i] = jsonObj1.getString("companyName");
                                //array_admin.add(admin_bean);
                               /* installedDevices = jsonObj1.getString("installedDevices");
                                companyName = jsonObj1.getString("companyName");
                                totalDevices = jsonObj1.getString("totalDevices");
                                availabledevices = jsonObj1.getString("availabledevices");
                                tripcount = jsonObj1.getString("tripcount");
                                tripCount[i] = Double.valueOf(tripcount);
                                compy_name[i] = companyName;
                                companyList.add(admin_bean);
*/
                            }
                            pieChart.setCenterText(tot_dev);
                            ce_sys.setText(value_inst[0]);
                            itrangle.setText(value_inst[1]);
                            arya.setText(value_inst[3]);
                            atlanta.setText(value_inst[2]);
                          //  trimble.setText(value_inst[2]);
                            fast.setText(value_inst[4]);
                            rose.setText(value_inst[5]);
                            trimble.setText(value_inst[6]);
                            etrans.setText(value_inst[7]);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */

}