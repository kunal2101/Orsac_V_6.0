package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.orsac.BarGraphChart;
import orsac.rosmerta.orsac_vehicle.android.orsac.CirclePieChart;
import orsac.rosmerta.orsac_vehicle.android.orsac.NavigationDrawerFragment;
import orsac.rosmerta.orsac_vehicle.android.orsac.Orsac_Admin_sec_Trip_count;
import orsac.rosmerta.orsac_vehicle.android.orsac.PieNewChart;
import orsac.rosmerta.orsac_vehicle.android.orsac.StackedbarChart;

public class NavigationActivity_backup extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private RecyclerView recyclerView;
    private List<Admin_model> array_admin;
    Admin_model admin_model;
    private Myadapter mAdapter;

    Map map;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private TextView[] dots;
    private AdminPage.MyViewPagerAdapter myViewPagerAdapter;
    String imageUri, imageUri_one, imageUri_two, imageUri_three, imageUri_four;
    int[] imags = {R.drawable.fasttra_two, R.drawable.rose_six, R.drawable.etrans_two,R.drawable.atlanata_three, R.drawable.arya_three };
    static int i = 0;
    ArrayList<HashMap<String, String>> GetCompanyDetail;
    private GridLayoutManager lLayout;
    private ArrayList<Admin_model> companyList;
    String totalcompanies, totaltripcountcnt, totaldevices, totalinstalled, installedDevices, companyName, totalDevices, availabledevices, tripcount;

    TextView tot_com, tot_trip, tot_dev, veh_trac;
    LinearLayout piechart, stackedbar, bargraph, circlepie;
    FrameLayout navi_frame;
    DrawerLayout drawer;
    float value_avai[];
    float value_inst[];
    String value_comap[];
    double[] tripCount;
    String[] compy_name;
    Handler mHandler = new Handler();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private final static int INTERVAL = 1000 * 60 *3  ;
