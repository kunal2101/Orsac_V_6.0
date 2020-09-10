package orsac.rosmerta.orsac_vehicle.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialog;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialogCallback;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Orsac_feedback;
import orsac.rosmerta.orsac_vehicle.android.Orsac.VehicelWise_Deatail;

public class SearchVehicleWise extends AppCompatActivity {
    TextView vehicle_textview, tot_avi , txt1 ,textView_version_name,etp_in_180,curr_etp,operational_truck,active_live,non_active;
    Button search_btn_main,msg_btn,rating_btn;
    ImageView serch_btn;
    String vehicelNo;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private AlertDialog internetDialog;
    private boolean isNetDialogShowing = false;
    LinearLayout lin_tot_install, lyn_active_vtu;
    String totalinstalled ="",msg_mob="";
    FrameLayout frame_floating,frame_device_managment;
    PreferenceHelper preferenceHelper;
    private SwipeRefreshLayout swipeContainer;
    TextView live_vtu;
    int total_i3ms_s=0;
    GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle_wise);

        tot_avi = (TextView) findViewById(R.id.tot_avi);
        etp_in_180=(TextView) findViewById(R.id.etp_in_180);
        live_vtu=(TextView) findViewById(R.id.live_vtu);
        operational_truck=(TextView) findViewById(R.id.operational_truck);
        active_live= (TextView) findViewById(R.id.active_live);
        non_active= (TextView) findViewById(R.id.non_active);
        rating_btn=(Button)findViewById(R.id.rating_btn) ;
        lyn_active_vtu = (LinearLayout) findViewById(R.id.lyn_active_vtu);
        frame_device_managment= (FrameLayout)findViewById(R.id.frame_device_managment);

        preferenceHelper = new PreferenceHelper(SearchVehicleWise.this);

        msg_btn=(Button)findViewById(R.id.msg_btn);
        frame_floating =(FrameLayout)findViewById(R.id.frame_floating);
        txt1 = (TextView) findViewById(R.id.txt1);
        textView_version_name =(TextView)findViewById(R.id.textView_version_name);
        lin_tot_install = (LinearLayout) findViewById(R.id.lin_tot_install);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(SearchVehicleWise.this, "Refresh", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 4000);
                try {
                    new preProcessDash().execute();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        tool_title.setText("Search Vehicle");
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
      /*  try {
            new Authentication().execute();
        } catch (Exception e) {
            e.getMessage();
        }*/
        try {
            PackageInfo pInfo = SearchVehicleWise.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            textView_version_name.setText("Powered By ORSAC - V-"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        serch_btn = (ImageView) findViewById(R.id.serch_btn);

        search_btn_main = (Button) findViewById(R.id.search_btn_main);
        vehicle_textview = (TextView) findViewById(R.id.vehicle_textview);

       // new install_appcount().execute();


        msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = vehicle_textview.getText().toString();
                msg = "VTS " + msg;
                if (!TextUtils.isEmpty(msg)) {
                    String no = preferenceHelper.getUser_id();
                        no = "9968447229";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:9220592205"));
                    sendIntent.putExtra("sms_body", msg);
                    startActivity(sendIntent);

                    //Getting intent and PendingIntent instance

                    /*Intent intent = new Intent(getApplicationContext(), Orsac_feedback.class);
                    intent.putExtra("send","1");
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    //Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(no, null, msg, pi, null);

                    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();*/

                } else {
                    Toast.makeText(SearchVehicleWise.this, "Please enter the vehicle number", Toast.LENGTH_SHORT).show();
                }
            }

        });
        search_btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicelNo = vehicle_textview.getText().toString();
                vehicelNo = vehicelNo.toUpperCase();
                if(vehicelNo.equalsIgnoreCase("negative_vijay")){
                    Intent inty = new Intent(SearchVehicleWise.this,Device_Manag_admin.class);
                    startActivity(inty);
                }
                if (!TextUtils.isEmpty(vehicelNo)) {
                    if (tot_avi.getText().toString().equals("-----")) {
                        Toast.makeText(SearchVehicleWise.this, "Please wait , Total VTU Installed Loading", Toast.LENGTH_SHORT).show();
                    }else {
                        if(vehicelNo.matches("[0-9]+")) {
                            Intent inty = new Intent(SearchVehicleWise.this, VehicelWise_Deatail.class);
                            inty.putExtra("vehicelNo", vehicelNo);
                            startActivity(inty);
                        }else {
                            Intent inty = new Intent(SearchVehicleWise.this, VehicelWise_Deatail.class);
                            inty.putExtra("vehicelNo", vehicelNo);
                            startActivity(inty);

                        }
                    }
                } else {
                    Toast.makeText(SearchVehicleWise.this, "Please enter the vehicle number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rating_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent inty = new Intent(SearchVehicleWise.this, VendorRating.class);
        startActivity(inty);
    }
});
        serch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_btn_main.performClick();
            }
        });
        frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(SearchVehicleWise.this, Orsac_feedback.class);
                startActivity(inty);
            }
        });
        lin_tot_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(totalinstalled.equalsIgnoreCase("")){
                Toast.makeText(SearchVehicleWise.this, "Please wait.......", Toast.LENGTH_SHORT).show();
            }else {
                //NavigationActivity
                Intent inty = new Intent(SearchVehicleWise.this, NavigationActivity.class);
                //Intent inty = new Intent(SearchVehicleWise.this, KK_LoginActivity_.class);
                startActivity(inty);
                //finish();
            }

               /* Intent inty = new Intent(SearchVehicleWise.this, KK_LoginActivity_.class);
                startActivity(inty);*/
            }
        });
        frame_device_managment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(SearchVehicleWise.this,Device_Manag_admin.class);
                startActivity(inty);

            }
        });


