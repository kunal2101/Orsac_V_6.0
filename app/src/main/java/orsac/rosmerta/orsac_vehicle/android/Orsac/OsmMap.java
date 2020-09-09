package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;

import orsac.rosmerta.orsac_vehicle.android.R;

public class OsmMap extends AppCompatActivity {
    private MapView myOpenMapView;
    private MapController myMapController;
    LocationManager locationManager;
    ArrayList<OverlayItem> overlayItemArray;
    MyItemizedIconOverlay myItemizedIconOverlay;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orsac_tracking_on_map);
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        myOpenMapView = (MapView) findViewById(R.id.mapView);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setMultiTouchControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        //myMapController.setZoom(20);
        //--- Create Overlay
        overlayItemArray = new ArrayList<OverlayItem>();
        overlayItemArray.add(new OverlayItem("Title", "Description", new GeoPoint(0.0d,0.0d))); // Lat/Lon decimal degrees

        DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);

       myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);





        myOpenMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(OsmMap.this, "try", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(OsmMap.this, "catch", Toast.LENGTH_LONG).show();

                }

            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //for demo, getLastKnownLocation from GPS only, not from NETWORK
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation
                = locationManager.getLastKnownLocation(
                LocationManager.GPS_PROVIDER);
        if(lastLocation != null){
            updateLoc(lastLocation);
        }

        //Add Scale Bar
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
        myOpenMapView.getOverlays().add(myScaleBarOverlay);
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> myOnItemGestureListener
                = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>(){

            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                return true;
            }
            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }

        };
        myOpenMapView.getOverlays().add(myItemizedIconOverlay);


    }
/*
    ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(overlayItemArray,
            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                    //do something
                    return true;
                }
                @Override
                public boolean onItemLongPress(final int index, final OverlayItem item) {
                    return false;
                }
            });
*/


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
    if(!overlayItemArray.isEmpty()){

    //overlayItemArray have only ONE element only, so I hard code to get(0)
    GeoPoint in = overlayItemArray.get(0).getPoint();
    IMapController mapController = myOpenMapView.getController();
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
    Toast.makeText(OsmMap.this, "catch", Toast.LENGTH_LONG).show();
}

        }


    }
    private void updateLoc(Location loc){
        GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
        myMapController.setCenter(locGeoPoint);

        setOverlayLoc(loc);

        myOpenMapView.invalidate();
    }
    private void setOverlayLoc(Location overlayloc){
        GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
        //---
        overlayItemArray.clear();

        OverlayItem newMyLocationItem = new OverlayItem(
                "My Location", "My Location", overlocGeoPoint);
        overlayItemArray.add(newMyLocationItem);
        //---
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
