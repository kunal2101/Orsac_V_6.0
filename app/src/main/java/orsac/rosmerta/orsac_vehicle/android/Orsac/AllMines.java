package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

public class AllMines extends AppCompatActivity {
    SupportMapFragment fm;
    private GoogleMap mMap;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    CameraUpdate cu;
    private ImageView tool_back_icon;
    String mine_name;
    private MyTextView tool_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_mines);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_title.setText("All Mines");
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        // fm.getMapAsync((OnMapReadyCallback) this);



        mMap = fm.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setPadding(0, 0, 0, 100);
        if (isConnection()) {
          new  maptracking().execute();
        } else {
            Toast.makeText(AllMines.this, "No Internet , Please Try After sometime", Toast.LENGTH_LONG).show();
        }
    }
    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK,myIntent);
        super.finish();
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) AllMines.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }
    class maptracking extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AllMines.this, "","Loading. Please wait...", true, false);
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter = "http://vts.orissaminerals.gov.in/andorsac/rest/CallService/getAllMineDetails";

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

for(int i = 0 ; i < jsonArray.length() ; i++){
    JSONObject jsonObject = jsonArray.getJSONObject(i);
    double latitude = Double.parseDouble(jsonObject.getString("latitude"));
    double longitude = Double.parseDouble(jsonObject.getString("longitude"));

    LatLng currentLat = new LatLng(latitude, longitude);
    mine_name = jsonObject.getString("minename");
    mMap.addMarker(new MarkerOptions()
            .position(currentLat)
            .title(mine_name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_one)));
    builder.include(currentLat);
   // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 17));


}

                        int padding = 50;
                        /**create the bounds from latlngBuilder to set into map camera*/
                        LatLngBounds bounds = builder.build();
                        /**create the camera with bounds and padding to set into map*/
                        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                /**set animated zoom camera into map*/
                                mMap.animateCamera(cu);

                            }
                        });

                         progressDialog.dismiss();

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }

    }
    public void addMarker(GoogleMap googleMap, double lat, double lon ) {
        LatLng campos = new LatLng(lat,lon);
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
}
