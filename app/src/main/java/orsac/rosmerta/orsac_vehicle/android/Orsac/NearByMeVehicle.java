package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

public class NearByMeVehicle extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    SearchableSpinner spinner_source, spinner_radious;
    final static int REQUEST_LOCATION = 199;
    ArrayList<String> demo_list;
    ArrayList<String> radius;
    Button button;
    LinearLayout lineaar, line_sear;
    EditText km_edit;
    String getValue, getradious, lat, lag;
    FrameLayout frame_floating,frame_floating_count;
    ImageView img_search;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private ImageView icon_tool;
    private GoogleMap googlemap;
    SupportMapFragment fm;
    TextView search_text;
    // private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);

    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    CameraUpdate cu;
    ArrayList<HashMap<String, String>> getdata;
    Map<String, String> map;
    LatLng currentLat, curlatlang;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    CountDownTimer countDownTimer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_me_vehicle);
        map = new HashMap<String, String>();

        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_source);
        spinner_radious = (SearchableSpinner) findViewById(R.id.spinner_radious);
        frame_floating_count = (FrameLayout) findViewById(R.id.frame_floating_count);
        search_text =(TextView)findViewById(R.id.search_text);
        frame_floating = (FrameLayout) findViewById(R.id.frame_floating);
        img_search = (ImageView) findViewById(R.id.img_search);
        button = (Button) findViewById(R.id.button);
        lineaar = (LinearLayout) findViewById(R.id.lineaar);
        line_sear = (LinearLayout) findViewById(R.id.line_sear);
        //km_edit = (EditText)findViewById(R.id.km_edit);
        spinner_source.setTitle("Select Mines");
        line_sear.setVisibility(View.GONE);
        radius = new ArrayList<String>();
        radius.add("1 Km");
        radius.add("3 Km");
        radius.add("5 Km");
        radius.add("7 Km");
        radius.add("8 Km");
        radius.add("10 Km");
        icon_tool = (ImageView) findViewById(R.id.tool_back_icon);
        icon_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
       // new maptracking().execute();
       /* demo_list.add("Bhairpur Lakhraj Fireclay Mines");
        demo_list.add("Chandpur Quqrtz & Quartzite Mines");
        demo_list.add("Mid East Int.Steel Ltd.(MESCO)\n");
        demo_list.add("Kaniha OCP W/B");
        demo_list.add("Samal Check-gate");
        demo_list.add("JHANKARPALI CHINACLAY MINES");*/
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_title.setText("Near By Me");

        ArrayAdapter adapters_radius = new ArrayAdapter<String>(NearByMeVehicle.this, R.layout.spinner_item, radius);
        spinner_radious.setAdapter(adapters_radius);
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        // fm.getMapAsync((OnMapReadyCallback) this);


        mMap = fm.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setPadding(0, 0, 0, 100);
        if (mGoogleApiClient == null) {
         enableLoc();
        }
      //  mMap.setMyLocationEnabled(true);
        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView) view.findViewById(R.id.sr_source);
               /* getValue = getsource.getText().toString();
                HashMap<String, String> map = getdata.get(position);
                lat = map.get("lat");
                lag = map.get("lag");
                String mines_name = map.get("name");

                curlatlang = new LatLng(Double.valueOf(lat), Double.valueOf(lag));
                mMap.addMarker(new MarkerOptions()
                        .position(curlatlang)
                        .title(mines_name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_marker)));


                //  Toast.makeText(NearByMines.this, ""+lag+"new"+lat, Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_radious.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView) view.findViewById(R.id.sr_source);


                getradious = getsource.getText().toString();
                getradious = getradious.replace(" Km","");
                // Toast.makeText(NearByMines.this, ""+getradious, Toast.LENGTH_SHORT).show();
                //lag

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_sear.setVisibility(View.VISIBLE);
                lineaar.setVisibility(View.VISIBLE);
                frame_floating.setVisibility(View.GONE);
                frame_floating_count.setVisibility(View.GONE);
                search_text.setText("");
                // getdata.clear();
                // demo_list.clear();
            }
        });
        countDownTimer = new CountDownTimer(20000, 20000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                    // Toast.makeText(NearByMeVehicle.this, "Countre ", Toast.LENGTH_SHORT).show();
                    new maptrackingWithradius().execute();
                    start();

            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(getradious) ) {
                    line_sear.setVisibility(View.GONE);
                    lineaar.setVisibility(View.GONE);
                    frame_floating.setVisibility(View.VISIBLE);
                    frame_floating_count.setVisibility(View.VISIBLE);
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    new maptrackingWithradius().execute();
                    return;
                } else {
                    Toast.makeText(NearByMeVehicle.this, "Select Mines and Radius", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);*/
      //  Toast.makeText(this, ""+location.getLatitude()+" and lat : "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
        //move map camera
        lat = String.valueOf(location.getLatitude());
       lag = String.valueOf(location.getLongitude());
        curlatlang = latLng;
        mMap.addMarker(new MarkerOptions()
                .position(curlatlang)
                .title("My Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        line_sear.setVisibility(View.VISIBLE);

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    private void enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
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
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().

                                status.startResolutionForResult((Activity) NearByMeVehicle.this, REQUEST_LOCATION);
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

    @Override
    protected void onStop() {
        super.onStop();

        countDownTimer.cancel();
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
               // mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
          //  mMap.setMyLocationEnabled(true);
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


    class maptrackingWithradius extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // getdata.clear();
            mMap.clear();
            progressDialog = ProgressDialog.show(NearByMeVehicle.this, "", "Loading. Please wait...", true, false);
            mMap.addCircle(new CircleOptions()
                    .center(curlatlang)
                    .radius(Double.parseDouble(getradious) * 1000 + 100)
                    .strokeColor(Color.RED)
                    .strokeWidth(4)
                    .fillColor(0x400000FF));
/*
            mMap.addMarker(new MarkerOptions()
                    .position(curlatlang)
                    .title("My Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_marker)));
*/
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter = "http://vts.orissaminerals.gov.in/andorsac/rest/CallService/getVehicleInRange?minename=&latitude=" + lat + "&longitude=" + lag + "&range=" + getradious;

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
                        JSONArray jsonArray = new JSONArray(getResponse);

                        if (jsonArray.length() <= 0) {
                            progressDialog.dismiss();
                            line_sear.setVisibility(View.GONE);
                            lineaar.setVisibility(View.GONE);
                            frame_floating.setVisibility(View.VISIBLE);
                            frame_floating_count.setVisibility(View.VISIBLE);
                            Toast.makeText(NearByMeVehicle.this, "No Vehicle Found..", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                                double longitude = Double.parseDouble(jsonObject.getString("logintude"));

                                currentLat = new LatLng(latitude, longitude);

                                countDownTimer.start();
                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No:- " + jsonObject.getString("vehicleregno"))
                                        .snippet("IMEI No:-" + jsonObject.getString("imeino"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));

                                builder.include(currentLat);
                                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 17));


                            }
                        search_text.setText(jsonArray.length()+"");

                            int padding = 300;
                            /**create the bounds from latlngBuilder to set into map camera*/
                            LatLngBounds bounds = builder.build();


                            /**create the camera with bounds and padding to set into map*/
                            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                           // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
                          //  mMap.animateCamera(cu);

                            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    /**set animated zoom camera into map*/
                                   mMap.animateCamera(cu);

                                }
                            });

                        }
                        progressDialog.dismiss();

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    public void addMarker(GoogleMap googleMap, double lat, double lon) {
        LatLng campos = new LatLng(lat, lon);
        MarkerOptions options = new MarkerOptions();
        mMap = googleMap;
       /* final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(campos)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        LatLng latlong = new LatLng(lat, lon);
        options.position(latlong);*/

        // options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(rid)));
        // mMap.addMarker(options.title("Title"));
        //  mk= mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
        //        .title("My Location")
        //      .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.custom_map_icon))));
        mMap.addMarker(new MarkerOptions()
                .position(campos));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campos, 12));
        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 18));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;

        }
        //startLocationUpdates();
    }
    /*private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mytracking.com.mytracking.circularImage.CircularImageView img = (mytracking.com.mytracking.circularImage.CircularImageView) customMarkerView.findViewById(R.id.countme);
        img.setImageResource(resId);
        // ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
      *//*  TextView markerTextView = (TextView) customMarkerView.findViewById(R.id.countme);
        markerTextView.setText(sequenceNo);
      *//*  //markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }*/
    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK,myIntent);
        super.finish();
    }
}
