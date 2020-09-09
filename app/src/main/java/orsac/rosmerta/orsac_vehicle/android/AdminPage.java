package orsac.rosmerta.orsac_vehicle.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orsac.rosmerta.orsac_vehicle.android.Orsac.AllMines;
import orsac.rosmerta.orsac_vehicle.android.Orsac.BarGraphChart;
import orsac.rosmerta.orsac_vehicle.android.Orsac.CirclePieChart;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Orsac_Admin_sec_Trip_count;
import orsac.rosmerta.orsac_vehicle.android.Orsac.PieNewChart;
import orsac.rosmerta.orsac_vehicle.android.Orsac.StackedbarChart;

/**
 * Created by Diwash Choudhary on 1/21/2017.
 */
public class AdminPage extends Activity {
    private RecyclerView recyclerView;
    private List<Admin_model> array_admin;
    Admin_model admin_model;
    private Myadapter mAdapter;

    Map map;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private TextView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;
    String imageUri, imageUri_one, imageUri_two, imageUri_three, imageUri_four;
    int[] imags = {R.drawable.fasttra_two, R.drawable.rose_six, R.drawable.atlanata_three, R.drawable.arya_three, R.drawable.trimmble_three, R.drawable.etrans_two};
    static int i = 0;
    ArrayList<HashMap<String, String>> GetCompanyDetail;
    private GridLayoutManager lLayout;
    private ArrayList<Admin_model> companyList;
    String totalcompanies, totaltripcountcnt, totaldevices, totalinstalled, installedDevices, companyName, totalDevices, availabledevices, tripcount;
TextView tot_com,tot_trip,tot_dev,veh_trac;
    LinearLayout piechart,stackedbar,bargraph,circlepie,all_mine;
    FrameLayout navi_frame;
    Handler mHandler = new Handler();
TextView tot_vs_active, T_ETP_vs_T_Active;
    private final static int INTERVAL = 1000 * 10  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_recycler);
        tot_com=(TextView)findViewById(R.id.tot_com);
        tot_trip=(TextView)findViewById(R.id.tot_trip);
        tot_dev=(TextView)findViewById(R.id.tot_dev);
        veh_trac=(TextView)findViewById(R.id.veh_trac);
        piechart=(LinearLayout)findViewById(R.id.piechart);
        stackedbar = (LinearLayout)findViewById(R.id.stackedbar);
        all_mine= (LinearLayout)findViewById(R.id.all_mine);
        bargraph = (LinearLayout)findViewById(R.id.bargraph);
        circlepie = (LinearLayout)findViewById(R.id.circlepie);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        tot_vs_active =(TextView)findViewById(R.id.tot_vs_active);
        T_ETP_vs_T_Active = (TextView)findViewById(R.id.T_ETP_vs_T_Active) ;

        map = new HashMap();
        companyList= new ArrayList<>();
        //login_info();
        new Authentication().execute();
        startRepeatingTask();
        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        // viewPager.addOnPageChangeListener(viewPagerPageChangeListener);




        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        array_admin = new ArrayList<>();
       /* admin_model = new Admin_model("Rosemerta Autotech Private Ltd.", imageUri, "01", "2564", "2369", "195");
        array_admin.add(admin_model);
        admin_model = new Admin_model("Fasttrackerz", imageUri_one, "02", "999", "804", "195");
        array_admin.add(admin_model);
        admin_model = new Admin_model("Atlanata", imageUri_two, "03", "391", "345", "46");
        array_admin.add(admin_model);
        admin_model = new Admin_model("Arya Omnitalk Wireless Solution Pvt Ltd.", imageUri_three, "04", "0", "0", "0");
        array_admin.add(admin_model);
        admin_model = new Admin_model("Trimble Mobility Solution Pvt Ltd.", imageUri_four, "05", "0", "0", "0");
        array_admin.add(admin_model);
        admin_model = new Admin_model("eTrans Solutions Pvt Ltd.", imageUri_four, "06", "120", "140", "14");
        array_admin.add(admin_model);*/



      /*  recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(15)
                .build());
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .size(15)
                .build());*/
        //mAdapter.notifyDataSetChanged();
        //recyclerView.refreshDrawableState();
        piechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(AdminPage.this, PieNewChart.class);
                startActivity(inty);
            }
        });

        stackedbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(AdminPage.this,StackedbarChart.class);
                startActivity(inty);
            }
        });
        all_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(AdminPage.this,AllMines.class);
                startActivity(inty);
                Toast.makeText(AdminPage.this,"Refreshing",Toast.LENGTH_LONG).show();
            }
        });

        bargraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(AdminPage.this,BarGraphChart.class);
                startActivity(inty);
            }
        });
        circlepie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(AdminPage.this,CirclePieChart.class);
                startActivity(inty);
            }
        });
    }
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            new Authentication().execute();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
            Toast.makeText(AdminPage.this,"Refreshing",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        Toast.makeText(AdminPage.this,"stop updating",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
        Toast.makeText(AdminPage.this,"stop updating",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRepeatingTask();
        Toast.makeText(AdminPage.this,"start updating",Toast.LENGTH_LONG).show();
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

    /*private  void  somefunction()
      {
          handle.post(ViewPagerVisibleScroll);
      }

      Runnable ViewPagerVisibleScroll= new Runnable() {
          @Override
          public void run() {
              if(i <= mAdapter.getItemCount()-1)
              {
                  viewPager.setCurrentItem(i, true);
                  handle.postDelayed(TopChartAnimation, 100);
                  i++;
              }
          }
      };*/
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
        @Override
        public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Myadapter.ViewHolder holder, int position) {
            Admin_model adminModel = array_admin.get(position);

            holder.item_title.setText(adminModel.getName());
            holder.num_head.setText(adminModel.getCounts());
            holder.tot_device.setText(/*"Total Devices = "+*/adminModel.getTot_device());
            holder.avai_devce.setText(/*"Available Devices = "+*/adminModel.getAvail_device());
            holder.ins_device.setText(/*"Installed Devices = " +*/ adminModel.getInstall_device());
            holder.trip_value.setText(adminModel.getTrip_count());
            //   int id = getResources().getIdentifier(adminModel.getImaaQueryge_url(), null, null);
            // int ids= getResources().getIdentifier("yourpackagename:drawable/" + "p_one", null, null);
            // .id(holder.item_icon).image(adminModel.image_url, true, true, 0, 0);
            /*holder.item_icon.setImageResource(ids);*/
            holder.item_icon.setImageResource(imags[position]);
            holder.layout_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminPage.this, Orsac_Admin_sec_Trip_count.class);
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
        ProgressDialog progressDialog = new ProgressDialog(AdminPage.this);
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
                            Intent inty = new Intent(AdminPage.this, AdminPage.class);
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
        ConnectivityManager manage = (ConnectivityManager) AdminPage.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

            progressDialog = new ProgressDialog(AdminPage.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
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
                            JSONArray jsonArray = jsonObj.getJSONArray("companydetails");

                            GetCompanyDetail = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                Admin_model admin_bean= new Admin_model();
                                String posi= String.valueOf(i+1);
                                admin_bean.setInstall_device(jsonObj1.getString("installedDevices"));
                                admin_bean.setName(jsonObj1.getString("companyName"));
                                admin_bean.setTot_device(jsonObj1.getString("totalDevices"));
                                admin_bean.setAvail_device(jsonObj1.getString("availabledevices"));
                                admin_bean.setTrip_count(jsonObj1.getString("tripcount"));
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

                                GetCompanyDetail.add(map);
                            }
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

                    progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */

}
