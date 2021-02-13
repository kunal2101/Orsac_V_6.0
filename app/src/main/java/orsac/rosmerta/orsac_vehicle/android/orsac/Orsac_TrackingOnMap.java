package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.UrlContants;

public class Orsac_TrackingOnMap extends AppCompatActivity {
    MapView mapView;
    MapController mapController;
    private TextView tool_title;
    private ImageView tool_back_icon;
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

    int minLat = Integer.MAX_VALUE;
    int maxLat = Integer.MIN_VALUE;
    int minLong = Integer.MAX_VALUE;
    int maxLong = Integer.MIN_VALUE;
    MyItemizedIconOverlay myItemizedIconOverlay;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orsac_tracking_on_map);

        tool_title=(TextView)findViewById(R.id.tool_title);
        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(Orsac_TrackingOnMap.this, NavigationActivity.class);
                startActivity(inty);
                finish();
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText("Track Me");

        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = (MapController) mapView.getController();
        mapController.setZoom(17);

        DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);

        myItemizedIconOverlay = new MyItemizedIconOverlay(items, null, defaultResourceProxyImpl);
        new GetAllShelterDetail().execute();
    }
    private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> {

        public MyItemizedIconOverlay(
                List<OverlayItem> pList,
                org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
                ResourceProxy pResourceProxy) {
            super(pList, pOnItemGestureListener, pResourceProxy);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void draw(Canvas canvas, MapView mapview, boolean arg2) {
            // TODO Auto-generated method stub
            super.draw(canvas, mapview, arg2);
            try{
                if(!items.isEmpty()){

                    //overlayItemArray have only ONE element only, so I hard code to get(0)
                    GeoPoint in = items.get(0).getPoint();
                    IMapController mapController = mapView.getController();
                    mapController.setZoom(16);

                    mapController.setCenter(in);
                    Point out = new Point();
                    mapview.getProjection().toPixels(in, out);

                    Bitmap bm = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_placeholder_black_shape_for_localization_on_maps_1);
                    canvas.drawBitmap(bm,
                            out.x - bm.getWidth()/2,  //shift the bitmap center
                            out.y - bm.getHeight()/2,  //shift the bitmap center
                            null);
                }
            }catch (Exception e){
                Toast.makeText(Orsac_TrackingOnMap.this, "catch", Toast.LENGTH_LONG).show();
            }

        }



    }

    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Orsac_TrackingOnMap.this, "Loading...", "Please Wait...", true);

        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = UrlContants.BASE_URL_NRDA;
                HttpPost httppost = new HttpPost(url);


                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
            progressDialog.dismiss();
            try{
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");


                if(success.equalsIgnoreCase("true")){
                    JSONArray jarray = jsonObj.getJSONArray("ShelterDetails");
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject dataObj = jarray.getJSONObject(i);


                        map.put("KEY_LATITUDE", dataObj.getString("28.6227819"));
                        map.put("KEY_LONGITUDE", dataObj.getString("77.3907584"));

                        try{

                            double getLatitude = Double.parseDouble(dataObj.getString("28.6227819"));
                            double getLongitude = Double.parseDouble(dataObj.getString("77.3907584"));

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
                            Toast.makeText(Orsac_TrackingOnMap.this, "" + ev.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
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

            }
            catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }
    public void addmarker(GeoPoint vechilePoint, String markerInfo)
    {
        Drawable tempMarker = getResources().getDrawable(R.drawable.ic_placeholder_black_shape_for_localization_on_maps_1);
        Bitmap bitmap = ((BitmapDrawable) tempMarker).getBitmap();
        Drawable currentMarker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

        final Marker startMarker = new Marker(mapView);
        startMarker.setPosition(vechilePoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(currentMarker);
        startMarker.setTitle(markerInfo);
        mapView.getOverlays().add(startMarker);
      //  startMarker.setInfoWindow(new MapCustomInfoBubble(mapView));
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


}
