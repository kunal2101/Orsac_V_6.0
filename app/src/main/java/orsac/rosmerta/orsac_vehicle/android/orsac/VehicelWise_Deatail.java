package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.ServiceList;

public class VehicelWise_Deatail extends Activity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    LinearLayout pie_chart, half_pie;
    TextView mapview;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private LottieAnimationView icon_tool;
    String etpno ,sim_vehi_no;
    String sim_status="valid" , com_polling_status = "valid",source_add;

    MyTextView  source, destination, dates ;
    TextView etp_title ,start_day,ign, code_title,gps,valid_date, circles, credit, dir_text, pick_time, pick_date, min_name, quitity, vehi_num, devi_id, tranpo_name,current_date_time;
    TextView txt;
    ArrayList<HashMap<String,String>> vendor_detail;
    FrameLayout frame_floating , frame_track ;
    String com_name_service;
    TextView vtu_status ,id_sl_no,vehicle_near_by_truck;
    String GPS_STATUS;
    PreferenceHelper preferenceHelper;


    GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    private String lat ="",lag;

//TODO : ADD ARIAL DISTANCE IN SEARCH DETAIL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicelwise_detail_new);
        Intent intent  = getIntent();
        preferenceHelper = new PreferenceHelper(this);

        Intent inty = getIntent();
        etpno = inty.getStringExtra("vehicelNo");
        frame_floating =(FrameLayout)findViewById(R.id.frame_floating) ;
        etp_title = (TextView) findViewById(R.id.etp_title);
        code_title = (TextView) findViewById(R.id.code_title);
        source = (MyTextView) findViewById(R.id.source);
        destination = (MyTextView) findViewById(R.id.destination);
        start_day = (TextView) findViewById(R.id.start_day);
        valid_date = (TextView) findViewById(R.id.valid_date);
        circles = (TextView) findViewById(R.id.circles);
        quitity = (TextView) findViewById(R.id.quitity);
        id_sl_no= (TextView)findViewById(R.id.id_sl_no);
        vehi_num = (TextView) findViewById(R.id.vehi_num);
        //  devi_id = (TextView) findViewById(R.id.devi_id);
        tranpo_name = (TextView) findViewById(R.id.tranpo_name);
        min_name = (TextView) findViewById(R.id.min_name);
        credit = (TextView) findViewById(R.id.credit);
        dir_text = (TextView) findViewById(R.id.dir_text);
        quitity = (TextView) findViewById(R.id.quitity);
        pick_date = (TextView) findViewById(R.id.pick_date);
        pick_time = (TextView) findViewById(R.id.pick_time);
        current_date_time =(TextView)findViewById(R.id.current_date_time);
        ign = (TextView) findViewById(R.id.ign);
        gps = (TextView) findViewById(R.id.gps);
        txt =  (TextView ) findViewById ( R.id.recharge_due ) ;
        vtu_status =(TextView)findViewById(R.id.vtu_status) ;
        vehicle_near_by_truck =(TextView)findViewById(R.id.vehicle_near_by_truck) ;
        frame_track=(FrameLayout)findViewById(R.id.frame_track);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        icon_tool = (LottieAnimationView) findViewById(R.id.icon_tool);
      //  icon_tool.setImageResource(R.drawable.location_new);
        icon_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (GPS_STATUS.equalsIgnoreCase("INVALID")) {
                        Toast.makeText(VehicelWise_Deatail.this, "Invalid  Location ....", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(VehicelWise_Deatail.this, VehicelWise_Map.class);

                        intent.putExtra("vehicelNo", etpno);
                        // inty.putExtra("vehicelNo",vehicelNo);

                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.getMessage();

                }


            }
        });
