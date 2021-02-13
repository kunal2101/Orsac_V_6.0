package orsac.rosmerta.orsac_vehicle.android.orsac;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.textview.TextView_Lato;
import orsac.rosmerta.orsac_vehicle.android.textview.Textview_lato_thin;
import orsac.rosmerta.orsac_vehicle.android.adapter.RouteDeviationAdapter;

public class RouteDeviationList extends AppCompatActivity {

    ArrayList<HashMap<String,String>> getDatalist;
    private RecyclerView mRecyclerView;
    RouteDeviationAdapter reportHistory ;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    PreferenceHelper preferenceHelper;
    String etpno = "", vehicleNo = "", tripStartTime = "", source = "", destination = "";
    CardView noPosts;
    TextView_Lato etp_no ;
    Textview_lato_thin imeino,veh_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_deviation_list);

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
        tool_title.setText("Deviation Route");
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerview);
        noPosts = (CardView)findViewById(R.id.noPosts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (RouteDeviationList.this, LinearLayoutManager.VERTICAL, false));
        preferenceHelper = new PreferenceHelper(this);

        try {

            Intent inty = getIntent();
            etpno = inty.getStringExtra("etpNo");
            vehicleNo = inty.getStringExtra("vehicleNo");
            tripStartTime = inty.getStringExtra("tripStartDateTime");
            source = inty.getStringExtra("source");
            destination = inty.getStringExtra("destination");
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
        String imie = "";
        ProgressDialog progressDialog = new ProgressDialog(RouteDeviationList.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(RouteDeviationList.this, "", "Please Wait...", true);
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
            JSONObject jo = null;

            try{
                jo = new JSONObject();
                jo.put("etpNo",  etpno);
                jo.put("vehicleNo", vehicleNo);
                jo.put("tripStartDateTime",  tripStartTime);
                jo.put("source", source);
                jo.put("destination",  destination);
                jo.put("plottingData", false);

            }catch (Exception ev){
                System.out.print(ev.getMessage());
            }

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpget = new HttpPost(preferenceHelper.getUrls() + "/routeDeviation");
                StringEntity entity = new StringEntity(jo.toString(), HTTP.UTF_8);
                entity.setContentType("application/json");
                httpget.setEntity(entity);
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
                JSONObject jsonObject = new JSONObject(getResponse);
                if (jsonObject.getString("message").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray innerArray = jsonArray.getJSONObject(0).getJSONArray("events");

                    for (int aind = 0; aind < innerArray.length(); aind++) {
                        JSONObject jsObj = innerArray.getJSONObject(aind);
                        imie = jsObj.getString("imeiNo");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("vehicleNo", jsObj.getString("vehicleNo"));
                        map.put("imeiNo", jsObj.getString("imeiNo"));
                        map.put("etpNo", jsObj.getString("etpNo"));
                        map.put("etpStartTime", jsObj.getString("etpStartTime"));
                        map.put("etpEndTime", jsObj.getString("etpEndTime"));
                        map.put("source", jsObj.getString("source"));
                        map.put("desitnation", jsObj.getString("desitnation"));
                        map.put("routeDevStartTime", jsObj.getString("routeDevStartTime"));
                        map.put("routeDevEndTime", jsObj.getString("routeDevEndTime"));
                        map.put("routeDevDuration", jsObj.getString("routeDevDuration"));
                        getDatalist.add(map);
                    }
                    etp_no.setText("ETP No. - " +etpno);
                    imeino.setText(imie);
                    veh_no.setText("Vehicle : "+vehicleNo);

                }
            } catch (Exception ev) {
                ev.getStackTrace();
            }
            if(getDatalist.size() > 0) {
                reportHistory = new RouteDeviationAdapter(RouteDeviationList.this, getDatalist);
                mRecyclerView.setAdapter(reportHistory);

            }else{
                Toast.makeText(RouteDeviationList.this, "No data found",Toast.LENGTH_LONG).show();
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
        ConnectivityManager manage = (ConnectivityManager) RouteDeviationList.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}