package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.ItemOffsetDecoration;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.StaticDynamicDetail;
import orsac.rosmerta.orsac_vehicle.android.UrlContants;

public class Orsac_Admin_sec_Trip_count extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public AutoCompleteTextView search;
    private List<String> list = new ArrayList<String>();
    public SimpleAdapter mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private String[] eta;// = {"L31702150/4", "L21702681/92 ", "L21702681/243", "I31707917/2", "L31701295/89", "L31701249/288"};
    private String[] circle = {"joda", "KOIRA", "KOIRA", "SAMBALPUR", "KOIRA", "JODA"};
    private ArrayList<Admin_sec_bean> beanClassDashbaord;
    private ArrayList<Admin_sec_bean> beanClassDashbaord_search;
    String com_name, insta, avail, trip, total_active ,total_inactive;
    TextView tot_com, tot_trip, tot_dev, veh_trac, com_title;

    Map map;
    CardView noPosts;
    private String[] vehicle_search, etp_no;
    ArrayAdapter<String> adapter;
    String[] countries;
    int active_count =0 , inactive_count =0 ,tot_trip_count = 0;
    PreferenceHelper preferenceHelper;
    LinearLayout lyn_etp_active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orsac__admin_sec__trip_count);

        map = new HashMap();
        countries = getResources().getStringArray(R.array.countries_array);
        preferenceHelper = new PreferenceHelper(this);


        Intent inty = getIntent();
        com_name = inty.getStringExtra("key_com_name");
        total_active = inty.getStringExtra("key_active");
        total_inactive = inty.getStringExtra("key_inactive");
        insta = inty.getStringExtra("key_insta");
        trip = inty.getStringExtra("key_trip");
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        com_title = (TextView) findViewById(R.id.com_title);
        tot_com = (TextView) findViewById(R.id.tot_com);
        tot_trip = (TextView) findViewById(R.id.tot_trip);
        tot_dev = (TextView) findViewById(R.id.tot_dev);
        veh_trac = (TextView) findViewById(R.id.veh_trac);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        noPosts = (CardView) findViewById(R.id.noPosts);
        lyn_etp_active = (LinearLayout) findViewById(R.id.lyn_etp_active);

        noPosts.setVisibility(View.GONE);
        tot_com.setText(insta);
        tot_trip.setText(total_inactive);
        tot_dev.setText(total_active);
        veh_trac.setText(trip);
        com_title.setText(com_name);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        //Trip_cont_info();
        hideSoftKeyboard();
        new Authentication().execute();
        for (int i = 0; i < circle.length; i++) {
            list.add(circle[i]);
        }

        beanClassDashbaord = new ArrayList<>();
        beanClassDashbaord_search = new ArrayList<>();
        /*for (int i = 0; i < eta.length; i++) {
            Admin_sec_bean beanClassForRecyclerView_contacts = new Admin_sec_bean(eta[i], circle[i]);

            beanClassDashbaord.add(beanClassForRecyclerView_contacts);
        }*/
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText("Company Detail");
        search = (AutoCompleteTextView) findViewById(R.id.search);


        mRecyclerView = ( RecyclerView ) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager (getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        // countryList();  // in this method, Create a list of items.

        // call the adapter with argument list of items and context.

        addTextListener();

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Orsac_Admin_sec_Trip_count.this, "Your Selected ETP No is " +
                                adapter.getItem(position).toString(),
                        Toast.LENGTH_LONG).show();
                hideSoftKeyboard();
            }
        });

        lyn_etp_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnection()){
                    new getStaticDynamicCount().execute();
                }
            }
        });
    }


    class getStaticDynamicCount extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(Orsac_Admin_sec_Trip_count.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(Orsac_Admin_sec_Trip_count.this, "", "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... params) {

            String getResponse = null;


            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(UrlContants.STATIC_DYNAMIC);
                HttpResponse httpResponse = httpClient.execute(httppost);
                getResponse = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception ev) {
                ev.getStackTrace();
            }

            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {
            try{
                JSONArray jsonArray_   =  new JSONArray(getResponse);
                String company = com_title.getText().toString();
                 for (int i = 0; i < jsonArray_.length(); i++) {

                JSONObject jsonObj = jsonArray_.getJSONObject(i);

                if (company.equalsIgnoreCase(jsonObj.getString("companyname"))) {
                    String dynamic = jsonObj.getString("sum");
                    String etp_active = tot_dev.getText().toString();
                    getDynamic(dynamic, active_count, etp_active);

                }/*else{
                    Toast.makeText(Orsac_Admin_sec_Trip_count.this, "Sorry no record found", Toast.LENGTH_LONG).show();
                }*/

                }
            }catch (Exception ev){
                System.out.print(ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }
    public void getDynamic(String dynamic, int active, String etp_active){
        final Dialog myDialog = new Dialog(Orsac_Admin_sec_Trip_count.this);
        myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialogbox1);

        TextView txt_title               = (TextView) myDialog.findViewById(R.id.txt_title);
        TextView txt_dash_totalvehicle       = (TextView)myDialog.findViewById(R.id.txt_dash_totalvehicle);
        TextView  txt_dash_idlevehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_idlevehicle);
        TextView  txt_dash_runnvehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_runnvehicle);
        TextView txt_dash_stopvehicle        = (TextView)myDialog.findViewById(R.id.txt_dash_stopvehicle);
        TextView txt_dash_nonpollvehicle     = (TextView)myDialog.findViewById(R.id.txt_dash_nonpollvehicle);
        TextView txtRefreshTimestamp         = (TextView)myDialog.findViewById(R.id.txtRefreshTimestamp);
        LinearLayout total_veh                = (LinearLayout)myDialog.findViewById(R.id.total_veh);
        LinearLayout running_veh              = (LinearLayout)myDialog.findViewById(R.id.running_veh);
        LinearLayout stop_veh                   = (LinearLayout)myDialog.findViewById(R.id.stop_veh);
        LinearLayout halt_veh                   = (LinearLayout)myDialog.findViewById(R.id.halt_veh);
        LinearLayout nonpolling_veh             = (LinearLayout)myDialog.findViewById(R.id.nonpolling_veh);
        TextView ok_close                  = (TextView)myDialog.findViewById(R.id.ok_close);
        TextView btn_detail                 = (TextView) myDialog.findViewById(R.id.btn_detail);

        txt_dash_totalvehicle.setText(etp_active);
        txt_title.setText("ETP/Active");
        if(Integer.parseInt ( dynamic )< 1){
            txt_dash_stopvehicle.setText("---");

        }else{
            txt_dash_stopvehicle.setText(dynamic);

        }
        int v_static =  active - Integer.parseInt(dynamic);

        if(v_static< 1){
            txt_dash_runnvehicle.setText("---");

        }else{
            txt_dash_runnvehicle.setText(""+v_static);

        }


        ok_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                Intent i = new Intent(Orsac_Admin_sec_Trip_count.this, StaticDynamicDetail.class);
                i.putExtra("cpany", com_name);
                startActivity(i);
            }
        });

        /*
        total_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("All");
            }
        });
        running_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("running");
            }
        });
        halt_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("idle");
            }
        });
        stop_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("stop");
            }
        });
        nonpolling_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prettyDialog("nonpolling");
            }
        });*/

        /*txt_dash_totalvehicle.setText(totalVehicles);
        txt_dash_idlevehicle.setText(nonpollingVehicles);
        txt_dash_runnvehicle.setText(runningVehicles);
        txt_dash_stopvehicle.setText(stopVehicles);
        txt_dash_nonpollvehicle.setText(temperVehicles);*/


       /* Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        txtRefreshTimestamp.setText(formattedDate);*/
        myDialog.show();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*AndyUtils.showCustomProgressDialog_New(Orsac_Admin_sec_Trip_count.this,
                    "Fetching Information Please wait...", false, null,2);
