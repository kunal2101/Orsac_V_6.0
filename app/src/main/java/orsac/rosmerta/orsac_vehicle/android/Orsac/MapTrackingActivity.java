package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialog;
import orsac.rosmerta.orsac_vehicle.android.Dialogs.PrettyDialogCallback;
import orsac.rosmerta.orsac_vehicle.android.GeocodingLocation;
import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.RouteDetailActivity;
import orsac.rosmerta.orsac_vehicle.android.SearchVehicleWise;
import orsac.rosmerta.orsac_vehicle.android.Textview.TextView_Lato;
import orsac.rosmerta.orsac_vehicle.android.Textview.Textview_lato_thin;

/**
 * Created by Developer on 09-Apr-17.
 */

public class MapTrackingActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener {

    SupportMapFragment fm;
    GoogleMap googleMap;
   /* static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);*/

    static final LatLng origin = new LatLng(28.5355, 77.3910);
    static final LatLng dest = new LatLng(28.5562, 77.1000);
    LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(23, 68), new LatLng(28, 97));
    ArrayList<LatLng> points = null;
    //Declare a variable to hold count down timer's paused status
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    Timer timer = new Timer();
    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 10 sec.
    int counter = 0;
    Marker mDesc = null;
    ArrayList<HashMap<String,String>> getDatalist;
    LatLng previousLat;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    MyTextView etp_title, code_title, source, destination, dates, ign, gps, vehic_no,
            circle_name, startTime_new, pass_valid_new;
    TextView min_name_text, quitity_tex, txt_speed, dir_text_new, dir_text_newss;
    ScrollView scroll;
    String etpno= "";
    CardView noPosts;
    ImageView imag_truck;
    FrameLayout frame_floating,frame_floating_detail;
    Marker markerGreen;
    ArrayList<LatLng> list_latlng ,src_des_latlng;
    ProgressDialog progressDialogss;
    boolean call_addredd=true;
    int count_add= 0 ;
    String source_st,desti_st;
    PreferenceHelper preferenceHelper;
    ImageView arraow;
    boolean isUp;
    LinearLayout line_tile ,actual_route,planned_route;
