package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by qr on 18-May-16.
 */
public class AllTracking extends AppCompatActivity {


    MapView mapView;
    MapController mapController;
    ResourceProxy resourceProxy;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private ImageView icon_tool;
    int minLat = Integer.MAX_VALUE;
    int maxLat = Integer.MIN_VALUE;
    int minLong = Integer.MAX_VALUE;
    int maxLong = Integer.MIN_VALUE;
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    String etpno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vechile_track);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        icon_tool = (ImageView) findViewById(R.id.icon_tool);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText(" Details ");
        Intent inty = getIntent();
        etpno = inty.getStringExtra("etpNo");

        //GeoPoint currentPoint = new GeoPoint(Double.parseDouble("28.62497670"), Double.parseDouble("77.38634930"));

        mapView = (MapView)findViewById(R.id.mapView);
       // mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapView.setMultiTouchControls(true);

        mapController = (MapController) mapView.getController();
        mapController.setZoom(17);
        //mapController.setCenter(currentPoint);

        new maptracking().execute();

        int delay = 10000; // delay for 1 sec.
        int period = 10000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
               // new TrackingBackground().execute();
            }
        }, delay, period);
    }

    /*
* Start server data load for tab selection.
*
*/
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

/*
    class AllTrackerData extends AsyncTask<String, String, String> {

        //CustomProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(AllTracking.this, "Getting Track Detail", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            int userId = 0;

            PreferenceHelper preferenceHelper = new PreferenceHelper(AllTracking.this);

            try{
                userId = Integer.parseInt(preferenceHelper.getUser_id());
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                userId = 0;
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userid", ""+userId));

            if(userId > 0) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Const.GetAllLiveVehicles);

                    // Add your data
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    getResponse = EntityUtils.toString(response.getEntity());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {

                    try{
                        JSONObject jsonObj = new JSONObject(getResponse);
                        boolean getStatus = false ;
                        try{
                            getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                        }catch (Exception ev){
                            System.out.print(ev.getMessage());
                        }

                        if(getStatus == true){

                            JSONArray dataList = jsonObj.getJSONArray("VehicleDetails");
                            for (int i = 0; i < dataList.length(); i++) {

                                JSONObject dataObj = dataList.getJSONObject(i);
                                final HashMap<String, String> map = new HashMap<String, String>();
                                map.put("KEY_VEHICLE_NO", dataObj.getString("vehicleno"));
                                map.put("KEY_IMEI_NO", dataObj.getString("unitid"));
                                map.put("KEY_TIMESTAMP", dataObj.getString("datatimestamp"));
                                map.put("KEY_LATITUDE", dataObj.getString("latitude"));
                                map.put("KEY_LONGITUDE", dataObj.getString("longitude"));
                                map.put("KEY_GPSSTATUS", dataObj.getString("gpsstatus"));
                                map.put("KEY_SPEED", dataObj.getString("speed"));
                                map.put("KEY_LOCATION", dataObj.getString("location"));
                                map.put("KEY_DIRECTION", dataObj.getString("direction"));

                                //userData.add(map);
                                try{

                                    double getLatitude = Double.parseDouble(dataObj.getString("latitude"));
                                    double getLongitude = Double.parseDouble(dataObj.getString("longitude"));

                                    if(getLatitude > 0 && getLongitude > 0) {

                                        GeoPoint vechilePoint = new GeoPoint(getLatitude, getLongitude);
                                        String markerInfo = map.get("KEY_VEHICLE_NO")+"`"+map.get("KEY_LOCATION")+"`"+map.get("KEY_TIMESTAMP");

                                        OverlayItem getitem = new OverlayItem("","",vechilePoint);
                                        items.add(getitem);

                                        addmarker(vechilePoint, markerInfo);

                                        mapView.invalidate();
                                        mapController.setCenter(vechilePoint);

                                    }

                                }catch (Exception ev){
                                    Toast.makeText(AllTracking.this, "" + ev.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }//close json for loop

                            for (OverlayItem item : items) {
                                GeoPoint point = item.getPoint();
                                if (point.getLatitudeE6() < minLat)
                                    minLat = point.getLatitudeE6();
                                if (point.getLatitudeE6() > maxLat)
                                    maxLat = point.getLatitudeE6();
                                if (point.getLongitudeE6() < minLong)
                                    minLong = point.getLongitudeE6();
                                if (point.getLongitudeE6() > maxLong)
                                    maxLong = point.getLongitudeE6();
                            }

                            BoundingBoxE6 boundingBox = new BoundingBoxE6(maxLat, maxLong, minLat, minLong);
                            mapView.zoomToBoundingBox(boundingBox);

                        }else{
                            Toast.makeText(AllTracking.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ev){
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                }
            });
        }

    }
*/

    /*
      * Start server data load for tab selection.
      *
      */
    class maptracking extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(MapTrackingActivity.this, "","Loading. Please wait...", true, false);
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter = "http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/etpwiselive?etpno=" + etpno;

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
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        final HashMap<String, String> map = new HashMap<String, String>();
                        double getLatitude = Double.parseDouble(jsonObject.getString("latitude"));
                        double getLongitude = Double.parseDouble(jsonObject.getString("longitude"));

                       // LatLng currentLat = new LatLng(latitude, longitude);
                        try{


                            if(getLatitude > 0 && getLongitude > 0) {

                                GeoPoint vechilePoint = new GeoPoint(getLatitude, getLongitude);
                                String markerInfo = map.get("KEY_VEHICLE_NO")+"`"+map.get("KEY_LOCATION")+"`"+map.get("KEY_TIMESTAMP");

                                OverlayItem getitem = new OverlayItem("","",vechilePoint);
                                items.add(getitem);

                                addmarker(vechilePoint, markerInfo);

                                mapView.invalidate();
                                mapController.setCenter(vechilePoint);

                            }

                        }catch (Exception ev){
                            Toast.makeText(AllTracking.this, "" + ev.getMessage(), Toast.LENGTH_LONG).show();
                        }

                      /*  etp_title.setText(jsonObject.getString("etpno"));
                        circle_name.setText(jsonObject.getString("circle"));
                        vehic_no.setText(jsonObject.getString("vehicleno"));
                        txt_speed.setText(jsonObject.getString("speed")+" Km");
                        dir_text_new.setText(jsonObject.getString("direction"));
                        source.setText(jsonObject.getString("source"));
                        destination.setText(jsonObject.getString("destination"));
                        startTime_new.setText("StartTime : "+jsonObject.getString("starttime"));
                        min_name_text.setText(jsonObject.getString("minename"));
                        quitity_tex.setText(jsonObject.getString("quantity")+ " Ton");
                        pass_valid_new.setText("Pass Valid : "+jsonObject.getString("passvalid"));
                        code_title.setText("Code :"+jsonObject.getString("code"));
                        dir_text_newss.setText(jsonObject.getString("starttime"));
                        if (counter == 0) {
                            com.google.android.gms.maps.model.Marker mOrigin = googleMap.addMarker(new MarkerOptions()
                                    .position(currentLat)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_red)));
                        } else {
                            if (mDesc != null) {
                                mDesc.remove();
                            }

                            mDesc = googleMap.addMarker(new MarkerOptions()
                                    .position(currentLat)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));

                            googleMap.addPolyline(new PolylineOptions()
                                    .add(previousLat, currentLat)
                                    .width(5).color(Color.BLUE).geodesic(true));
                        }

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 12));
                        previousLat = currentLat;
                        counter++;
                        // progressDialog.dismiss();
*/
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }

    }