*/
            try {
                AndyUtils.showCustomProgressDialog(Orsac_Admin_sec_Trip_count.this,
                        "Fetching Information Please wait...", true, null);

            } catch (Exception ev) {
                ev.getMessage();
            }
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
            nameValuePairs.add(new BasicNameValuePair("username", user_names));
            nameValuePairs.add(new BasicNameValuePair("password", passwords));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));
            if (com_name.contains(" ")) {
                com_name = com_name.replaceAll(" ", "%20");
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+"companywiseetp?companyname=" + com_name);

                // Add your data
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                        if (isConnection()) {

                            try {

                                if (getResponse == null) {
                                    noPosts.setVisibility(View.VISIBLE);

                                } else {
                                    noPosts.setVisibility(View.GONE);
                                    JSONArray jsonArray = new JSONArray(getResponse);

                                    ArrayList<String> tempStore = new ArrayList<String>();


                                    //  JSONArray jsonArray = getResponse.getJSONArray("");
                                    etp_no = new String[jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Admin_sec_bean admin_sec_bean = new Admin_sec_bean();
                                        admin_sec_bean.setCircle(jsonObject.getString("circle"));
                                        admin_sec_bean.setEta_no(jsonObject.getString("etpno"));
                                        admin_sec_bean.setVehregno(jsonObject.getString("vehregno"));
                                        admin_sec_bean.setPolling_status(jsonObject.getString("vehiclestatus"));
                                        if(jsonObject.getString("vehiclestatus").equalsIgnoreCase("OK")){
                                            admin_sec_bean.setPolling_status("Valid");
                                            active_count = active_count+1;
                                        }else{
                                            admin_sec_bean.setPolling_status("Invalid");
                                            inactive_count = inactive_count+1;
                                        }

                                        etp_no[i] = jsonObject.getString("etpno");
                                        //circlrArr[i] = jsonObject.getString("circle");
                                        if (!tempStore.contains(jsonObject.getString("circle"))) {
                                            tempStore.add(jsonObject.getString("circle"));
                                        }

                                        //eta[i]=jsonObject.getString("etpno");
                                        beanClassDashbaord.add(admin_sec_bean);


                                    }
                                    //eta=etp_no;
                                    String[] circlrArr = new String[tempStore.size()];
                                    for (int aind = 0; aind < tempStore.size(); aind++) {
                                        circlrArr[aind] = tempStore.get(aind);
                                    }

                                    int aLen = etp_no.length;
                                    int bLen = circlrArr.length;
                                    String[] C = new String[aLen + bLen];
                                    System.arraycopy(etp_no, 0, C, 0, aLen);
                                    System.arraycopy(circlrArr, 0, C, aLen, bLen);
                                    eta = C;

                                    String temp = C.toString();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (beanClassDashbaord.isEmpty()) {
                                noPosts.setVisibility(View.VISIBLE);
                            }

                            mAdapter = new SimpleAdapter(beanClassDashbaord, Orsac_Admin_sec_Trip_count.this);
                            //  mAdapter = new SimpleAdapter(list,this);
                            mRecyclerView.setAdapter(mAdapter);
                            adapter = new
                                    ArrayAdapter<String>(Orsac_Admin_sec_Trip_count.this, android.R.layout.simple_list_item_1, eta);

                            search.setAdapter(adapter);

                            tot_dev.setText(active_count+"/"+total_active);
                            tot_trip.setText(inactive_count+"/"+total_inactive);
                            tot_trip_count = active_count + inactive_count;
                            veh_trac.setText(tot_trip_count+"");


                        } else {
                            Toast.makeText(Orsac_Admin_sec_Trip_count.this, "No Internet ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    //search.setThreshold(1);

                    //  progressDialog.dismiss();
                    AndyUtils.removeCustomProgressDialog();
                }
            });
        }

    }
    /*Testing Asyn same work like Login_info method */

    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {

    }

