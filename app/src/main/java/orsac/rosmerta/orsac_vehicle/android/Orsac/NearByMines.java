package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
/*import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;*/
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
/*import tapzo.demo.com.tapzo.Orsac.Model.Person;*/
import orsac.rosmerta.orsac_vehicle.android.R;

public class NearByMines extends AppCompatActivity {
    SearchableSpinner spinner_source, spinner_radious;

    ArrayList<String> demo_list;
    ArrayList<String> radius;
    Button button;
    LinearLayout lineaar, line_sear;
    EditText km_edit;
    String getValue, getradious, lat, lag;
    FrameLayout frame_floating,frame_floating_count;
    ImageView img_search;
    TextView search_text;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private ImageView icon_tool;
    private GoogleMap googlemap;
    SupportMapFragment fm;
    // private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);
    private GoogleMap mMap;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    CameraUpdate cu;
    ArrayList<HashMap<String, String>> getdata;
    Map<String, String> map;
    LatLng currentLat, curlatlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_mines);
        map = new HashMap<String, String>();
        frame_floating_count = (FrameLayout) findViewById(R.id.frame_floating_count);
        search_text =(TextView)findViewById(R.id.search_text);
        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_source);
        spinner_radious = (SearchableSpinner) findViewById(R.id.spinner_radious);
        frame_floating = (FrameLayout) findViewById(R.id.frame_floating);
        img_search = (ImageView) findViewById(R.id.img_search);
        button = (Button) findViewById(R.id.button);
        lineaar = (LinearLayout) findViewById(R.id.lineaar);
        line_sear = (LinearLayout) findViewById(R.id.line_sear);
        //km_edit = (EditText)findViewById(R.id.km_edit);
        spinner_source.setTitle("Select Mines");
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
        new maptracking().execute();
       /* demo_list.add("Bhairpur Lakhraj Fireclay Mines");
        demo_list.add("Chandpur Quqrtz & Quartzite Mines");
        demo_list.add("Mid East Int.Steel Ltd.(MESCO)\n");
        demo_list.add("Kaniha OCP W/B");
        demo_list.add("Samal Check-gate");
        demo_list.add("JHANKARPALI CHINACLAY MINES");*/
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_title.setText("Near By Mines");

        ArrayAdapter adapters_radius = new ArrayAdapter<String>(NearByMines.this, R.layout.spinner_item, radius);
        spinner_radious.setAdapter(adapters_radius);
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        // fm.getMapAsync((OnMapReadyCallback) this);


        mMap = fm.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setPadding(0, 0, 0, 100);

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView) view.findViewById(R.id.sr_source);
                getValue = getsource.getText().toString();
                HashMap<String, String> map = getdata.get(position);
                lat = map.get("lat");
                lag = map.get("lag");
                String mines_name = map.get("name");

                curlatlang = new LatLng(Double.valueOf(lat), Double.valueOf(lag));
                mMap.addMarker(new MarkerOptions()
                        .position(curlatlang)
                        .title(mines_name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_marker)));

                //  Toast.makeText(NearByMines.this, ""+lag+"new"+lat, Toast.LENGTH_SHORT).show();
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(getradious) && !TextUtils.isEmpty(getValue)) {
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
                    Toast.makeText(NearByMines.this, "Select Mines and Radius", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class maptracking extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NearByMines.this, "", "Loading. Please wait...", true, false);
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
                        getdata = new ArrayList<HashMap<String, String>>();
                        demo_list = new ArrayList<String>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));

                            HashMap<String, String> mMaps = new HashMap<String, String>();
                            mMaps.put("lat", String.valueOf(latitude));
                            mMaps.put("lag", String.valueOf(longitude));
                            mMaps.put("name", jsonObject.getString("minename"));
                            getdata.add(mMaps);
                            demo_list.add(jsonObject.getString("minename"));

                        }
                        ArrayAdapter adapters = new ArrayAdapter<String>(NearByMines.this, R.layout.spinner_item, demo_list);
                        spinner_source.setAdapter(adapters);
                        progressDialog.dismiss();

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }

    }

    class maptrackingWithradius extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // getdata.clear();
            mMap.clear();
            progressDialog = ProgressDialog.show(NearByMines.this, "", "Loading. Please wait...", true, false);
            mMap.addCircle(new CircleOptions()
                    .center(curlatlang)
                    .radius(Double.parseDouble(getradious) * 1000 + 100)
                    .strokeColor(Color.RED)
                    .strokeWidth(4)
                    .fillColor(0x400000FF));
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
                            Toast.makeText(NearByMines.this, "No Vehicle Found..", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                                double longitude = Double.parseDouble(jsonObject.getString("logintude"));

                                currentLat = new LatLng(latitude, longitude);


                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLat)
                                        .title("Vehicle No:- " + jsonObject.getString("vehicleregno"))
                                        .snippet("IMEI No:-" + jsonObject.getString("imeino"))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));

                                builder.include(currentLat);
                                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 17));


                            }
                                search_text.setText(jsonArray.length()+"");

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