TextView loaddds,avg_spped_text ,distance_travel_text;
    ArrayList<LatLng> mRouteArr = new ArrayList<>();
    ArrayList<LatLng> mRouteArr_new = new ArrayList<>();
    int currentPttwo = 0;
    LatLngBounds.Builder bld;
    Marker mMarker;
    int map_maker_count = 1;
    final Handler handler_play_pause = new Handler();
    Button btnPlayBtn;
    boolean isPlayHistory = false;
    int currentPt = 0 ;
    CardView info_card;
    LinearLayout lin_detail;
    TextView_Lato txt_info_switcher;
    Boolean is_ruunig_etp_show =true;
    TextView_Lato play_txt,pause_txt;
    ArrayList<String> arra_speed = new ArrayList<>();
    ArrayList<String> arra_loc = new ArrayList<>();
    ArrayList<String> arra_date_time = new ArrayList<>();

    Button  btnBackBtn,btnForBtn;
    TextView sppeddd,vehicle_no_replay,com_add,stringDateTime;

    String src_code, dest_code;
    TextView_Lato running_route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etp_map_new);
        preferenceHelper = new PreferenceHelper(this);

       // noPosts = (CardView) findViewById(R.id.noPosts);
        //noPosts.setVisibility(View.GONE);
       // frame_floating= (FrameLayout)findViewById(R.id.frame_floating);
       // frame_floating_detail=(FrameLayout)findViewById(R.id.frame_floating_detail);
       // imag_truck = (ImageView) findViewById(R.id.imag_truck);
       // scroll = (ScrollView) findViewById(R.id.scroll);
      //  scroll.setVerticalScrollBarEnabled(false);
       // scroll.setHorizontalScrollBarEnabled(false);
        etp_title = (MyTextView) findViewById(R.id.etp_title);
        code_title = (MyTextView) findViewById(R.id.code_title);
        source = (MyTextView) findViewById(R.id.source);
        destination = (MyTextView) findViewById(R.id.destination);
        vehic_no = (MyTextView) findViewById(R.id.vehic_no);
        circle_name = (MyTextView) findViewById(R.id.circle_name);
        startTime_new = (MyTextView) findViewById(R.id.startTime_new);
        pass_valid_new = (MyTextView) findViewById(R.id.pass_valid_new);
        min_name_text = (TextView) findViewById(R.id.min_name_text);
        quitity_tex = (TextView) findViewById(R.id.quitity_tex);
        txt_speed = (TextView) findViewById(R.id.txt_speed);
        dir_text_newss = (TextView) findViewById(R.id.dir_text_newss);
        dir_text_new = (TextView) findViewById(R.id.dir_text_new);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        line_tile = (LinearLayout)findViewById(R.id.lin_trip_hide_detail) ;
        loaddds =(TextView)findViewById(R.id.loaddds);
        planned_route = (LinearLayout)findViewById(R.id.planned_route);
        actual_route   = (LinearLayout)findViewById(R.id.actual_route);
        avg_spped_text =(TextView)findViewById(R.id.avg_spped_text);
                distance_travel_text =(TextView)findViewById(R.id.distance_travel_text);
        info_card = (CardView) findViewById(R.id.info_card) ;
        lin_detail = (LinearLayout)findViewById(R.id.lin_detail);
        txt_info_switcher = (TextView_Lato)findViewById(R.id.txt_info_switcher);
        btnPlayBtn=(Button)findViewById(R.id.btnPlayBtn);
        btnBackBtn=(Button)findViewById(R.id.btnBackBtn);
        btnForBtn=(Button)findViewById(R.id.btnForBtn);
        sppeddd =(TextView)findViewById(R.id.sppeddd) ;
        running_route=(TextView_Lato) findViewById(R.id.running_route);
                vehicle_no_replay=(TextView)findViewById(R.id.vehicle_no) ;
        com_add=(TextView)findViewById(R.id.vehicle_no) ;
                stringDateTime=(TextView)findViewById(R.id.stringDateTime) ;
       // play_txt = (TextView_Lato)findViewById(R.id.play_txt);
       // pause_txt = (TextView_Lato)findViewById(R.id.pause_txt);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Tracking");
        Intent inty = getIntent();
        list_latlng = new ArrayList<LatLng>();
        src_des_latlng=new ArrayList<LatLng>();


        etpno = inty.getStringExtra("etpNo");
