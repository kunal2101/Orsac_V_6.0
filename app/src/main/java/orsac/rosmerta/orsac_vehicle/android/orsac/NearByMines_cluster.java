/*
package tapzo.demo.com.tapzo.Orsac;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tapzo.demo.com.tapzo.Orsac.Model.MultiDrawable;
import tapzo.demo.com.tapzo.Orsac.Model.Person;
import tapzo.demo.com.tapzo.R;

public class NearByMines_cluster extends AppCompatActivity implements OnMapReadyCallback ,ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
    SearchableSpinner spinner_source;
    ArrayList<String> demo_list ;
    Button button;
    LinearLayout lineaar , line_sear;
    EditText km_edit;
    String getValue;
    FrameLayout frame_floating;
    ImageView img_search;
    private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_mines);


        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_source);
        frame_floating=(FrameLayout)findViewById(R.id.frame_floating);
        img_search = (ImageView)findViewById(R.id.img_search);
        button = (Button)findViewById(R.id.button);
        lineaar= (LinearLayout)findViewById(R.id.lineaar);
        line_sear = (LinearLayout)findViewById(R.id.line_sear);
        km_edit = (EditText)findViewById(R.id.km_edit);
        spinner_source.setTitle("Select Mines");
        demo_list = new ArrayList<String>();
        demo_list.add("BABA Ji ");
        demo_list.add("BABA Ji ");
        demo_list.add("BABA Ji ");
        demo_list.add("BABA Ji ");
        demo_list.add("BABA Ji ");
        demo_list.add("BABA Ji ");
        ArrayAdapter adapters = new ArrayAdapter<String>(NearByMines_cluster.this,R.layout.spinner_item, demo_list);
        spinner_source.setAdapter(adapters);

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                 getValue = getsource.getText().toString();
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
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(km_edit.getText().toString() ) && !TextUtils.isEmpty(getValue)){
                    line_sear.setVisibility(View.GONE);
                    lineaar.setVisibility(View.GONE);
                    frame_floating.setVisibility(View.VISIBLE);
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return;
                }else {
                    Toast.makeText(NearByMines_cluster.this, "Select Mines and Radius", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
        if (mMap != null) {
            return;
        }

        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
     //   googleMap.setOnCameraIdleListener(mClusterManager);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        // permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mClusterManager = new ClusterManager<Person>(this, googleMap);
        mClusterManager.setRenderer(new PersonRenderer());
       // googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(NearByMines_cluster.this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();

    }
    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mClusterManager.addItem(new Person(position(), "Walter", R.drawable.ic_travelling_vehicles_of_a_road));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mClusterManager.addItem(new Person(position(), "Gran", R.drawable.ic_map_truck_red));

        // http://www.flickr.com/photos/nypl/3111525394/
        mClusterManager.addItem(new Person(position(), "Ruth", R.drawable.ic_map_truck_green));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mClusterManager.addItem(new Person(position(), "Stefan", R.drawable.ic_map_truck_red));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mClusterManager.addItem(new Person(position(), "Mechanic", R.drawable.ic_travelling_vehicles_of_a_road));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mClusterManager.addItem(new Person(position(), "Yeats", R.drawable.ic_map_truck_red));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mClusterManager.addItem(new Person(position(), "John", R.drawable.ic_map_truck_green));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mClusterManager.addItem(new Person(position(), "Trevor the Turtle", R.drawable.ic_travelling_vehicles_of_a_road));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        mClusterManager.addItem(new Person(position(), "Teach", R.drawable.ic_map_truck_red));
    }
    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Person person) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person person) {

    }

    private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

}
*/
