package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Developer on 09-Apr-17.
 */

public class MapTrackingActivity_Live_Status extends AppCompatActivity implements GoogleMap.OnMapClickListener {

    SupportMapFragment fm;
    GoogleMap googleMap;
   /* static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);*/

    static final LatLng origin = new LatLng(28.5355, 77.3910);
    static final LatLng dest = new LatLng(28.5562, 77.1000);
    ArrayList<LatLng> points = null;

    Timer timer = new Timer();
    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 10 sec.
    int counter = 0;
    Marker mDesc = null;
    LatLng previousLat;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    MyTextView etp_title, code_title, source, destination, dates, ign, gps, vehic_no,
            circle_name, startTime_new, pass_valid_new;
    TextView min_name_text, quitity_tex, txt_speed, dir_text_new;

    String etpno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_design);
        etp_title = (MyTextView) findViewById(R.id.etp_title);
        code_title = (MyTextView) findViewById(R.id.code_title);
        source = (MyTextView) findViewById(R.id.source);
        destination=(MyTextView)findViewById(R.id.destination) ;
        vehic_no = (MyTextView) findViewById(R.id.vehic_no);
        circle_name = (MyTextView) findViewById(R.id.circle_name);
        startTime_new = (MyTextView) findViewById(R.id.startTime_new);
        pass_valid_new = (MyTextView) findViewById(R.id.pass_valid_new);
        min_name_text = (TextView) findViewById(R.id.min_name_text);
        quitity_tex = (TextView) findViewById(R.id.quitity_tex);
        txt_speed = (TextView) findViewById(R.id.txt_speed);
        dir_text_new = (TextView) findViewById(R.id.dir_text_new);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Tracking");
        Intent inty = getIntent();
        etpno = inty.getStringExtra("etpNo");

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap = fm.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setPadding(0, 0, 0, 100);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

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
             /*   TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + "Latityde");

                // Setting the longitude
                tvLng.setText("Longitude:" + "Laongitude");
*/
                // Returning the view containing InfoWindow contents
                return v;

            }
        });
        new maptracking().execute();

        new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                new maptracking().execute();
                start();
            }
        }.start();

    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

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
            String parameter = "http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/etpwiselivevehicle?vehicleno=" + etpno;

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
                        if(jsonObject.getString("latitude").equalsIgnoreCase("null")){
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapTrackingActivity_Live_Status.this);
                            builder.setMessage("NO DATA FOUND WITH THIS ETP NO")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            Intent inty = new Intent( MapTrackingActivity_Live_Status.this, NavigationActivity.class);
                                            startActivity(inty);
                                            dialog.cancel();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("ORSAC ");
                            alert.show();


                            Toast.makeText(MapTrackingActivity_Live_Status.this, "NO DATA FOUND WITH THIS ETP NO", Toast.LENGTH_LONG).show();
                        }else {

                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));

                            LatLng currentLat = new LatLng(latitude, longitude);
                            etp_title.setText(jsonObject.getString("etpno"));
                            circle_name.setText(jsonObject.getString("circle"));
                            vehic_no.setText(jsonObject.getString("vehicleno"));
                            txt_speed.setText(jsonObject.getString("speed") + " Km");
                            dir_text_new.setText(jsonObject.getString("direction"));
                            source.setText(jsonObject.getString("source"));
                            destination.setText(jsonObject.getString("destination"));
                            startTime_new.setText(jsonObject.getString("starttime"));
                            min_name_text.setText(jsonObject.getString("minename"));
                            quitity_tex.setText(jsonObject.getString("quantity") + " Ton");
                            pass_valid_new.setText("Pass Valid : " + jsonObject.getString("passvalid"));
                            code_title.setText("Code :" + jsonObject.getString("code"));
                            if (counter == 0) {
                                Marker mOrigin = googleMap.addMarker(new MarkerOptions()
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
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }

    }
    /*
    Ending getting contact list from backend
     */


}