//        imag_truck.setImageResource(R.drawable.animation_draw);
      //  AnimationDrawable frameAnimation = (AnimationDrawable) imag_truck.getDrawable();
      //  frameAnimation.start();
        MapsInitializer.initialize(this);
        bld = new LatLngBounds.Builder();
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        googleMap = fm.getMap();

        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setPadding(0, 0, 0, 100);
        googleMap.getUiSettings().setZoomControlsEnabled(false);

       new maptracking().execute();
        arraow =(ImageView)findViewById(R.id.arraow);
        arraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUp) {
                    // slideDown(myView);
                    //  myButton.setText("Slide up");
                    line_tile.setVisibility(View.VISIBLE);
                    loaddds.setText("Show Trip Details");
                    arraow.setBackground(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_arrow_up));

                } else {
                    //slideUp(myView);
                    //myButton.setText("Slide down");
                    line_tile.setVisibility(View.GONE);
                    loaddds.setText("Hide Trip Details");

                    arraow.setBackground(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_arrow));

                }
                isUp = !isUp;
            }
        });
        txt_info_switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_info_switcher.getText().toString().equalsIgnoreCase("Show Details") ){
                    line_tile.setVisibility(View.VISIBLE);
                    info_card.setVisibility(View.GONE);
                    txt_info_switcher.setText("Replay On Map");

                }else {
                    line_tile.setVisibility(View.GONE);
                    info_card.setVisibility(View.VISIBLE);
                    txt_info_switcher.setText("Show Details");
                }
            }
        });
        planned_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog();

            }
        });
        actual_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog();

            }
        });
        btnPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(isPlayHistory == false){ // for playing
                    isPlayHistory = true;

                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnPlayBtn.setBackgroundDrawable(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_pause_icon) );
                    } else {
                        btnPlayBtn.setBackground(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_pause_icon));
                    }
                    drawPolyline_play_txt(mRouteArr);
                }else if(isPlayHistory == true){ // for pause
                    isPlayHistory = false;

                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnPlayBtn.setBackgroundDrawable(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_play_btn_new) );
                    } else {
                        btnPlayBtn.setBackground(ContextCompat.getDrawable(MapTrackingActivity.this, R.drawable.ic_play_btn_new));
                    }
                }
                /*
                if(isclick) {
                    isPlayHistory = true;
                    play_txt.setBackgroundTintList(ContextCompat.getColorStateList(SingleVehicleMapHistoryActivity.this, R.color.gray));
                      play_txt.setEnabled(false);
                    Toast.makeText(SingleVehicleMapHistoryActivity.this, "Please Wait , We process All Data...", Toast.LENGTH_SHORT).show();
                    drawPolyline_play_txt(mRouteArr);

                }*/
            }
        });
        btnBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPt == 0){

                }else {
                    currentPt = currentPt - 1;
                }
            }
        });
        btnForBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPt == mRouteArr.size()-1){
                    //currentPt = currentPt + 1;
                }else {
                    currentPt = currentPt + 1;
                }
            }
        });
        running_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MapTrackingActivity.this,"Button Click",Toast.LENGTH_LONG).show();
                new maptracking__().execute();
            }
        });
/*
        pause_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    handler_play_pause.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
*/

     /*   googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.custom_marker, null);

                // Getting the position from the marker
                //LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
             *//*   TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + "Latityde");

                // Setting the longitude
                tvLng.setText("Longitude:" + "Laongitude");
*//*
                // Returning the view containing InfoWindow contents
                return v;

            }
        });*/


