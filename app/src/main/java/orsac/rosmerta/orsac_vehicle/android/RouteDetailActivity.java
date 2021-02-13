package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import orsac.rosmerta.orsac_vehicle.android.textview.TextView_Lato;
import orsac.rosmerta.orsac_vehicle.android.textview.Textview_lato_thin;
import orsac.rosmerta.orsac_vehicle.android.adapter.RouteDetailsAdapter;

public class RouteDetailActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> getDatalist;
    private RecyclerView mRecyclerView;
    RouteDetailsAdapter reportHistory ;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    PreferenceHelper preferenceHelper;
    String etpno = "";
CardView noPosts;
TextView_Lato etp_no ;
    Textview_lato_thin imeino,veh_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        etp_no = (TextView_Lato)findViewById(R.id.etp_no);
        imeino =  (Textview_lato_thin)findViewById(R.id.imeino) ;
        veh_no = (Textview_lato_thin)findViewById(R.id.veh_no);
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("Running Route");
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerview);
        noPosts = (CardView)findViewById(R.id.noPosts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (RouteDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        preferenceHelper = new PreferenceHelper(this);
        try {
            Intent inty = getIntent();
            etpno = inty.getStringExtra("etpNo");
        }catch (Exception Ev){
            Ev.getMessage();

        }
        if (isConnection()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new getReport().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new getReport().execute();
            }
        }

    }
    class getReport extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog = new ProgressDialog(RouteDetailActivity.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(RouteDetailActivity.this, "", "Please Wait...", true);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog){
                    /****cleanup code****/
                    finish();

                }});
        }

        @Override
        protected String doInBackground(String... params) {

          try {
               //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getETPWiseTrack?etp=L101903621/23
                HttpClient httpClient = new DefaultHttpClient();
               // HttpGet httpget = new HttpGet(preferenceHelper.getUrls() + "/getETPWiseTrack?etp=L101902694/8" );
              HttpGet httpget = new HttpGet(preferenceHelper.getUrls() + "/getETPWiseTrack?etp=" + etpno );

              // HttpGet httpget = new HttpGet("http://raplsouth.com/AIS140APIAlert_new/AIS140.svc/alert?clientid=1088&orgid=39&usertype=3&dealerid=99&vehRegNo=MA3FJEB1S00C19088&fromTimestamp=2019-09-17%2000:00&toTimestamp=2019-09-17%2023:59");


                HttpResponse httpResponse = httpClient.execute(httpget);
                getResponse = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception ev) {
                ev.getStackTrace();
            }

            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {

            try {
                getDatalist = new ArrayList<>();
                //getDatalist.clear();
                //JSONObject jsonObject = new JSONObject(getResponse);
                JSONArray jsonArray = new JSONArray(getResponse);

                    //JSONArray jsDataArr = jsonObject.getJSONArray("");
                JSONObject jsObj_ = jsonArray.getJSONObject(0);
                etp_no.setText("ETP NO :- " + jsObj_.getString("etp"));
                imeino.setText(jsObj_.getString("imei"));
                veh_no.setText("Vehicle No :- " + jsObj_.getString("veh"));

                    for(int aind = 0 ; aind < jsonArray.length(); aind++){
                        JSONObject jsObj = jsonArray.getJSONObject(aind);

                        HashMap<String,String> map = new HashMap<>();
                        map.put("datetime",jsObj.getString("datetime"));
                        map.put("ignition",jsObj.getString("ignition"));
                        map.put("LAT",jsObj.getString("lat"));
                        map.put("LNG",jsObj.getString("lon"));
                        //map.put("VehicleRegNo",jsObj.getString("InternalBatteryChargingStatus"));
                        // map.put("DateTimeStart",jsObj.getString("MainBatteryStatus"));
                        map.put("dist",jsObj.getString("dist"));



                          getDatalist.add(map);
                    }



            } catch (Exception ev) {
                ev.getStackTrace();
            }
            if(getDatalist.size() > 0) {
                reportHistory = new RouteDetailsAdapter(RouteDetailActivity.this, getDatalist, mRecyclerView);
                mRecyclerView.setAdapter(reportHistory);

            }else{
                Toast.makeText(RouteDetailActivity.this, "No data found",Toast.LENGTH_LONG).show();
                noPosts.setVisibility(View.VISIBLE);
            }

            progressDialog.dismiss();
        }
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) RouteDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}