/*
        txt.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Intent inty = new Intent ( VehicelWise_Deatail.this,PaymentLogin.class );
                inty.putExtra ( "vehicleno",etpno );
                startActivity ( inty );

            }
        } );
*/

        if (mGoogleApiClient == null) {
            enableLoc();
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         current_date_time.setText(df.format(c.getTime()));
        frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(VehicelWise_Deatail.this, ServiceList.class);
                inty.putExtra("vendor_name",com_name_service);
                startActivity(inty);
                Toast.makeText(VehicelWise_Deatail.this, com_name_service, Toast.LENGTH_SHORT).show();

            }
        });
        if (isConnection()) {
            new Authentication().execute();
        } else {
            Toast.makeText(VehicelWise_Deatail.this, "No Internet , Please Try After sometime", Toast.LENGTH_LONG).show();
        }
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Detail Information");

        vendor_detail = new ArrayList<>();
        HashMap mMap = new HashMap();
        mMap.put("9937383917","Sumit kumar rout Co:dilip Pradhan, 1st floor pradhan complex, infront of A1 Bazar, near kali mandir, hatatota, talcher:759100");

    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    @Override
    public void onConnected ( Bundle bundle ) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended ( int i ) {

    }

    @Override
    public void onConnectionFailed ( ConnectionResult connectionResult ) {

    }

    @Override
    public void onLocationChanged ( Location location ) {
        mLastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);*/
        //  Toast.makeText(this, ""+location.getLatitude()+" and lat : "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
        //move map camera
       // lat = String.valueOf(location.getLatitude());
        // lag = String.valueOf ( location.getLongitude ( ) );