/*
        lyn_active_vtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnection()){
                    new getStaticDynamicCount().execute();
                }
            }
        });
*/

    }




    class getStaticDynamicCount extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(SearchVehicleWise.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(SearchVehicleWise.this, "", "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... params) {

            String getResponse = null;


            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(UrlContants.STATIC_DYNAMIC);
                HttpResponse httpResponse = httpClient.execute(httppost);
                getResponse = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception ev) {
                ev.getStackTrace();
            }

            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {
            try{
                JSONArray jsonArray_   =  new JSONArray(getResponse);

                   // for (int i = 0; i < jsonArray_.length(); i++) {

                        JSONObject jsonObj = jsonArray_.getJSONObject(0);

                        String dynamic = jsonObj.getString("sum");
                        String activeLive = active_live.getText().toString();
                        getDynamic(dynamic, activeLive);

                    //}
            }catch (Exception ev){
                System.out.print(ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) SearchVehicleWise.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public  void downloadcount(int count){
       final Dialog myDialog = new Dialog(SearchVehicleWise.this);
       myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialog_app_download);
        Button ok_close = (Button) myDialog.findViewById(R.id.btn_done);
        TextView txt_tot = (TextView) myDialog.findViewById(R.id.txt_tot);
        TextView txt_lweek = (TextView) myDialog.findViewById(R.id.txt_lweek);

        txt_tot.setText("Total Download : "+count);

        ok_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
        myDialog.show();

        Toast.makeText(this, " Total Download - 23446  Last Week - 1499", Toast.LENGTH_LONG).show();
    }

    class preProcessDash extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        ProgressBar pbHeaderProgress;

        int total_active=0;
        int total_inactive = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbHeaderProgress = (ProgressBar) findViewById(R.id.progress_bar);
            Log.d ( "Test Push","Test Push" );
            Log.d ( "Test Push","Test Push" );
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
              //  HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=0");
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls()+"/"+UrlContants.GET_MOB_DASH_DATA+"?dataRange=0");
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=0

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
                        JSONArray jsonArray_   =  new JSONArray(getResponse);

                        try {
                            for (int i = 0; i < jsonArray_.length(); i++) {

                                JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();


                                String posi = String.valueOf(i + 1);
                                admin_bean.setActive(jsonObj1.getString("active"));
                                admin_bean.setInactive(jsonObj1.getString("inactive"));

                                total_i3ms =total_i3ms+Integer.parseInt(jsonObj1.getString("i3msactive"));
                                last_90_days_st = jsonObj1.getString("last90");
                                total_etp = Integer.parseInt(jsonObj1.getString("okstatus")) + Integer.parseInt(jsonObj1.getString("notokstatus"));
                                active_etp =jsonObj1.getString("okstatus");
                                inactive_etp=jsonObj1.getString("notokstatus");
                                total_count = total_count + Integer.parseInt(jsonObj1.getString("active")) + Integer.parseInt(jsonObj1.getString("inactive"));
                                total_active = total_active + Integer.parseInt(jsonObj1.getString("active"));
                                total_inactive = total_inactive + Integer.parseInt(jsonObj1.getString("inactive"));
                            }
                            totalinstalled = total_i3ms+"";
                            etp_in_180.setText(last_90_days_st);
                            live_vtu.setText(""+total_etp);
                            tot_avi.setText(""+total_i3ms);
                            operational_truck.setText(total_i3ms - Integer.parseInt(last_90_days_st)+"");
                            active_live.setText(""+total_active);
                            // T_ETP_vs_T_Active.setText(inactive_etp+"/"+(total_i3ms  - total_active)+ "");
                            non_active.setText(total_i3ms  - total_active+"");
                            tot_avi.setVisibility(View.VISIBLE);
                            txt1.setVisibility(View.VISIBLE);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        }

    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
 /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "Action Down", Toast.LENGTH_SHORT).show();
            case MotionEvent.ACTION_MOVE:
               // Toast.makeText(this, "Action MOVE", Toast.LENGTH_SHORT).show();
            case MotionEvent.ACTION_UP:
                //Toast.makeText(this, "Action UP", Toast.LENGTH_SHORT).show();
        }
        return false;
    }*/
