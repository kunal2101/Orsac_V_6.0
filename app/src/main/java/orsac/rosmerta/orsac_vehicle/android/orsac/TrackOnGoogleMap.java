package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

public class TrackOnGoogleMap extends FragmentActivity implements android.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private TextView tool_title;
    Location mlocation;
    private ImageView tool_back_icon;
    ArrayList<HashMap<String, String>> arl;
    double getLatitude, getLongitude;
    private LatLng latlng;
    String markerInfo;
    double lat, longy, lat_sou, laty_sou;

    private LocationManager manager;
    Marker options_bike, option_car;
    private GoogleApiClient googleApiClient;
    //for ployline

    PolylineOptions lineOptions = null;
    LocationRequest mLocationRequest;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    int firt_time = 0;
    LatLngBounds.Builder bld = new LatLngBounds.Builder();
    private ArrayList<LatLng> points; //added
    Polyline line;
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5 * 1000);
        mLocationRequest.setFastestInterval(6 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public BroadcastReceiver GpsChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // do something
            } else {
                ShowGpsDialog();

            }
            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            } else {
                ShowGpsDialog();

            }


        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_on_google_map);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText(" Track On Map ");
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        points = new ArrayList<LatLng>();
        // Getting Map for the SupportMapFragment

        mMap = fm.getMap();

        MarkerOptions options = new MarkerOptions()

                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(new LatLng(28.6302217, 77.3720332));
        mMap.addMarker(options);
        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(TrackOnGoogleMap.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(TrackOnGoogleMap.this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        // Timber.v("Location error " + connectionResult.getErrorCode());
                    }
                }).build();

        googleApiClient.connect();
        //BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
       /* addMarkers("location", "location", 28.6302217, 77.3720332);
        addMarkers("location", "location", 28.6284773, 77.372755);
        addMarkers("location", "location", 20.307946, 85.820179);
        addMarkers("location", "location", 20.307946, 85.820179);
       */ if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mlocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        try {
            lat = mlocation.getLatitude();
            longy = mlocation.getLongitude();
            addMarkers("location", "location", lat, longy);

            // latLng = new LatLng(lat, longy);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }
    private void redrawLine(){

      //  mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
            addMarkers_new(points.get(i));
        }

        //addMarker(); //add Marker in current position
       // line = mMap.addPolyline(options); //add Polyline
    }
    private void addMarkers_new( LatLng myLoc) {
     myLoc = new LatLng(getLatitude, getLongitude);
        option_car = mMap.addMarker(new MarkerOptions().position(myLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.truck)));
       /* option_car = mMap.addMarker(new MarkerOptions().position(myLoc).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        options_bike = mMap.addMarker(new MarkerOptions().position(myLoc).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLatitude, getLongitude), 12));


    }

    private void addMarkers(String userName, String userMobile, double getLatitude, double getLongitude) {
        LatLng myLoc = new LatLng(getLatitude, getLongitude);
        String postTitle = userName + "\n" + userMobile + "\n" + getLatitude + "\n" + getLongitude;
        option_car = mMap.addMarker(new MarkerOptions().position(myLoc).title(postTitle).icon(BitmapDescriptorFactory.fromResource(R.drawable.truck)));
       /* option_car = mMap.addMarker(new MarkerOptions().position(myLoc).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        options_bike = mMap.addMarker(new MarkerOptions().position(myLoc).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLatitude, getLongitude), 12));


    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    private void addMarker(LatLng currentLatLng, int counter, int size) {
        MarkerOptions options = new MarkerOptions();
        if (counter == 0) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (counter == (size - 1)) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
        options.position(currentLatLng);
        //mMap.addMarker(options);
        mMap.addMarker(options.title(markerInfo));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,16));

    }

    @Override
    public void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        try {
            //PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } catch (Exception ev) {
            System.out.print(ev.getMessage());
        }
        // Log.d(TAG, "Location update started ..............: ");
        //Toast.makeText(getApplicationContext(),"Location update started ..............: ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();

        googleApiClient.disconnect();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mlocation = location;
        try {
            lat = mlocation.getLatitude();
            longy = mlocation.getLongitude();
            latlng = new LatLng(lat, longy);
            lat_sou = lat;
            laty_sou = longy;
            if (firt_time == 0)
                for (int i = 1; i <= 10; i++) {
                    double max = 0.1000;
                    double min = 0.0100;

                    Random random = new Random();
                    double randomValue = min + (max - min) * random.nextDouble();

                    lat_sou = lat_sou + randomValue;
                    laty_sou = laty_sou + randomValue;
                    addMarkers("location", "location", lat_sou, laty_sou);
                    bld.include(new LatLng(lat_sou, laty_sou));
                    points.add(new LatLng(lat_sou, laty_sou)); //added


                    firt_time = firt_time + 1;
                }
            LatLngBounds latLngBounds = bld.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
            //redrawLine();
        } catch (Exception e) {
            Toast.makeText(TrackOnGoogleMap.this, "Finding Location please wait", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void ShowGpsDialog() {
        // AndyUtils.removeCustomProgressDialog();
        //  isGpsDialogShowing = true;
        enableLoc();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 199) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // TODO
                   /* Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    Intent inty = new Intent(MainScreen.this, Daily_info_ACtivity.class);
                    startActivity(inty);*/
                    break;
                case Activity.RESULT_CANCELED:
                    // TODO
                    Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    finish();
                    break;

            }
        }

    }

    private void enableLoc() {
        // isGpsDialogShowing = true;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(TrackOnGoogleMap.this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            // Timber.v("Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

           /* locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
           */
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().

                                status.startResolutionForResult(
                                        (Activity) TrackOnGoogleMap.this, 199);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }


    /*
    * end*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ShowGpsDialog();
        }
        registerReceiver(GpsChangeReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(GpsChangeReceiver);


    }

}
