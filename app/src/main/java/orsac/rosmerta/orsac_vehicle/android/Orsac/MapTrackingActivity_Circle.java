package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Developer on 09-Apr-17.
 */

public class MapTrackingActivity_Circle extends AppCompatActivity implements GoogleMap.OnMapClickListener {

    SupportMapFragment fm;
    GoogleMap googleMap;
   /* static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);*/

    static final LatLng origin = new LatLng(28.5355, 77.3910);
    static final LatLng dest = new LatLng(28.5562, 77.1000);
    ArrayList<LatLng> points = null;
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
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
    String st_etp,st_code_title, st_source, st_destination,  st_vehic_no,
            st_circle_name, st_startTime_new, st_pass_valid_new,st_min_name_text, st_quitity_tex, st_txt_speed, st_dir_text_new;
    CountDownTimer countDownTimer ;
    String etpno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_design_circle);
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
        etpno = inty.getStringExtra("etpno");

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //fm.getMapAsync((OnMapReadyCallback) this);
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
                etp_title = (MyTextView) v.findViewById(R.id.etp_title);
                code_title = (MyTextView) v.findViewById(R.id.code_title);
                source = (MyTextView) v.findViewById(R.id.source);
                destination=(MyTextView)v.findViewById(R.id.destination) ;
                vehic_no = (MyTextView) v.findViewById(R.id.vehic_no);
                circle_name = (MyTextView) v.findViewById(R.id.circle_name);
                startTime_new = (MyTextView) v.findViewById(R.id.startTime_new);
                pass_valid_new = (MyTextView) v.findViewById(R.id.pass_valid_new);
                min_name_text = (TextView) v.findViewById(R.id.min_name_text);
                quitity_tex = (TextView) v.findViewById(R.id.quitity_tex);
                txt_speed = (TextView) v.findViewById(R.id.txt_speed);
                dir_text_new = (TextView) v.findViewById(R.id.dir_text_new);

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
                etp_title.setText(st_etp);
                circle_name.setText(st_circle_name);
                vehic_no.setText(st_vehic_no);
                txt_speed.setText(st_txt_speed);
                dir_text_new.setText(st_dir_text_new);
                source.setText(st_source);
                destination.setText(st_destination);
                startTime_new.setText(st_startTime_new);
                min_name_text.setText(st_min_name_text);
                quitity_tex.setText(st_quitity_tex);
                pass_valid_new.setText(st_pass_valid_new);
                code_title.setText(st_code_title);

                return v;

            }
        });
        new maptracking().execute();

        countDownTimer = new CountDownTimer(20000, 20000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(counter > 0) {
                    new maptracking().execute();
                    start();
                }
            }
        };
        countDownTimer.start();

    }
    public  void  timerclass(){
        countDownTimer = new CountDownTimer(20000, 20000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new maptracking().execute();
                start();
            }
        };
        countDownTimer.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        isCanceled = true;
        countDownTimer.cancel();
        finish();
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
            progressDialog = ProgressDialog.show(MapTrackingActivity_Circle.this, "","Loading. Please wait...", true, false);
        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter = "http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/etpwiselive?etpno=" + etpno;
//L111701565/307
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
                            isCanceled = true;

                            AlertDialog.Builder builder = new AlertDialog.Builder(MapTrackingActivity_Circle.this);
                            builder.setMessage("NO DATA FOUND WITH THIS ETP NO")
                                    .setCancelable(false)

                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                          /*  Intent inty = new Intent( MapTrackingActivity_Circle.this, DetailActivty_Sec.class);
                                            startActivity(inty);*/
                                            dialog.cancel();
                                            isCanceled = true;
                                            countDownTimer.cancel();
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("ORSAC ");
                            alert.show();


                            Toast.makeText(MapTrackingActivity_Circle.this, "NO DATA FOUND WITH THIS ETP NO", Toast.LENGTH_LONG).show();
                        }else {
                            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonObject.getString("longitude"));

                            LatLng currentLat = new LatLng(latitude, longitude);

                            st_etp = jsonObject.getString("etpno");
                            st_circle_name = jsonObject.getString("circle");
                            st_vehic_no = jsonObject.getString("vehicleno");
                            st_txt_speed = jsonObject.getString("speed") + " Km";
                            st_dir_text_new = jsonObject.getString("direction");
                            st_source = jsonObject.getString("source");
                            st_destination = jsonObject.getString("destination");
                            st_startTime_new = jsonObject.getString("starttime");
                            st_min_name_text = jsonObject.getString("minename");
                            st_quitity_tex = jsonObject.getString("quantity") + " Ton";
                            st_pass_valid_new = jsonObject.getString("passvalid");
                            st_code_title = jsonObject.getString("code");
                            progressDialog.dismiss();

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
                              //  Toast.makeText(MapTrackingActivity_Circle.this, "" + previousLat, Toast.LENGTH_LONG).show();
                                googleMap.addPolyline(new PolylineOptions()
                                        .add(previousLat, currentLat)
                                        .width(5).color(Color.BLUE).geodesic(true));
                            }

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLat, 17));
                            previousLat = currentLat;
                            counter++;
                           // timerclass();
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