/*
        new CountDownTimer(30000, 30000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (isPaused || isCanceled) {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    // Toast.makeText(MapTrackingActivity.this, "ontick cancel" , Toast.LENGTH_LONG).show();
                    cancel();
                } else {

                   // new maptracking().execute();
                    //start();
                    // Toast.makeText(MapTrackingActivity.this, "ontick" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                if (isPaused || isCanceled) {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    //  Toast.makeText(MapTrackingActivity.this, "onfinish caqncel" , Toast.LENGTH_LONG).show();
                    cancel();
                } else {
                    new maptracking().execute();
                    start();
                    // Toast.makeText(MapTrackingActivity.this, "onfinish" , Toast.LENGTH_LONG).show();
                }
            }
        }.start();
*/


       /* frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.GONE);
                frame_floating_detail.setVisibility(View.VISIBLE);
            }
        });
        frame_floating_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.VISIBLE);
                frame_floating_detail.setVisibility(View.GONE);
            }
        });*/

    }


    public Bitmap createDrawableFromViewpoint(String poiTitle) {
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_shelter_layout, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        TextView txt_title = (TextView)marker.findViewById(R.id.txt_title);
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        txt_title.setText(poiTitle);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCanceled = true;


           /* if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }*/



        // Toast.makeText(MapTrackingActivity.this, "stop" , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) MapTrackingActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /*
  * Start server data load for tab selection.
  *
  */
    class getReport extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog = new ProgressDialog(MapTrackingActivity.this, R.style.mProgressBar);
 int distance_ = 0 ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txt_info_switcher.setVisibility(View.VISIBLE);
            info_card.setVisibility(View.VISIBLE);
            line_tile.setVisibility(View.GONE);
            is_ruunig_etp_show = false ;


            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(MapTrackingActivity.this, "", "Please Wait...", true);
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

                distance_ =  Integer.parseInt(jsonArray.getJSONObject(0).getString("dist"));
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



                    try {
                        if(!jsObj.getString("vehSpeed").equalsIgnoreCase("0.000")) {
                            mRouteArr.add(new LatLng(Double.parseDouble(jsObj.getString("lat")), Double.parseDouble(jsObj.getString("lon"))));
                            arra_speed.add(jsObj.getString("vehSpeed").split("\\.", 2)[0]);

                            arra_loc.add(Integer.parseInt(jsObj.getString("dist"))  - distance_  + " KM");
                            arra_date_time.add(jsObj.getString("datetime"));
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    getDatalist.add(map);
                }
                double tot_dist =  distance(mRouteArr);
                try {
                    drawPolyline_new(mRouteArr);
                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }


            } catch (Exception ev) {
                ev.getStackTrace();
            }


            progressDialog.dismiss();
        }
    }
    public ArrayList<LatLng> remove_dup (List<LatLng> list){
        ArrayList<LatLng> newList = new ArrayList<LatLng>();
        for (LatLng element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        return  newList;

    }

    private void drawPolyline_play_txt(final ArrayList<LatLng> routeArr) {



        List<LatLng> points = routeArr;
        if(map_maker_count == 1) {
            mMarker = googleMap.addMarker(new MarkerOptions().position(points.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_vehicle)));
            map_maker_count++;
        }

        //Code to move car along static latitude and longitude

        points = remove_dup(points);
        Toast.makeText(MapTrackingActivity.this, "Please Wait , We process All Data...", Toast.LENGTH_SHORT).show();

        final List<LatLng> finalPoints = points;
        handler_play_pause.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isPlayHistory) {
                    if (currentPt  < finalPoints.size()) {
                        //post again
                        Log.d("tess", "inside run ");

                        try {
                            stringDateTime.setText(arra_date_time.get(currentPt));
                            sppeddd.setText(arra_speed.get(currentPt));
                            com_add.setText( addredtolatlng(routeArr.get(currentPt).latitude ,routeArr.get(currentPt).longitude));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                      //  com_add.setText(arra_loc.get(currentPt));
                       // stringDateTime.setText(arra_date_time.get(currentPt));
                       // sppeddd.setText(arra_speed.get(currentPt));
                        Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
                        targetLocation.setLatitude(finalPoints.get(currentPt).latitude);
                        targetLocation.setLongitude(finalPoints.get(currentPt).longitude);
                        animateMarkerNew(targetLocation, mMarker);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalPoints.get(currentPt), 17f));

                        handler_play_pause.postDelayed(this, 4000);
                        currentPt++;
                    } else {
                        Log.d("tess", "call back removed");
                        //removed callbacks
                        handler_play_pause.removeCallbacks(this);
                    }
                }
            }
        }, 4000);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(routeArr.get(1), 13));
    }

    /* private void drawPolyline(ArrayList<LatLng> routeArr) {
       final List<LatLng> points = routeArr;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#51a8ff"));
        polylineOptions.width(10);
        polylineOptions.addAll(routeArr);
        googleMap.addPolyline(polylineOptions);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#ffffff"));
        polylineOptions.width(4);
        polylineOptions.addAll(routeArr);

        googleMap.addPolyline(polylineOptions);*//*

        //decodePoly(_path); // list of latlng
      *//*  for ( int i = 0; i < points.size() - 1; i++) {
        LatLng src = points.get(i);
        LatLng dest = points.get(i + 1);
        final LatLng latLng = points.get(i);
        // mMap is the Map Object
        if(src.latitude < dest.latitude ) {
                *//**//*Polyline line = googleMap.addPolyline(
            new PolylineOptions().add(
                    new LatLng(src.latitude, src.longitude),
                    new LatLng(dest.latitude, dest.longitude)
            ).width(5).color(Color.BLUE).geodesic(true)
                );*//**//*
            // final LatLng latLngs = points.get(i);

            final Handler handlerOne = new Handler();

            handlerOne.postDelayed(new Runnable() {
                @Override
                public void run() {
                    googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin)));

                }
            }, 1000);
        }


    }*/
   private void drawPolyline_new(ArrayList<LatLng> routeArr) {
       final List<LatLng> points = routeArr;

       googleMap.addMarker(new MarkerOptions().position(points.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_location)));
      // googleMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_location)));

