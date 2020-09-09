package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Timer;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;

public class VehicelWise_Map extends AppCompatActivity  {
    SupportMapFragment fm;
    GoogleMap googleMap;
   /* static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);*/

    static final LatLng origin = new LatLng(28.5355, 77.3910);
    static final LatLng dest = new LatLng(28.5562, 77.1000);
    ArrayList<LatLng> points = null;
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    Timer timer = new Timer();
    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 10 sec.
    int counter = 0;
    Marker mDesc = null;
    LatLng previousLat;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    MyTextView etp_title, code_title, source, destination, dates, ign, gps, vehic_no,
            circle_name, startTime_new, pass_valid_new;
    TextView min_name_text, quitity_tex, txt_speed, dir_text_new;
    String st_etp,st_code_title, st_source, st_destination,  st_vehic_no,
            st_circle_name, st_startTime_new, st_pass_valid_new,st_min_name_text, st_quitity_tex, st_txt_speed, st_dir_text_new;
    CountDownTimer countDownTimer ;
    String etpno;
    PreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicel_wise__map);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Tracking");
        Intent inty = getIntent();
        etpno = inty.getStringExtra("vehicelNo");
        preferenceHelper = new PreferenceHelper(this);


        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //fm.getMapAsync((OnMapReadyCallback) this);
        googleMap = fm.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setPadding(0, 0, 0, 100);
        if (isConnection()) {
            new Authentication().execute();
        } else {
            Toast.makeText(VehicelWise_Map.this, "No Internet , Please Try After sometime", Toast.LENGTH_LONG).show();
        }
        countDownTimer = new CountDownTimer(20000, 20000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(counter > 0) {
                   // Toast.makeText(VehicelWise_Map.this, "Countre "+counter, Toast.LENGTH_SHORT).show();
                    new update_Authentication().execute();
                    start();
                }
            }
        };

    }
    @Override
    protected void onStop() {
        super.onStop();
        isCanceled = true;
        countDownTimer.cancel();
        finish();
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    class update_Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*AndyUtils.showCustomProgressDialog(VehicelWise_Map.this,
                    "Loading...", false, null);*/

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
            //    HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=" + etpno);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+"getlivevehicles?vehicleno=" + etpno);

//preferenceHelper.getUrls() +"/"+
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
                        if(jsonObject.getString("latitude").equalsIgnoreCase("null")){
                            isCanceled = true;

                            AlertDialog.Builder builder = new AlertDialog.Builder(VehicelWise_Map.this);
                            builder.setMessage("NO DATA FOUND WITH THIS ETP NO")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                          /*  Intent inty = new Intent( MapTrackingActivity_Circle.this, DetailActivty_Sec.class);
                                            startActivity(inty);*/
                                            dialog.cancel();
                                            isCanceled = true;
                                            countDownTimer.cancel();
                                            // progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("ORSAC ");
                            alert.show();


                            Toast.makeText(VehicelWise_Map.this, "NO DATA FOUND WITH THIS ETP NO", Toast.LENGTH_LONG).show();
                        }else {
                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));
                            String sl_no= jsonObject.getString("unique_serial_id");
                            LatLng currentLat = new LatLng(latitude, longitude);

                            /*st_etp = jsonObject.getString("etpno");
                            st_circle_name = jsonObject.getString("circle");
                            st_vehic_no = jsonObject.getString("vehicleno");
                            st_txt_speed = jsonObject.getString("speed") + " Km";
                            st_dir_text_new = jsonObject.getString("direction");
                            st_source = jsonObject.getString("source");
                            st_destination = jsonObject.getString("destination");
                            st_startTime_new = jsonObject.getString("starttime");
                            st_min_name_text = jsonObject.getString("minename");
                            st_quitity_tex = jsonObject.getString("quantity") + " Ton";
                            st_pass_valid_new = jsonObject.getString("passvalid");
                            st_code_title = jsonObject.getString("code");
                            */
                            //  progressDialog.dismiss();

                            if (counter == 0) {
                                Marker mOrigin = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No/SL No :-" + etpno+"/"+sl_no)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_red)));
                                countDownTimer.start();
                            } else {
                                if (mDesc != null) {
                                    mDesc.remove();
                                }

                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No :-" + etpno)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));
                                //  Toast.makeText(MapTrackingActivity_Circle.this, "" + previousLat, Toast.LENGTH_LONG).show();
                                googleMap.addPolyline(new PolylineOptions()
                                        .add(previousLat, currentLat)

                                        .width(5).color(Color.BLUE).geodesic(true));
                            }

                           // googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLat));
                            previousLat = currentLat;
                            counter++;
                            // timerclass();
                        }
                        try{

                            if(jsonObject.has("slatitude")){
                                //get Value of video 20.307515,85.8090322


                                double latitude = Double.parseDouble(jsonObject.getString("slatitude"));
                                double longitude = Double.parseDouble(jsonObject.getString("slongitude"));

                                //   String sl_no= jsonObject.getString("source");
                                LatLng currentLat = new LatLng(latitude, longitude);
                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Source Address :-" + jsonObject.getString("source"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_g)));
                                double dlatitude = Double.parseDouble(jsonObject.getString("dlatitude"));
                                double dlongitude = Double.parseDouble(jsonObject.getString("dlongitude"));
                                //   String sl_no= jsonObject.getString("source");
                                LatLng dcurrentLat = new LatLng(dlatitude, dlongitude);
                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(dcurrentLat)
                                        .title("destination Address :-" + jsonObject.getString("destination"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_r)));
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(currentLat);
                                builder.include(dcurrentLat);
                                LatLngBounds bounds = builder.build();

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);

                                googleMap.animateCamera(cu);
                            }


                        }catch (Exception e){
                            e.getMessage();

                        }
                       /* etp_title.setText("ETP NO - " + jsonObject.getString("etpno"));
                        if(jsonObject.getString("etpno") == "NA"){
                            Toast.makeText(VehicelWise_Map.this, "No ETPNo assign with this vehicle No .", Toast.LENGTH_SHORT).show();
                        }
                        code_title.setText(jsonObject.getString("unitid"));
                        source.setText(jsonObject.getString("source"));
                        destination.setText(jsonObject.getString("destination"));
                        start_day.setText(jsonObject.getString("starttime"));
                        valid_date.setText(jsonObject.getString("trackdate"));
                        circles.setText(jsonObject.getString("company"));
                        min_name.setText("MINERAL NAME :" + jsonObject.getString("minename"));
                        quitity.setText("QUANTITY :" + jsonObject.getString("quantity")+ " Ton");
                        vehi_num.setText(jsonObject.getString("vehical_no"));
                        //  devi_id.setText(jsonObject.getString("deviceid"));
                        // tranpo_name.setText(jsonObject.getString("tranpo_name"));
                        credit.setText("SPEED :"+jsonObject.getString("speed")+ " Km");
                        dir_text.setText("DIRECTION : "+jsonObject.getString("headings"));
                        pick_time.setText(jsonObject.getString("datereceived"));
                        pick_date.setText(jsonObject.getString("vtype"));
                        ign.setText(jsonObject.getString("ign"));
                        gps.setText(jsonObject.getString("gpsstatus"));
*/


                     //   AndyUtils.removeCustomProgressDialog();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                   // AndyUtils.removeCustomProgressDialog();
                }
            });
           // AndyUtils.removeCustomProgressDialog();
        }
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(VehicelWise_Map.this,
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
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+"getlivevehicles?vehicleno=" + etpno);

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
                        if(jsonObject.getString("latitude").equalsIgnoreCase("null")){
                            isCanceled = true;

                            AlertDialog.Builder builder = new AlertDialog.Builder(VehicelWise_Map.this);
                            builder.setMessage("NO DATA FOUND WITH THIS ETP NO")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                          /*  Intent inty = new Intent( MapTrackingActivity_Circle.this, DetailActivty_Sec.class);
                                            startActivity(inty);*/
                                            dialog.cancel();
                                            isCanceled = true;
                                            countDownTimer.cancel();
                                           // progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("ORSAC ");
                            alert.show();


                            Toast.makeText(VehicelWise_Map.this, "NO DATA FOUND WITH THIS ETP NO", Toast.LENGTH_LONG).show();
                        }else {
                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));
                            String sl_no= jsonObject.getString("unique_serial_id");
                            LatLng currentLat = new LatLng(latitude, longitude);

                            /*st_etp = jsonObject.getString("etpno");
                            st_circle_name = jsonObject.getString("circle");
                            st_vehic_no = jsonObject.getString("vehicleno");
                            st_txt_speed = jsonObject.getString("speed") + " Km";
                            st_dir_text_new = jsonObject.getString("direction");
                            st_source = jsonObject.getString("source");
                            st_destination = jsonObject.getString("destination");
                            st_startTime_new = jsonObject.getString("starttime");
                            st_min_name_text = jsonObject.getString("minename");
                            st_quitity_tex = jsonObject.getString("quantity") + " Ton";
                            st_pass_valid_new = jsonObject.getString("passvalid");
                            st_code_title = jsonObject.getString("code");
                            */
                          //  progressDialog.dismiss();

                            if (counter == 0) {
                                Marker mOrigin = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No/SL No :-" + etpno+"/"+sl_no)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_red)));
                                countDownTimer.start();
                            } else {
                                if (mDesc != null) {
                                    mDesc.remove();
                                }

                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No :-" + etpno)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));
                                //  Toast.makeText(MapTrackingActivity_Circle.this, "" + previousLat, Toast.LENGTH_LONG).show();
                                googleMap.addPolyline(new PolylineOptions()
                                        .add(previousLat, currentLat)

                                        .width(5).color(Color.BLUE).geodesic(true));
                            }

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 15));
                            previousLat = currentLat;
                            counter++;
                            // timerclass();
                        }
                        try{
                            if(jsonObject.has("slatitude")){
                                //get Value of video 20.307515,85.8090322
                                double latitude = Double.parseDouble(jsonObject.getString("slatitude"));
                                double longitude = Double.parseDouble(jsonObject.getString("slongitude"));

                                //   String sl_no= jsonObject.getString("source");
                                LatLng currentLat = new LatLng(latitude, longitude);
                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Source Address :-" + jsonObject.getString("source"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_g)));
                                double dlatitude = Double.parseDouble(jsonObject.getString("dlatitude"));
                                double dlongitude = Double.parseDouble(jsonObject.getString("dlongitude"));
                                //   String sl_no= jsonObject.getString("source");
                                LatLng dcurrentLat = new LatLng(dlatitude, dlongitude);
                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(dcurrentLat)
                                        .title("destination Address :-" + jsonObject.getString("destination"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_r)));
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(currentLat);
                                builder.include(dcurrentLat);
                                LatLngBounds bounds = builder.build();

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);

                                googleMap.animateCamera(cu);
                            }
                        }catch (Exception e){
                            e.getMessage();

                        }

                       /* etp_title.setText("ETP NO - " + jsonObject.getString("etpno"));
                        if(jsonObject.getString("etpno") == "NA"){
                            Toast.makeText(VehicelWise_Map.this, "No ETPNo assign with this vehicle No .", Toast.LENGTH_SHORT).show();
                        }
                        code_title.setText(jsonObject.getString("unitid"));
                        source.setText(jsonObject.getString("source"));
                        destination.setText(jsonObject.getString("destination"));
                        start_day.setText(jsonObject.getString("starttime"));
                        valid_date.setText(jsonObject.getString("trackdate"));
                        circles.setText(jsonObject.getString("company"));
                        min_name.setText("MINERAL NAME :" + jsonObject.getString("minename"));
                        quitity.setText("QUANTITY :" + jsonObject.getString("quantity")+ " Ton");
                        vehi_num.setText(jsonObject.getString("vehical_no"));
                        //  devi_id.setText(jsonObject.getString("deviceid"));
                        // tranpo_name.setText(jsonObject.getString("tranpo_name"));
                        credit.setText("SPEED :"+jsonObject.getString("speed")+ " Km");
                        dir_text.setText("DIRECTION : "+jsonObject.getString("headings"));
                        pick_time.setText(jsonObject.getString("datereceived"));
                        pick_date.setText(jsonObject.getString("vtype"));
                        ign.setText(jsonObject.getString("ign"));
                        gps.setText(jsonObject.getString("gpsstatus"));
*/


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
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) VehicelWise_Map.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

}