/*
    class TrackingBackground extends AsyncTask<String, String, String> {

        //CustomProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //AndyUtils.showCustomProgressDialog(AllTracking.this, "Getting", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            int userId = 0;

            PreferenceHelper preferenceHelper = new PreferenceHelper(AllTracking.this);

            try{
                userId = Integer.parseInt(preferenceHelper.getUser_id());
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                userId = 0;
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userid", "" + userId));

            if(userId > 0) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Const.GetAllLiveVehicles);

                    // Add your data
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    getResponse = EntityUtils.toString(response.getEntity());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {

                    try{
                        JSONObject jsonObj = new JSONObject(getResponse);
                        boolean getStatus = false ;
                        try{
                            getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                        }catch (Exception ev){
                            System.out.print(ev.getMessage());
                        }

                        if(getStatus == true){
                            mapView.getOverlays().clear();

                            //startMarker.remove(mapView);
                            JSONArray dataList = jsonObj.getJSONArray("VehicleDetails");
                            for (int i = 0; i < dataList.length(); i++) {

                                JSONObject dataObj = dataList.getJSONObject(i);
                                final HashMap<String, String> map = new HashMap<String, String>();
                                map.put("KEY_VEHICLE_NO", dataObj.getString("vehicleno"));
                                map.put("KEY_IMEI_NO", dataObj.getString("unitid"));
                                map.put("KEY_TIMESTAMP", dataObj.getString("datatimestamp"));
                                map.put("KEY_LATITUDE", dataObj.getString("latitude"));
                                map.put("KEY_LONGITUDE", dataObj.getString("longitude"));
                                map.put("KEY_GPSSTATUS", dataObj.getString("gpsstatus"));
                                map.put("KEY_SPEED", dataObj.getString("speed"));
                                map.put("KEY_LOCATION", dataObj.getString("location"));
                                map.put("KEY_DIRECTION", dataObj.getString("direction"));
                                try{

                                    double getLatitude = Double.parseDouble(dataObj.getString("latitude"));
                                    double getLongitude = Double.parseDouble(dataObj.getString("longitude"));

                                    if(getLatitude > 0 && getLongitude > 0) {

                                        GeoPoint vechilePoint = new GeoPoint(getLatitude, getLongitude);
                                        String markerInfo = map.get("KEY_VEHICLE_NO")+"`"+map.get("KEY_LOCATION")+"`"+map.get("KEY_TIMESTAMP");

                                        addmarker(vechilePoint, markerInfo);
                                        mapView.invalidate();
                                    }

                                }catch (Exception ev){
                                    Toast.makeText(AllTracking.this, "" + ev.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        }else{
                            Toast.makeText(AllTracking.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ev){
                        System.out.print(ev.getMessage());
                    }
                    //AndyUtils.removeCustomProgressDialog();
                }
            });
        }

    }
*/

    /*
     *
     */

    public void addmarker(GeoPoint vechilePoint, String markerInfo)
    {
        Drawable tempMarker = getResources().getDrawable(R.drawable.ic_map_truck_red);
        Bitmap bitmap = ((BitmapDrawable) tempMarker).getBitmap();
        Drawable currentMarker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

        final Marker startMarker = new Marker(mapView);
        startMarker.setPosition(vechilePoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(currentMarker);
        startMarker.setTitle(markerInfo);
        mapView.getOverlays().add(startMarker);
        startMarker.setInfoWindow(new MapCustomInfoBubble(mapView));
        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                HashMap<String, String> mapp = null;
                String geTetle = marker.getTitle();
                try {

                   /* //Toast.makeText(AllTracking.this,"setOnMarkerClickListener\n"+geTetle,Toast.LENGTH_LONG).show();
                    if(marker.isInfoWindowShown()){
                        marker.closeInfoWindow();
                    }else {
                        marker.showInfoWindow();
                    }*/
                } catch (Exception ev) {

                }
                marker.showInfoWindow();
                return false;
            }
        });

    }




    public class MapCustomInfoBubble extends InfoWindow {

        public MapCustomInfoBubble(MapView mapView) {

            super(R.layout.custom_track_marker, mapView);//my custom layout and my mapView
        }
        @Override
        public void onClose() {
            //by default, do nothing
            //Toast.makeText(AllTracking.this,"setOnMarkerClickListener\n",Toast.LENGTH_LONG).show();

        }
        @Override
        public void onOpen(Object item) {
            final Marker marker  = (Marker)item;
            //the marker on which you click to open the bubble
            //String title = marker.getTitle();
            String getMarkerInfo = marker.getTitle();
            String[] arrMarkerInfo = getMarkerInfo.split("`");

            TextView nameTxt    = (TextView)mView.findViewById(R.id.nameTxt);
            TextView addressTxt = (TextView)mView.findViewById(R.id.addressTxt);
            TextView timeTxt    = (TextView)mView.findViewById(R.id.timeTxt);

            Button btnClose     = (Button)mView.findViewById(R.id.btnClose);

            String CurrentString = arrMarkerInfo[1];
            String[] separated = CurrentString.split(" ");
            String getAddress = "";
            if (separated.length > 6) {
                for (int ind = 0; ind < separated.length; ind++) {
                    if (ind < 6) {
                        if (getAddress.equalsIgnoreCase("")) {
                            getAddress = separated[ind];
                        } else {
                            getAddress = getAddress + " " + separated[ind];
                        }
                    } else if (ind == 6) {
                        getAddress = getAddress + "\n" + separated[ind];
                    } else {
                        getAddress = getAddress + " " + separated[ind];
                    }
                }
            } else {
                getAddress = CurrentString;
            }

            nameTxt.setText("" + arrMarkerInfo[0]);
            addressTxt.setText("" + getAddress);
            timeTxt.setText("" + arrMarkerInfo[2]);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    marker.closeInfoWindow();
                }
            });
        }

    }

}