int tot_device, tot_install;
    // Float[] value_inst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        tot_com = (TextView) findViewById(R.id.tot_com);
        tot_trip = (TextView) findViewById(R.id.tot_trip);
        tot_dev = (TextView) findViewById(R.id.tot_dev);
        veh_trac = (TextView) findViewById(R.id.veh_trac);
        piechart = (LinearLayout) findViewById(R.id.piechart);
        stackedbar = (LinearLayout) findViewById(R.id.stackedbar);
        bargraph = (LinearLayout) findViewById(R.id.bargraph);
        circlepie = (LinearLayout) findViewById(R.id.circlepie);

        final Handler handler = new Handler();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        map = new HashMap();
        companyList = new ArrayList<>();
        //login_info();
         if(isConnection()){
             new Authentication().execute();
         }else{
             Toast.makeText(NavigationActivity_backup.this,"No Internet , Please Try After sometime",Toast.LENGTH_LONG).show();
         }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        array_admin = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navi_frame = (FrameLayout) findViewById(R.id.navi_frame);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
        navi_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer( GravityCompat.START);
            }
        });
        ImageView imgview = (ImageView) findViewById(R.id.navigationIcon);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(GravityCompat.START);

            }
        });

        piechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(NavigationActivity_backup.this, PieNewChart.class);
                inty.putExtra("tot_device",tot_device);
                inty.putExtra("tot_install",tot_install);
                startActivity(inty);
            }
        });

        stackedbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b=new Bundle();

                b.putFloatArray("key_avail", value_avai);
                b.putFloatArray("key_inst", value_inst);
                b.putCharSequenceArray("key_comp",value_comap);
                Intent inty = new Intent(NavigationActivity_backup.this, StackedbarChart.class);
             // inty.putStringArrayListExtra("avai_arr",value_avai);
                inty.putExtras(b);

                startActivity(inty);

            }
        });

        bargraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b=new Bundle();

                b.putFloatArray("key_avail", value_avai);
                b.putFloatArray("key_inst", value_inst);
                b.putCharSequenceArray("key_comp",value_comap);

                Intent inty = new Intent(NavigationActivity_backup.this, BarGraphChart.class);
                inty.putExtras(b);

                startActivity(inty);

            }
        });
        circlepie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putDoubleArray("TRIPCOUNT", tripCount);
                // b.putFloatArray("AVAILABLE_DEVICE", value_avai);
                b.putStringArray("COMPANY_NAME", compy_name);
                Intent inty = new Intent(NavigationActivity_backup.this,CirclePieChart.class);
                inty.putExtras(b);
                startActivity(inty);
            }
        });

    }
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            new UpdateAdmin().execute();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
           // Toast.makeText(NavigationActivity.this,"Refreshing",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
       // Toast.makeText(NavigationActivity.this,"stop updating",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
       // Toast.makeText(NavigationActivity.this,"stop updating",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRepeatingTask();
      //  Toast.makeText(NavigationActivity.this,"start updating",Toast.LENGTH_LONG).show();
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
/*
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent inty = new Intent(NavigationActivity.this, Demo_CircleActivity.class);
            startActivity(inty);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(NavigationActivity.this, LivestatusActivity.class);
            startActivity(intent);
            //finish();

        } else if (id == R.id.nav_slideshow) {
            Intent inty = new Intent(NavigationActivity.this, Orsac_About_us.class);
            startActivity(inty);
            //finish();

        } else if (id == R.id.nav_manage) {

            Intent inty = new Intent(NavigationActivity.this, TermsAndConditionActivity_Orsac.class);
            startActivity(inty);
          //  finish();
        } else if (id == R.id.nav_share) {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"));
            startActivity(rateIntent);
            finish();
        } else if (id == R.id.nav_send) {
            Intent inty = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(inty);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

*/
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        drawer.openDrawer(GravityCompat.START);
        mNavigationDrawerFragment.navigationItemSelection(position, NavigationActivity_backup.this);

    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
       // Admin_model adminModel;
        @Override
        public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Myadapter.ViewHolder holder, int position) {
            final Admin_model adminModel = array_admin.get(position);

            holder.item_title.setText(adminModel.getName());
            holder.num_head.setText(adminModel.getCounts());
            holder.tot_device.setText(/*"Total Devices = "+*/adminModel.getTot_device());
            holder.avai_devce.setText(/*"Available Devices = "+*/adminModel.getAvail_device());
            holder.ins_device.setText(/*"Installed Devices = " +*/ adminModel.getInstall_device());
            holder.trip_value.setText(adminModel.getTrip_count());
          /*  String url = adminModel.getImage_url();
            aQuery.id(holder.item_icon).image(url, true, true, 0, 0);
*/
            //   int id = getResources().getIdentifier(adminModel.getImage_url(), null, null);
            // int ids= getResources().getIdentifier("yourpackagename:drawable/" + "p_one", null, null);
            // aQuery.id(holder.item_icon).image(adminModel.image_url, true, true, 0, 0);
            /*holder.item_icon.setImageResource(ids);*/
            holder.item_icon.setImageResource(imags[position]);
            holder.layout_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationActivity_backup.this, Orsac_Admin_sec_Trip_count.class);

                    intent.putExtra("key_com_name",adminModel.getName());
                    intent.putExtra("key_total",adminModel.getTot_device());
                    intent.putExtra("key_trip",adminModel.getTrip_count());
                    intent.putExtra("key_avai",adminModel.getAvail_device());
                    intent.putExtra("key_insta",adminModel.getInstall_device());


                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array_admin.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView item_title;
            protected ImageView item_icon;
            protected TextView num_head;
            protected TextView tot_device;
            protected TextView avai_devce;
            protected TextView ins_device;
            protected TextView trip_value;
            protected LinearLayout layout_one;


            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                num_head = (TextView) itemLayputView.findViewById(R.id.num_head);
                item_title = (TextView) itemLayputView.findViewById(R.id.item_title);
                item_icon = (ImageView) itemLayputView.findViewById(R.id.item_icon);
                tot_device = (TextView) itemLayputView.findViewById(R.id.Total_Devices);
                avai_devce = (TextView) itemLayputView.findViewById(R.id.title_avai);
                ins_device = (TextView) itemLayputView.findViewById(R.id.title_use);
                layout_one = (LinearLayout) itemLayputView.findViewById(R.id.layout_one);
                trip_value = (TextView) itemLayputView.findViewById(R.id.trip_value);
            }
        }
    }