//       PolylineOptions polylineOptions = new PolylineOptions();
//       polylineOptions.color(Color.parseColor("#51a8ff"));
//       polylineOptions.width(10);
//       polylineOptions.addAll(routeArr);
//       googleMap.addPolyline(polylineOptions);
//
//       polylineOptions = new PolylineOptions();
//       polylineOptions.color(Color.parseColor("#3ae1aa"));
//       polylineOptions.width(4);
//       polylineOptions.addAll(routeArr);

     //  googleMap.addPolyline(polylineOptions);
       for ( int i = 0; i < points.size() - 1; i++) {
           LatLng src = points.get(i);
           LatLng dest = points.get(i + 1);
           final LatLng latLng = points.get(i);
           googleMap.addMarker(new MarkerOptions()
                   .position(latLng)
                   .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromViewpoint(i+1+""))));


           googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_one)));

           // mMap is the Map Object

       }



       currentPttwo = 0;
       googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 20f));
       for ( int i = 0; i < points.size() - 1; i++) {
           if (currentPttwo < points.size()) {
               bld.include(points.get(currentPttwo));

               //polylineOptions.color(Color.parseColor("#3f51b5"));
               //polylineOptions.width(3);
               // polylineOptions.add(points.get(currentPttwo));
               //googleMap.addPolyline(polylineOptions);
               //googleMap.addMarker(new MarkerOptions().position(points.get(currentPttwo)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin)));
               currentPttwo++;
               googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bld.build(), 100));
               // googleMap.addMarker((new MarkerOptions().position(points.get(currentPttwo)).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin_one))));


           } else {
               // Toast.makeText(MapTrackingActivity.this, ""+currentPttwo, Toast.LENGTH_SHORT).show();
              // h.removeMessages(0);
           }
       }
/*
        h.postDelayed(new Runnable(){
            public void run(){
                if (currentPttwo < points.size()) {
                    bld.include(points.get(currentPttwo));

                    polylineOptions.color(Color.parseColor("#3f51b5"));
                    polylineOptions.width(3);
                    polylineOptions.add(points.get(currentPttwo));
                    googleMap.addPolyline(polylineOptions);
                    //googleMap.addMarker(new MarkerOptions().position(points.get(currentPttwo)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin)));
                    currentPttwo++;
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bld.build(), 100));
                    googleMap.addMarker((new MarkerOptions().position(points.get(currentPttwo)).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin_one))));


                }else {
                   // Toast.makeText(MapTrackingActivity.this, ""+currentPttwo, Toast.LENGTH_SHORT).show();
                    h.removeMessages(0);
                }
                h.postDelayed(this, delay_test);
            }
        }, delay_test);
*/

       //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
       //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(routeArr.get(1), 13));
   }
   public  String addredtolatlng(double lat , double lng) throws IOException {
       Geocoder geocoder;
       List<Address> addresses;
       geocoder = new Geocoder(this, Locale.getDefault());

       addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

       String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
       String city = addresses.get(0).getLocality();
       String state = addresses.get(0).getAdminArea();
       String country = addresses.get(0).getCountryName();
       String postalCode = addresses.get(0).getPostalCode();
       String knownName = addresses.get(0).getFeatureName();
       return  address;
   }
    private void drawPolyline(ArrayList<LatLng> routeArr) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#51a8ff"));
        polylineOptions.width(15);
        polylineOptions.addAll(routeArr);
        googleMap.addPolyline(polylineOptions);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.parseColor("#7B1FA2"));
        polylineOptions.width(10);
        polylineOptions.addAll(routeArr);
        googleMap.addPolyline(polylineOptions);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(routeArr.get(1), 13));
    }