/*
    private void Trip_cont_info() {
        String url = UrlContants.TRIP_CONT_ETP + com_name;
        String url_new = "http://209.190.15.26/orsacweb/rest/CallService/companywiseetp?companyname=Atlanta";
        AndyUtils.showCustomProgressDialog(Orsac_Admin_sec_Trip_count.this, "Loading....", false, null);
        ProgressDialog progressDialog = new ProgressDialog(Orsac_Admin_sec_Trip_count.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);

        aQuery.progress(progressDialog).ajax(url_new, null, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (isConnection()) {
                    try {
                        if (object.has("success")) ;
                        {
                            String error_msg = object.getString("error");
                            //                        Toast.makeText(Orsac_Admin_sec_Trip_count.this, error_msg, Toast.LENGTH_LONG).show();
                            noPosts.setVisibility(View.VISIBLE);
                            AndyUtils.removeCustomProgressDialog();
                        }
                        JSONArray jsonArray = object.getJSONArray("");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Admin_sec_bean admin_sec_bean = new Admin_sec_bean();
                            admin_sec_bean.setCircle(jsonObject.getString("circle"));
                            admin_sec_bean.setEta_no(jsonObject.getString("etpno"));
                            beanClassDashbaord.add(admin_sec_bean);

                        }
                        mRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        mRecyclerView.setLayoutManager(layoutManager);
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                        mRecyclerView.addItemDecoration(itemDecoration);

                        mAdapter = new SimpleAdapter(beanClassDashbaord, Orsac_Admin_sec_Trip_count.this);
                        //  mAdapter = new SimpleAdapter(list,this);
                        mRecyclerView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndyUtils.removeCustomProgressDialog();
                } else {
                    Toast.makeText(Orsac_Admin_sec_Trip_count.this, "No Internet ", Toast.LENGTH_LONG).show();
                }

            }
        });
        AndyUtils.removeCustomProgressDialog();

    }
*/

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Orsac_Admin_sec_Trip_count.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

    // this method is used to create list of items.
    public void countryList() {

        list.add("Afghanistan");
        list.add("Albania");
        list.add("Algeria");
        list.add("Bangladesh");
        list.add("Belarus");
        list.add("Canada");
        list.add("Cape Verde");
        list.add("Central African Republic");
        list.add("Denmark");
        list.add("Dominican Republic");
        list.add("Egypt");
        list.add("France");
        list.add("Germany");
        list.add("Hong Kong");
        list.add("India");
        list.add("Iceland");
    }

    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<Admin_sec_bean> filteredList = new ArrayList<>();

                for (int i = 0; i < beanClassDashbaord.size(); i++) {

                    final String text = beanClassDashbaord.get(i).getEta_no().toLowerCase();
                    final String text_circle = beanClassDashbaord.get(i).getVehregno().toLowerCase();
                    if (text.contains(query) || text_circle.contains(query)) {
                        Admin_sec_bean beanClassForRecyclerView_contactsss = new Admin_sec_bean(beanClassDashbaord.get(i).getEta_no(), beanClassDashbaord.get(i).getVehregno(), beanClassDashbaord.get(i).getPolling_status());

                        filteredList.add(beanClassForRecyclerView_contactsss);
                        // filteredList.add(list.get(i));
                        /*for (int j = 0; i <= circle.length; j++) {
                            String cir_element = circle[j];
                            if (cir_element.equalsIgnoreCase(text)) {
                                Admin_sec_bean beanClassForRecyclerView_contacts = new Admin_sec_bean(eta[j], circle[j]);
                                beanClassDashbaord_search.add(beanClassForRecyclerView_contacts);
                            }
                        }*/
                    }
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager (Orsac_Admin_sec_Trip_count.this));
                mAdapter = new SimpleAdapter(filteredList, Orsac_Admin_sec_Trip_count.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}