/*
    private void login_info() {
        String url = UrlContants.DASHBORAD;
        map.put("userid", "10001");
        ProgressDialog progressDialog = new ProgressDialog(NavigationActivity_backup.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);


        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (isConnection()) {
                    try {
                        boolean sucess = object.getBoolean("Success");
                        if (sucess) {
                            String message = object.getString("Message");
                            Intent inty = new Intent(NavigationActivity_backup.this, AdminPage.class);
                            startActivity(inty);
                            // Snackbar.make(view, message, Snackbar.LENGTH_LONG) .show();
                        } else {
                            //Snackbar.make(view, "Hello", Snackbar.LENGTH_LONG) .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
*/

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) NavigationActivity_backup.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AndyUtils.showCustomProgressDialog(NavigationActivity_backup.this,
                    "Fetching Information Please wait...", false, null);
         /*   progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*/
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;

            /*PreferenceHelper preferenceHelper = new PreferenceHelper(ActivityLogin.this);
            try{
                getDeviceToken = preferenceHelper.getGcm_id();
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                getDeviceToken = "";
            }*/

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        HashMap<String, String> map = new HashMap<>();
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));

                            tot_com.setText(jsonObj.getString("totalcompanies"));
                            tot_trip.setText(jsonObj.getString("totaltripcountcnt"));
                            tot_dev.setText(jsonObj.getString("totaldevices"));
                            veh_trac.setText(jsonObj.getString("totalinstalled"));
                            tot_device= Integer.parseInt(jsonObj.getString("totalinstalled"));
                          tot_install=Integer.parseInt(jsonObj.getString("totaldevices"));
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                           value_avai = new float[jsonArray.length()];
                            value_comap= new String[jsonArray.length()];
                            value_inst = new float[jsonArray.length()];
                            tripCount = new double[jsonArray.length()];
                            compy_name = new String[jsonArray.length()];
                       // value_inst = new Float[jsonArray.length()];
                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();
                                String posi = String.valueOf(i + 1);
                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                                admin_bean.setImage_url(imags[i]);
                                admin_bean.setTot_device(jsonObj1.getString("totalDevices"));
                                admin_bean.setAvail_device(jsonObj1.getString("availabledevices"));
                                admin_bean.setTrip_count(jsonObj1.getString("tripcount"));
                                value_avai[i] = Float.valueOf(jsonObj1.getString("availabledevices"));
                                value_inst[i] =Float.valueOf(jsonObj1.getString("installedDevices"));
                                value_comap[i] = jsonObj1.getString("companyName");

                                admin_bean.setCounts(posi);
                                companyList.add(admin_bean);
                                array_admin.add(admin_bean);
                            /*    map.put("KEY_installedDevices", jsonObj1.getString("installedDevices"));
                                map.put("KEY_companyName", jsonObj1.getString("companyName"));
                                map.put("KEY_totalDevices", jsonObj1.getString("totalDevices"));
                                map.put("KEY_availabledevices", jsonObj1.getString("availabledevices"));
                               map.put("KEY_tripcount", jsonObj1.getString("tripcount"));*/
                                installedDevices = jsonObj1.getString("installedDevices");
                                companyName = jsonObj1.getString("companyName");
                                totalDevices = jsonObj1.getString("totalDevices");
                                availabledevices = jsonObj1.getString("availabledevices");
                                tripcount = jsonObj1.getString("tripcount");
                                tripCount[i] = Double.valueOf(tripcount);
                                compy_name[i] = companyName;
                                GetCompanyDetail.add(map);
                            }
                          /*  SwipeCardAdapter swipeCardAdapter = new SwipeCardAdapter(NavigationActivity.this,array_admin,NavigationActivity.this);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(NavigationActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(swipeCardAdapter);*/

                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