//

    class maptracking__ extends AsyncTask<String, String, String> {

        LatLng src_currentLat,des_currentLat;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (counter == 0) {
                try {
                    pDialog = ProgressDialog.show(MapTrackingActivity.this, "", "Loading. Please wait...", true, true);
                }catch (Exception ev){
                    ev.getMessage();
                }
            }
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter =  preferenceHelper.getUrls()+"/getRouteFromETPSrcDest?source="+src_code+"&etpno="+etpno+"&destination="+dest_code;
//http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/etpwiselive?etpno=L3170250/4

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(parameter);
                HttpResponse response = httpclient.execute(httppost);
                jsonStr = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonStr;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        /*JSONObject jsonObj = new JSONObject(getResponse);

                        if (jsonObj.has("error_code")) {
                            noPosts.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }*/
                        //  noPosts.setVisibility(View.GONE);
                        JSONArray jsonArray = new JSONArray(getResponse);

                        JSONObject jsonObject =  jsonArray.getJSONObject(0);
                        String str_json = jsonObject.getString("jsonstr");
                        JSONObject jjobject = new JSONObject(str_json);
                       JSONArray inn_array = jjobject.getJSONArray("features");
                       JSONObject inn_job = inn_array.getJSONObject(0);
                       String geomerty = inn_job.getString("geometry");
                       JSONObject innn_obj = new JSONObject(geomerty);
                       JSONArray inn_arr = innn_obj.getJSONArray("coordinates");
                        for (int j=0; j< inn_arr.length(); j++) {
                            JSONArray f_arr = inn_arr.getJSONArray(j);
                            //System.out.println("inner Array" +f_arr);
                            for (int i = 0; i < f_arr.length(); i++) {
                                JSONArray f_arr_inn = f_arr.getJSONArray(i);
                                String str_lat = f_arr_inn.getString(1);
                                String str_lng = f_arr_inn.getString(0);
                                // System.out.println("innerdsafds Array" +str_lat+ "---" +str_lng);

                                mRouteArr_new.add(new LatLng(Double.parseDouble(str_lat), Double.parseDouble(str_lng)));

                            }
                        }



                        System.out.println("Print Array"+mRouteArr_new);
                        googleMap.addMarker(new MarkerOptions().position(mRouteArr_new.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin)));
                        googleMap.addMarker(new MarkerOptions().position(mRouteArr_new.get(mRouteArr_new.size()-1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_origin)));

                        drawPolyLineOnMap(mRouteArr_new);


                      //  pDialog.dismiss();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                        Toast.makeText(MapTrackingActivity.this, "No route found", Toast.LENGTH_LONG).show();
                        // progressDialog.dismiss();
                    }

                }
            });
        }

    }

    private double distance(ArrayList<LatLng> latLngs) {
        //double lat1, double lon1, double lat2, double lon2
        double dist = 0.0;
        for (int i =0; i< latLngs.size()-2;i++){
            double theta = latLngs.get(i).longitude - latLngs.get(i+1).longitude;
            dist = Math.sin(deg2rad(latLngs.get(i).latitude))
                    * Math.sin(deg2rad(latLngs.get(i+1).latitude))
                    + Math.cos(deg2rad(latLngs.get(i).latitude))
                    * Math.cos(deg2rad(latLngs.get(i+1).latitude))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
        }

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    class maptracking extends AsyncTask<String, String, String> {

        LatLng src_currentLat,des_currentLat;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (counter == 0) {
                try {
                    pDialog = ProgressDialog.show(MapTrackingActivity.this, "", "Loading. Please wait...", true, true);
                }catch (Exception ev){
                    ev.getMessage();
                }
            }
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter =  preferenceHelper.getUrls() +"/"+"etpWiseLive?etpno=" + etpno;
//http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/etpwiselive?etpno=L3170250/4
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(parameter);
                HttpResponse response = httpclient.execute(httppost);
                jsonStr = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {

                e.printStackTrace();
            }
            return jsonStr;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        /*JSONObject jsonObj = new JSONObject(getResponse);

                        if (jsonObj.has("error_code")) {
                            noPosts.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }*/
                      //  noPosts.setVisibility(View.GONE);
                        JSONArray jsonArray = new JSONArray(getResponse);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if(jsonObject.getString("latitude").equalsIgnoreCase("null")){
                            isCanceled = true;
                            pDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapTrackingActivity.this);
                            builder.setMessage("NO DATA FOUND  OR INVALID DATA WITH THIS ETP NO")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                           /* Intent inty = new Intent( MapTrackingActivity.this, NavigationActivity.class);
                                            startActivity(inty);*/
                                            dialog.cancel();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("ORSAC ");
                            alert.show();


                            Toast.makeText(MapTrackingActivity.this, "NO DATA FOUND WITH THIS ETP NO", Toast.LENGTH_LONG).show();
                        }else {
                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));

                            LatLng currentLat = new LatLng(latitude, longitude);
                            list_latlng.add(currentLat);
                            src_des_latlng.add((currentLat));
                            Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
                            targetLocation.setLatitude(latitude);
                            targetLocation.setLongitude(longitude);
                            etp_title.setText(jsonObject.getString("etpno"));
                            circle_name.setText(jsonObject.getString("circle"));
                            vehic_no.setText(jsonObject.getString("vehicleno"));
                            txt_speed.setText(jsonObject.getString("speed") + " Km");
                            dir_text_new.setText(jsonObject.getString("direction"));
                            source.setText(jsonObject.getString("source"));
                            destination.setText(jsonObject.getString("destination"));
                            startTime_new.setText("StartTime : " + jsonObject.getString("starttime"));
                            min_name_text.setText(jsonObject.getString("minename"));
                            quitity_tex.setText(jsonObject.getString("quantity") + " Ton");
                            pass_valid_new.setText("Pass Valid : " + jsonObject.getString("passvalid"));
                            code_title.setText("Code :" + jsonObject.getString("code"));

                            src_code = jsonObject.getString("code");
                            dest_code = jsonObject.getString("destcode");

                            dir_text_newss.setText(jsonObject.getString("packetdate")+"/"+jsonObject.getString("packettime"));
                          //  avg_spped_text .setText(jsonObject.getString("avgSpeed") + " KM/H");

                            //distance_travel_text.setText(jsonObject.getString("avgDist") + " KM");
                            avg_spped_text .setText(jsonObject.getString("avgSpeed").split("\\.", 2)[0]  + " KM/H");



                            source_st = jsonObject.getString("source");
                             desti_st = jsonObject.getString("destination");
