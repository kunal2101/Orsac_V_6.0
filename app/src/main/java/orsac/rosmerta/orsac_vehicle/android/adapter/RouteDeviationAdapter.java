package orsac.rosmerta.orsac_vehicle.android.adapter;

/**
 * Created by qr on 25-Oct-17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;

import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.textview.TextView_Lato;
import orsac.rosmerta.orsac_vehicle.android.textview.Textview_lato_thin;

public class RouteDeviationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<HashMap<String,String>> mDataset;
    private Context mcontext;
    private Activity mactivity;
   // private OnItemClickListener listener;
    private FragmentActivity myContext;

    LayoutInflater layoutInflaterAndroid;
    View mView;
    AlertDialog alertNewBooking;
    Button btn_close;
    GoogleMap googleMap;

   /* public interface OnItemClickListener {
        void onItemClick(HashMap<String, String> item, int position);
    }


    public void add(int position, HashMap<String,String> item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void update(int position, HashMap<String,String> item) {
        mDataset.remove(position);
        notifyItemRemoved(position);

        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove( HashMap<String,String> item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }*/

    // Provide a suitable constructor (depends on the kind of dataset)
    public RouteDeviationAdapter(Context context, ArrayList<HashMap<String, String>> myDataset) {

        mcontext = context;
        mDataset = myDataset;

       //mapint();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deviation_list_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolderRow vh = new ViewHolderRow(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        HashMap<String,String> map = mDataset.get(position);

        ViewHolderRow userViewHolder = (ViewHolderRow) holder;

        userViewHolder.txt_date_time.setText("Total Duration : "+map.get("routeDevDuration"));
        userViewHolder.txt_distance.setText("Started at : "+map.get("routeDevStartTime"));
        userViewHolder.txt_ignition.setText("Ended at : "+map.get("routeDevEndTime"));

        // binding item click listner
      // userViewHolder.bind(position, mDataset.get(position), listener);

       /* userViewHolder.btnTrackNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat =0;
                double lng = 0;

                try{
                    lat = Double.parseDouble(mDataset.get(position).get("LAT"));
                    lng = Double.parseDouble(mDataset.get(position).get("LNG"));

                }catch (Exception e){

                }

                if (lat> 0 && lng>0){
                    mapint(lat, lng);
                    //shomap(lat, lng);
                }else{
                    Toast.makeText(myContext,"Invalid location", Toast.LENGTH_LONG).show();
                }

            }
        });*/



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

   /* public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }*/

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolderRow extends RecyclerView.ViewHolder {
        public TextView_Lato txt_date_time;
        Textview_lato_thin txt_distance ,txt_ignition;

        public ViewHolderRow(View v) {
            super(v);

            txt_date_time       = (TextView_Lato)v.findViewById(R.id.txt_date_time);
            txt_distance           = (Textview_lato_thin)v.findViewById(R.id.txt_distance);
            txt_ignition             = (Textview_lato_thin)v.findViewById(R.id.txt_ignition);

           // btnTrackNow         = (TextView_Lato)v.findViewById(R.id.btn_trcak);

        }

       /* public void bind(final int position, final HashMap<String,String> item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
        }*/
    }

    /*private void showMap(){
        Dialog dialog = new Dialog(mcontext,  R.style.conformationDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogmap);
        dialog.show();
        GoogleMap googleMap;


        MapView mMapView;// (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(mcontext);

        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        googleMap = mMapView.getMap();

    }*/

   /* public void mapint(final double lat, final double lng){
       *//* layoutInflaterAndroid = LayoutInflater.from(mcontext);
        mView = layoutInflaterAndroid.inflate(R.layout.dialogmap, null);
        //mView.setBackgroundColor(Color.parseColor("#ffffff"));
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext, R.style.mAlertDialog);
        builder.setView(mView);
        builder.setCancelable(true);

        btn_close = (Button) mView.findViewById(R.id.btn_close);

        alertNewBooking = builder.create();

        // SupportMapFragment fm;
        // GoogleMap googleMap;
        // LatLng latlng = new LatLng(lat, lng);

        //fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Fragment fragManager = myContext.getSupportFragmentManager().findFragmentById(R.id.map);
       // SupportMapFragment fm =(SupportMapFragment)  myContext.getSupportFragmentManager().findFragmentById(R.id.gmap);
        //googleMap = fm.getMap();
        ((SupportMapFragment) myContext.getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setPadding(0, 0, 0, 100);*//*


        final Dialog dialog = new Dialog(mactivity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setContentView(R.layout.dialogmap);

        MapView mMapView = (MapView) dialog.findViewById(R.id.gmap);
        MapsInitializer.initialize(mactivity);

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap gMap) {
                googleMap = gMap;
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                LatLng latlng = new LatLng(lat, lng);
                googleMap.clear();

                //adding marker on google map
                googleMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
            }
        });
        //shomap(lat, lng);

        btn_close = (Button) dialog.findViewById(R.id.btn_close);
       *//* LatLng latlng = new LatLng(lat, lng);
        googleMap.clear();


        //adding marker on google map
        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));*//*

        // alertNewBooking.show();
        // Button dialogButton = (Button) dialog.findViewById(R.id.btn_tutup);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
*/
   /* private void shomap(double lat, double lng){
         //AlertDialog alertNewBooking;
        myContext=(FragmentActivity) mactivity;

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mcontext);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialogmap, null);
        //mView.setBackgroundColor(Color.parseColor("#ffffff"));
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext, R.style.mAlertDialog);
        builder.setView(mView);
        builder.setCancelable(true);

        Button btn_close = (Button) mView.findViewById(R.id.btn_close);

        final AlertDialog alertNewBooking = builder.create();

        // SupportMapFragment fm;
       // GoogleMap googleMap;
          LatLng latlng = new LatLng(lat, lng);

        //fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
       // Fragment fragManager = myContext.getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment fm =(SupportMapFragment)  myContext.getSupportFragmentManager().findFragmentById(R.id.gmap);
        GoogleMap  googleMap = fm.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setPadding(0, 0, 0, 100);

        //adding marker on google map
        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        alertNewBooking.show();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertNewBooking.dismiss();
            }
        });
    }*/

   /* private void shomap(double lat, double lng){
        //AlertDialog alertNewBooking;
        //  myContext=(FragmentActivity) mactivity;


        LatLng latlng = new LatLng(lat, lng);
        googleMap.clear();


        //adding marker on google map
        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_truck_green)));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        alertNewBooking.show();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertNewBooking.dismiss();
            }
        });
    }*/

}