/*
                        if (getStatus == true) {

                            String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");


                            //preferenceHelper.putUser_id(getUserId);


                            Toast.makeText(AdminPage.this, "" + getMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AdminPage.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminPage.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
*/
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
AndyUtils.removeCustomProgressDialog();
                   // progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */
    class UpdateAdmin extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*AndyUtils.showCustomProgressDialog(NavigationActivity.this,
                    "Fetching Information Please wait...", false, null);
        */ /*   progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*/
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;

            /*PreferenceHelper preferenceHelper = new PreferenceHelper(ActivityLogin.this);
            try{
                getDeviceToken = preferenceHelper.getGcm_id();
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                getDeviceToken = "";
            }*/

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;//strStatus;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(ActivityLogin.this,""+getResponse,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new JSONObject(getResponse);
                        HashMap<String, String> map = new HashMap<>();
                        array_admin.clear();
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));

                            tot_com.setText(jsonObj.getString("totalcompanies"));
                            tot_trip.setText(jsonObj.getString("totaltripcountcnt"));
                            tot_dev.setText(jsonObj.getString("totaldevices"));
                            veh_trac.setText(jsonObj.getString("totalinstalled"));
                            tot_device= Integer.parseInt(jsonObj.getString("totalinstalled"));
                            tot_install=Integer.parseInt(jsonObj.getString("totaldevices"));
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");
                            value_avai = new float[jsonArray.length()];
                            value_comap= new String[jsonArray.length()];
                            value_inst = new float[jsonArray.length()];
                            tripCount = new double[jsonArray.length()];
                            compy_name = new String[jsonArray.length()];
                            // value_inst = new Float[jsonArray.length()];
                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean = new Admin_model();
                                String posi = String.valueOf(i + 1);
                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                             //   admin_bean.setImage_url(jsonObj1.getString("image"));
                                admin_bean.setImage_url(imags[i]);
                                admin_bean.setTot_device(jsonObj1.getString("totalDevices"));
                                admin_bean.setAvail_device(jsonObj1.getString("availabledevices"));
                                admin_bean.setTrip_count(jsonObj1.getString("tripcount"));
                                value_avai[i] = Float.valueOf(jsonObj1.getString("availabledevices"));
                                value_inst[i] =Float.valueOf(jsonObj1.getString("installedDevices"));
                                value_comap[i] = jsonObj1.getString("companyName");

                                admin_bean.setCounts(posi);
                                companyList.add(admin_bean);
                                array_admin.add(admin_bean);
                            /*    map.put("KEY_installedDevices", jsonObj1.getString("installedDevices"));
                                map.put("KEY_companyName", jsonObj1.getString("companyName"));
                                map.put("KEY_totalDevices", jsonObj1.getString("totalDevices"));
                                map.put("KEY_availabledevices", jsonObj1.getString("availabledevices"));

                                map.put("KEY_tripcount", jsonObj1.getString("tripcount"));*/
                                installedDevices = jsonObj1.getString("installedDevices");
                                companyName = jsonObj1.getString("companyName");
                                totalDevices = jsonObj1.getString("totalDevices");
                                availabledevices = jsonObj1.getString("availabledevices");
                                tripcount = jsonObj1.getString("tripcount");
                                tripCount[i] = Double.valueOf(tripcount);
                                compy_name[i] = companyName;
                                GetCompanyDetail.add(map);
                            }

                          /*  SwipeCardAdapter swipeCardAdapter = new SwipeCardAdapter(NavigationActivity.this,array_admin,NavigationActivity.this);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(NavigationActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(swipeCardAdapter);
*/
                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);


                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

/*
                        if (getStatus == true) {

                            String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");


                            //preferenceHelper.putUser_id(getUserId);


                            Toast.makeText(AdminPage.this, "" + getMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AdminPage.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminPage.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
*/
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }

    }
}