try {
    double srcLat_latitude = Double.parseDouble(jsonObject.getString("srcLat"));
    double srcLong_longitude = Double.parseDouble(jsonObject.getString("srcLong"));

     src_currentLat = new LatLng(srcLat_latitude, srcLong_longitude);
    list_latlng.add(src_currentLat);
}catch (Exception e){
    e.getMessage();
}
                           try {
                               double destLat_latitude = Double.parseDouble(jsonObject.getString("destLat"));
                               double destLong_longitude = Double.parseDouble(jsonObject.getString("destLong"));

                                des_currentLat = new LatLng(destLat_latitude, destLong_longitude);
                               list_latlng.add(des_currentLat);
                           }catch (Exception e){
                               e.getMessage();
                           }
                            if (counter == 0) {
                                Marker mOrigin = googleMap.addMarker(new MarkerOptions()
                                        .title("Current Location")
                                        .position(currentLat)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));
                                try {
                                    googleMap.addMarker(new MarkerOptions()
                                            .title(source_st)
                                            .position(src_currentLat)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_medical_)));
                                }catch (Exception e){
                                    e.getMessage();
                                }
                                try {
                                    googleMap.addMarker(new MarkerOptions()
                                            .title(desti_st)
                                            .position(des_currentLat)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_medical_des)));

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 10));
                                }catch (Exception e){
                                    e.getMessage();
                                }
                                LatLngBounds.Builder builder = LatLngBounds.builder();


                                builder.include(currentLat);
                                try {
                                    builder.include(src_currentLat);
                                }catch (Exception e){
                                    e.getMessage();
                                }
                                try {
                                    builder.include(des_currentLat);
                                }catch (Exception e){
                                    e.getMessage();
                                }
                                try {

                                    final LatLngBounds boundssss = builder.build();
                                    boundssss.getCenter();

                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(boundssss, 150);
                                    googleMap.moveCamera(cu);
                                }catch (Exception e){
                                    e.getMessage();
                                }
                            } else {
                                if (mDesc != null) {
                                    mDesc.remove();
                                }

                                mDesc = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_red)));


                           /* PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                            for (int z = 0; z < list_latlng.size(); z++) {
                                LatLng point = list_latlng.get(z);
                                options.add(point);
                            }
                             googleMap.addPolyline(options.);*/

                            /*googleMap.addPolyline(new PolylineOptions()
                                    .add(previousLat, currentLat)
                                    .width(5).color(Color.BLUE).geodesic(true));*/

                                drawPolyLineOnMap(src_des_latlng);
                              //  animateMarkerNew(targetLocation, mDesc);


                            }
