package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.UrlContants;

public class Orsac_circle_detail extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public AutoCompleteTextView search;
    private List<String> list = new ArrayList<String>();
    public SimpleAdapter_Sec mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private String[] eta; /*= {"L31702150/4", "L21702681/92 ", "L21702681/243", "I31707917/2", "L31701295/89", "L31701249/288"}*/
    private String[] circle = {"joda", "KOIRA", "KOIRA", "SAMBALPUR", "KOIRA", "JODA"};
    private ArrayList<Admin_sec_bean> beanClassDashbaord;
    private ArrayList<Admin_sec_bean> beanClassDashbaord_search;
    String com_name, insta, avail, trip, total;
    TextView tot_com, tot_trip, tot_dev, veh_trac, com_title;
    Map map;
    private String[] vehicle_search ,etp_no;

    CardView noPosts;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orsac_circle_etp_no);

        map = new HashMap();

        Intent inty = getIntent();
        com_name = inty.getStringExtra("circle");
        total = inty.getStringExtra("key_total");
        trip = inty.getStringExtra("key_trip");
        insta = inty.getStringExtra("key_insta");
        avail = inty.getStringExtra("key_avai");
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        com_title = (TextView) findViewById(R.id.com_title);
        tot_com = (TextView) findViewById(R.id.tot_com);
        tot_trip = (TextView) findViewById(R.id.tot_trip);
        tot_dev = (TextView) findViewById(R.id.tot_dev);
        veh_trac = (TextView) findViewById(R.id.veh_trac);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        search = (AutoCompleteTextView ) findViewById(R.id.search);
        noPosts=(CardView)findViewById(R.id.noPosts);
        noPosts.setVisibility(View.GONE);
        tot_com.setText(total);
        tot_trip.setText(insta);
        tot_dev.setText(avail);
        veh_trac.setText(trip);
        com_title.setText(com_name);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(Orsac_circle_detail.this, ViewPagerActivity.class);
                startActivity(inty);
                finish();
            }
        });
        //Trip_cont_info();
        new  Authentication().execute();
        for (int i = 0; i < circle.length; i++) {
            list.add(circle[i]);
        }
        hideSoftKeyboard();
        beanClassDashbaord = new ArrayList<>();
        beanClassDashbaord_search = new ArrayList<>();
        /*for (int i = 0; i < eta.length; i++) {
            Admin_sec_bean beanClassForRecyclerView_contacts = new Admin_sec_bean(eta[i], circle[i]);

            beanClassDashbaord.add(beanClassForRecyclerView_contacts);
        }*/
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText("Circle Detail");
        //search = (EditText) findViewById(R.id.search);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Orsac_circle_detail.this,"Your Selected ETP No is "+
                                adapter.getItem(position).toString(),
                        Toast.LENGTH_LONG).show();
                hideSoftKeyboard();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        // countryList();  // in this method, Create a list of items.

        // call the adapter with argument list of items and context.

        addTextListener();
        hideSoftKeyboard();
    }
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog_New(Orsac_circle_detail.this,
                    "Fetching Information Please wait...", false, null,2);

          /*  progressDialog.setMessage("Loading....");
            progressDialog.show();
          */  // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
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

            try {
                HttpClient httpclient = new DefaultHttpClient();
                // http://209.190.15.26/orsacweb/rest/CallService/circlewiseetp?circle=JODA
                HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/circlewiseetp?circle="+com_name);

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
               if(getResponse==null){
                   noPosts.setVisibility(View.VISIBLE);

               }else {
                   JSONArray jsonArray = new JSONArray(getResponse);
                   ArrayList<String> tempStore = new ArrayList<String>();

                   etp_no = new String[jsonArray.length()];
                   //  JSONArray jsonArray = getResponse.getJSONArray("");
                   for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject jsonObject = jsonArray.getJSONObject(i);
                       Admin_sec_bean admin_sec_bean = new Admin_sec_bean();
                       admin_sec_bean.setCircle(jsonObject.getString("circle"));
                       admin_sec_bean.setEta_no(jsonObject.getString("etpno"));
                       etp_no[i] = jsonObject.getString("etpno");
                       //circlrArr[i] = jsonObject.getString("circle");
                       if (!tempStore.contains(jsonObject.getString("circle"))) {
                           tempStore.add(jsonObject.getString("circle"));
                       }
                       beanClassDashbaord.add(admin_sec_bean);
                       mAdapter = new SimpleAdapter_Sec(beanClassDashbaord, Orsac_circle_detail.this);
                       //  mAdapter = new SimpleAdapter(list,this);
                       mRecyclerView.setAdapter(mAdapter);

                   }
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

               }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(Orsac_circle_detail.this, "No Internet ", Toast.LENGTH_LONG).show();
                        }
                        adapter = new
                                ArrayAdapter<String>(Orsac_circle_detail.this, android.R.layout.simple_list_item_1, eta);

                        search.setAdapter(adapter);
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                    AndyUtils.removeCustomProgressDialog();
                  //  progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */

/*
    private void Trip_cont_info() {
        String url = UrlContants.TRIP_CONT_ETP + com_name;
String url_new="http://209.190.15.26/orsacweb/rest/CallService/companywiseetp?companyname=Atlanta";
        AndyUtils.showCustomProgressDialog(Orsac_circle_detail.this, "Loading....", false, null);
        ProgressDialog progressDialog = new ProgressDialog(Orsac_circle_detail.this);
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
                            mAdapter = new SimpleAdapter_Sec(beanClassDashbaord, Orsac_circle_detail.this);
                            //  mAdapter = new SimpleAdapter(list,this);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndyUtils.removeCustomProgressDialog();
                } else {
                    Toast.makeText(Orsac_circle_detail.this, "No Internet ", Toast.LENGTH_LONG).show();
                }

            }
        });
        AndyUtils.removeCustomProgressDialog();

    }
*/

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Orsac_circle_detail.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    final String text_circle = beanClassDashbaord.get(i).getCircle().toLowerCase();
                    if (text.contains(query)|| text_circle.contains(query) ) {
                        Admin_sec_bean beanClassForRecyclerView_contactsss = new Admin_sec_bean(beanClassDashbaord.get(i).getEta_no(),beanClassDashbaord.get(i).getCircle());

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

                mRecyclerView.setLayoutManager(new LinearLayoutManager(Orsac_circle_detail.this));
                mAdapter = new SimpleAdapter_Sec(filteredList, Orsac_circle_detail.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}