try {
    if (!lat.equalsIgnoreCase ( "" ) && lat.startsWith ( "0" ) && lag.startsWith ( "0" )) {
        vehicle_near_by_truck.setText ( "Invalid Location \n (Getting Invalid Lat - Lng)"  );
        vehicle_near_by_truck.setTextSize ( 12 );

    } else {
        Location startPoint = new Location ( "locationA" );
        startPoint.setLatitude ( Double.parseDouble ( lat ) );
        startPoint.setLongitude ( Double.parseDouble ( lag ) );

        Location endPoint = new Location ( "locationA" );
        endPoint.setLatitude ( mLastLocation.getLatitude ( ) );
        endPoint.setLongitude ( mLastLocation.getLongitude ( ) );
        double distance = startPoint.distanceTo ( endPoint );


        vehicle_near_by_truck.setText ( String.format ( " %.2f" , distance / 1000 ) + " KM" );

    }
}catch (Exception e){
    e.getMessage ();
}



    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onMapReady ( GoogleMap googleMap ) {

    }
    private void enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi( LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult> () {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().

                                status.startResolutionForResult((Activity) VehicelWise_Deatail.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    buildGoogleApiClient();
                    break;
                case Activity.RESULT_CANCELED:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    onbackClick();
                    break;
            }
        }



    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    class SimValidilty extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*
            AndyUtils.showCustomProgressDialog(VehicelWise_Deatail.this,
                    "Fetching Information Please wait...", false, null);
*/

        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;



            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            //nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
              //  HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);
                //OD35B1747
            //    HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getsimvalidity?vehicleno="+sim_vehi_no);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/getsimvalidity?vehicleno="+sim_vehi_no);

               // preferenceHelper.getUrls() +"/"+
                // Add your data
              //  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                            String totalinstalled = jsonObj.optString("simvalidity");
                            if(totalinstalled == "null"){
                                ign.setText("Invalid");
                            }
                            String[] sdata = totalinstalled.split("-");

                            start_day.setText(sdata[2]+"-"+sdata[1]+"-"+sdata[0]);
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = df.format(c);
                            String[] cdata = formattedDate.split("-");
                            Toast.makeText(VehicelWise_Deatail.this, ""+formattedDate, Toast.LENGTH_SHORT).show();
                            if(Integer.parseInt(cdata[2])>Integer.parseInt(sdata[0])){
                                //Toast.makeText(VehicelWise_Deatail.this, "Sim Expire 1", Toast.LENGTH_SHORT).show();
                                sim_status= "Invalid";
                               // vtu_status.setText("Mobile Trcaking");
                                blink();

                            }else  if(Integer.parseInt(cdata[2])==Integer.parseInt(sdata[0])){
                                if(Integer.parseInt(cdata[1])>Integer.parseInt(sdata[1])){
                                   // Toast.makeText(VehicelWise_Deatail.this, "Sim Expire 2", Toast.LENGTH_SHORT).show();
                                  //  vtu_status.setText("Mobile Trcaking");
                                    sim_status= "Invalid";
                                    blink();

                                }else  if(Integer.parseInt(cdata[1])==Integer.parseInt(sdata[1])){
                                    if(Integer.parseInt(cdata[0])>Integer.parseInt(sdata[2])){
                                       // Toast.makeText(VehicelWise_Deatail.this, "Sim Expire 3", Toast.LENGTH_SHORT).show();
                                      //  vtu_status.setText("Mobile Trcaking");
                                        sim_status= "Invalid";
                                        blink();

                                    }
                                }
                            }else {
                               // Toast.makeText(VehicelWise_Deatail.this, "gggggggg", Toast.LENGTH_SHORT).show();
                                sim_status= "Valid";

                            }
                            ign.setText(sim_status);

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

    }
    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                //7893782867
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = (TextView) findViewById(R.id.recharge_due);
                        //txt.setVisibility(View.VISIBLE);
                      //  frame_track.setVisibility(View.VISIBLE);
                        if(txt.getVisibility() == View.VISIBLE){
                            txt.setVisibility(View.INVISIBLE);
                        }else{
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(VehicelWise_Deatail.this,
                    "Loading...", false, null);
        /*    progressDialog.setMessage("Loading....");
            progressDialog.show();*/
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




            try {
                HttpClient httpclient = new DefaultHttpClient();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=" + etpno);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+ "getlivevehicles?vehicleno=" + etpno);

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
                        if(jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            etp_title.setText("ETP NO - " + jsonObject.getString("etpno"));
                            if (jsonObject.getString("etpno") == "NA") {
                                Toast.makeText(VehicelWise_Deatail.this, "No ETPNo assign with this vehicle No .", Toast.LENGTH_SHORT).show();
                            }
                            code_title.setText(jsonObject.getString("unitid"));
                            source.setText(jsonObject.getString("source"));
                            source_add=       jsonObject.getString("source");

                            destination.setText(jsonObject.getString("destination"));
                           // start_day.setText(jsonObject.getString("starttime"));
                            valid_date.setText(jsonObject.getString("trackdate"));
                            circles.setText(jsonObject.getString("company"));
                            com_name_service = jsonObject.getString("company");
                            min_name.setText(jsonObject.getString("minename"));
                            quitity.setText( jsonObject.getString("quantity") + " Ton");
                            vehi_num.setText(jsonObject.getString("vehical_no"));
                            etpno =jsonObject.getString("vehical_no");
                            //  devi_id.setText(jsonObject.getString("deviceid"));
                            // tranpo_name.setText(jsonObject.getString("tranpo_name"));
                            credit.setText( jsonObject.getString("speed") + " Km");
                            dir_text.setText( jsonObject.getString("headings"));
                            pick_time.setText(jsonObject.getString("datereceived"));
                            pick_date.setText(jsonObject.getString("vtype"));
                           // ign.setText(jsonObject.getString("ignnumber"));
                            gps.setText(jsonObject.getString("gpsstatus"));
                            id_sl_no.setText(jsonObject.getString("unique_serial_id"));
                            GPS_STATUS =  jsonObject.getString("gpsstatus");
                            sim_vehi_no = jsonObject.getString("vehical_no");
                            if(jsonObject.getString("new24hrDateStatus").equalsIgnoreCase("OK")) {
                                com_polling_status = "Valid";
                                vtu_status.setText("Valid");
                            }else {
                                com_polling_status = "Invalid";
                                vtu_status.setText("Invalid");
                            }

                            lat = jsonObject.getString ( "latitude" ) ;
                             lag = jsonObject.getString ( "longitude" );

                            new SimValidilty().execute();


                                Location startPoint = new Location ( "locationA" );
                                startPoint.setLatitude ( Double.parseDouble ( jsonObject.getString ( "latitude" ) ) );
                                startPoint.setLongitude ( Double.parseDouble ( jsonObject.getString ( "longitude" ) ) );

                                Location endPoint = new Location ( "locationA" );
                                endPoint.setLatitude (mLastLocation.getLatitude () );
                                endPoint.setLongitude ( mLastLocation.getLongitude () );
                                double distance = startPoint.distanceTo ( endPoint );
                            if(mLastLocation != null  &&  lat.startsWith ( "0" ) && lag.startsWith ( "0" )) {
                                vehicle_near_by_truck.setText ( "Invalid Location \n (Getting Invalid Lat-Lng)"  );
                                vehicle_near_by_truck.setTextSize ( 12 );
                            }else {
                                vehicle_near_by_truck.setText ( String.format ( " %.2f" , distance / 1000 ) + " KM" );
                            }

                        }else {
                            Toast.makeText(VehicelWise_Deatail.this, "Invalid Vehicle Number/IMEI No..", Toast.LENGTH_LONG).show();
                        onbackClick();
                        }

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

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
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
                Intent intent = new Intent(VehicelWise_Deatail.this, VehicelWise_Map.class);
                intent.putExtra("etpno", etpno);
                startActivity(intent);
                break;


        }
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) VehicelWise_Deatail.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

}