//                            if(count_add == 0){
//                                new DataLongOperationAsynchTask().execute(source_st);
//                            } if(count_add == 1){
//                                new DataLongOperationAsynchTask().execute(desti_st);
//                                // call_addredd=false;
//                            }


                            // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 16));
                            previousLat = currentLat;
                            counter++;
                            pDialog.dismiss();

                            // progressDialog.dismiss();
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                       // progressDialog.dismiss();
                    }

                }
            });
        }

    }
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GREEN);
        polyOptions.width(15);
        polyOptions.addAll(list);

       // googleMap.clear();
        googleMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        googleMap.animateCamera(cu);
    }

    /*
    Ending getting contact list from backend
     */
    private void animateMarkerNew(final Location destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));
                    } catch (Exception ex) {
                        //I don't care atm..
                        //Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            valueAnimator.start();
        }
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);
        float bearing = 0;


        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            bearing = (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            bearing = (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            bearing = (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            bearing = (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);

        if (begin.latitude == end.latitude) {
            bearing = 0;
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(bearing),Toast.LENGTH_LONG).show();
        return bearing;
    }
    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(MapTrackingActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                String add_ =params[0].replace(" ","%20");

                response = getLatLongByURL("https://maps.googleapis.com/maps/api/geocode/json?address="+add_+"&key=AIzaSyDiQDfSTS5LGBps3qJtIb-zSjmJhljnp4s&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
if (count_add == 0) {
    LatLng currentLat = new LatLng(lat, lng);
    googleMap.addMarker(new MarkerOptions()
            .position(currentLat)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_one)));
    count_add = count_add+ 1;
}else if (count_add == 1){
    LatLng currentLat = new LatLng(lat, lng);
    googleMap.addMarker(new MarkerOptions()
            .position(currentLat)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_location)));
    count_add = count_add+ 1;
}

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void prettyDialog(){
        final PrettyDialog dialog = new PrettyDialog(this);
        dialog
                .setTitle("Choose View Option")
                .setMessage("")
                .setIcon(R.drawable.pdlg_icon_info,R.color.pdlg_color_blue,null)
                .addButton("List view", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                        Intent intent = new Intent(MapTrackingActivity.this, RouteDetailActivity.class);
                        intent.putExtra("etpNo", etpno);

                        startActivity(intent);
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("Map View", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        if(is_ruunig_etp_show){
                            new getReport().execute();

                        }else {
                            Toast.makeText(MapTrackingActivity.this, "Planned Route Already Show on Map", Toast.LENGTH_SHORT).show();
                        }


                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"Cancel selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                });
        dialog.show();
    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


}