/*
    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        ProgressBar pbHeaderProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbHeaderProgress = (ProgressBar) findViewById(R.id.progress_bar);

           */
/* AndyUtils.showCustomProgressDialog(SearchVehicleWise.this,
                    "Fetching Information Please wait...", false, null);
*//*

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
                HttpPost httppost = new HttpPost("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getcompanydata?userid=10001");

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
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                             totalinstalled = jsonObj.getString("totalinstalled");
                            msg_mob = jsonObj.getString("mob");
                            preferenceHelper.putUser_id(msg_mob);
                            tot_avi.setText(totalinstalled);
                            tot_avi.setVisibility(View.VISIBLE);
                            txt1.setVisibility(View.VISIBLE);
                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    pbHeaderProgress.setVisibility(View.GONE);
                    tot_avi.setVisibility(View.VISIBLE);
                    txt1.setVisibility(View.VISIBLE);
                    */
/* AndyUtils.removeCustomProgressDialog();*//*

                    // progressDialog.dismiss();
                }
            });
        }

    }
*/

    public BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);

            if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
                removeInternetDialog();
            } else {
                if (isNetDialogShowing) {
                    return;
                }
                showInternetDialog();
            }
        }
    };

    private void removeInternetDialog() {
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
            isNetDialogShowing = false;
            internetDialog = null;
        }
    }

    private void showInternetDialog() {
        isNetDialogShowing = true;
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(this);
        internetBuilder.setCancelable(false);
        internetBuilder
                .setTitle(getString(R.string.dialog_no_internet))
                .setMessage(getString(R.string.dialog_no_inter_message))
                .setPositiveButton(getString(R.string.dialog_enable_3g),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete

                                Intent intent = new Intent(
                                        Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                removeInternetDialog();
                                finish();
                            }
                        })
                .setNeutralButton(getString(R.string.dialog_enable_wifi),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // User pressed Cancel button. Write
                                // Logic Here
                                startActivity(new Intent(
                                        Settings.ACTION_WIFI_SETTINGS));
                                removeInternetDialog();
                                finish();
                            }
                        })
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeInternetDialog();
                                finish();
                                internetDialog.dismiss();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) SearchVehicleWise.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);

        if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
            try {
                if (tot_avi.getText().toString().equalsIgnoreCase("-----")){
                    new preProcessDash().execute();

                }

            } catch (Exception e) {
                e.getMessage();
            }
        }
        registerReceiver(internetConnectionReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnectionReciever);
    }

/*
    class install_appcount extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getAppInstallCount?data1=OMVTS1");

                HttpResponse response = httpclient.execute(httpGet);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {

                    try {
                        JSONObject jsonObj_   =  new JSONObject(getResponse);


                        String status = jsonObj_.getString("Status");

                       int count = Integer.parseInt(jsonObj_.getString("Message")) + 23768 ;


                        //downloadcount(count);
                        Toast.makeText(SearchVehicleWise.this, " Total Download --  "+count, Toast.LENGTH_LONG).show();

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                }
            });
        }

    }
*/



    public void getDynamic(String dynamic, String active){
        final Dialog myDialog = new Dialog(SearchVehicleWise.this);
        myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialogbox);

        TextView txt_title                   = (TextView) myDialog.findViewById(R.id.txt_title);
        TextView txt_dash_totalvehicle       = (TextView)myDialog.findViewById(R.id.txt_dash_totalvehicle);
        TextView  txt_dash_idlevehicle       = (TextView)myDialog.findViewById(R.id.txt_dash_idlevehicle);
        TextView  txt_dash_runnvehicle       = (TextView)myDialog.findViewById(R.id.txt_dash_runnvehicle);
        TextView txt_dash_stopvehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_stopvehicle);
        TextView txt_dash_nonpollvehicle     = (TextView)myDialog.findViewById(R.id.txt_dash_nonpollvehicle);
        TextView txtRefreshTimestamp         = (TextView)myDialog.findViewById(R.id.txtRefreshTimestamp);
        LinearLayout total_veh               = (LinearLayout)myDialog.findViewById(R.id.total_veh);
        LinearLayout running_veh             = (LinearLayout)myDialog.findViewById(R.id.running_veh);
        LinearLayout stop_veh                = (LinearLayout)myDialog.findViewById(R.id.stop_veh);
        LinearLayout halt_veh                = (LinearLayout)myDialog.findViewById(R.id.halt_veh);
        LinearLayout nonpolling_veh          = (LinearLayout)myDialog.findViewById(R.id.nonpolling_veh);
        TextView ok_close = (TextView)myDialog.findViewById(R.id.ok_close);

        txt_dash_totalvehicle.setText(active);
        txt_title.setText("Truck with active VTUs");

        if(Integer.parseInt ( dynamic )< 1){
            txt_dash_stopvehicle.setText("---");

        }else{
            txt_dash_stopvehicle.setText(dynamic);

        }

        int v_static =  Integer.parseInt(active) - Integer.parseInt(dynamic);

        if(v_static< 1){
            txt_dash_runnvehicle.setText("---");

        }else{
            txt_dash_runnvehicle.setText(""+v_static);

        }



        ok_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        /*
        total_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("All");
            }
        });
        running_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("running");
            }
        });
        halt_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("idle");
            }
        });
        stop_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("stop");
            }
        });
        nonpolling_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("nonpolling");
            }
        });*/

        /*txt_dash_totalvehicle.setText(totalVehicles);
        txt_dash_idlevehicle.setText(nonpollingVehicles);
        txt_dash_runnvehicle.setText(runningVehicles);
        txt_dash_stopvehicle.setText(stopVehicles);
        txt_dash_nonpollvehicle.setText(temperVehicles);*/


       /* Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        txtRefreshTimestamp.setText(formattedDate);*/
        myDialog.show();
    }

    private void prettyDialog(final String type){
        final PrettyDialog dialog = new PrettyDialog(this);
        dialog
                .setTitle("Choose View Option")
                .setMessage("")
                .setIcon(R.drawable.pdlg_icon_info,R.color.pdlg_color_blue,null)
                .addButton("List view", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                        /*Intent intent = new Intent(SearchVehicleWise.this, AllVehiclelistActivity.class);
                        intent.putExtra("vehType", type);
                        startActivity(intent);*/
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("Map View", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                       /* Intent intent = new Intent(TestActivity.this, StatusVehiclemapActivity.class);
                        intent.putExtra("vehType", type);
                        startActivity(intent);*/

                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"Cancel selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                });
        dialog.show();
    }

}
