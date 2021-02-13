package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.ItemOffsetDecoration;
import orsac.rosmerta.orsac_vehicle.android.R;

public class LivestatusActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public EditText search;
    private List<String> list = new ArrayList<String>();
    public LivestatusAdapter mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private String[] eta = {"OR09N9602", "OD05S5747", "OD09C3371", "OD29C7833", "OD05G6304", "OR09J8116"};
    private String[] vehicle_search, etp_no;

    private String[] circle = {"Atlanta", "Atlanta", "Fastrackerz", "Rosmerta Autotech Private Ltd.", "Arya Omnitalk Wireless Solution Pvt Ltd.", "Fastrackerz"};
    private ArrayList<Admin_sec_bean> beanClassDashbaord, halt_array , running_array , stop_array;
    private ArrayList<Admin_sec_bean> beanClassDashbaord_search;
    TextView tot_vehicle, halt_vehicle, run_cont, stop_con, not_work, over_speed,counter;
    FloatingActionButton next_page;
    int counte = 1 ;
    LinearLayoutManager mLayoutManager;
    int tot_trip =0, halt=0 , running_vehicle=0,stop_vehicle=0,not_working=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orsac__vehicle_status_sec__trip_count);
        tot_vehicle = (TextView) findViewById(R.id.tot_vehicle);
        halt_vehicle = (TextView) findViewById(R.id.halt_vehicle);
        stop_con = (TextView) findViewById(R.id.stop_con);
        not_work = (TextView) findViewById(R.id.not_work);
        run_cont = (TextView) findViewById(R.id.run_cont);
        counter = (TextView)findViewById(R.id.counter);
next_page = (FloatingActionButton) findViewById(R.id.nexr_page);

        hideSoftKeyboard();
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.INVISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });

        ((TextView) findViewById(R.id.titl)).setText("Live Status");
        ((TextView) findViewById(R.id.locations)).setText("Vehicle No");
        ((TextView) findViewById(R.id.type)).setText("ETP No");
        new livestatus().execute();
     /*   for (int i = 0; i < circle.length; i++) {
            list.add(circle[i]);
        }

     */
        beanClassDashbaord = new ArrayList<>();
        vehicle_search = new String[4000];
        etp_no = new String[4000];
        beanClassDashbaord_search = new ArrayList<>();
       /* for (int i = 0; i < eta.length; i++) {
            Admin_sec_bean beanClassForRecyclerView_contacts = new Admin_sec_bean(eta[i], circle[i]);

            beanClassDashbaord.add(beanClassForRecyclerView_contacts);
        }
       */
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText("Live Status");
        search = (EditText) findViewById(R.id.search);
        search.setHint("search Vehicle No...");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // mRecyclerView.setHasFixedSize(true);
        //   mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager (getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
         mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // countryList();  // in this method, Create a list of items.

        // call the adapter with argument list of items and context.
        addTextListener();
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beanClassDashbaord.isEmpty()){

                Toast.makeText(LivestatusActivity.this, "Information Loading Please Wait..",
                        Toast.LENGTH_LONG).show();
                }else {
                    counte = counte+1;

                    for (int j = 0 ; j<beanClassDashbaord.size();j++){
                        vehicle_search[j] =   beanClassDashbaord.get(j).getEta_no();
                        etp_no[j] =beanClassDashbaord.get(j).getCircle();

                    }
                    new livestatus().execute();
                }
            }
        });
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

    class livestatus extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int i = 1;
          //  AndyUtils.showCustomProgressDialog_New(LivestatusActivity.this,
            //        "Fetching Information Please wait...", true, null, 2);
            AndyUtils.showCustomProgressDialog_New(LivestatusActivity.this,
                    "Fetching Information Please wait...", false, null,2);

        }

        protected String doInBackground(String... args) {

            String jsonStr = null;
            String parameter = "http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/etpwiselivebyloginid?loginid=10001&pageno="+counte+"&itemsPerPage=20";

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
                        //halt_vehicle, run_cont, stop_con, not_work
                        JSONObject jsonObject_one = new JSONObject(getResponse);

                        tot_trip = tot_trip +Integer.valueOf( jsonObject_one.getString("work"));
                        halt = halt +Integer.valueOf( jsonObject_one.getString("hault"));
                        running_vehicle = running_vehicle +Integer.valueOf( jsonObject_one.getString("run"));
                        stop_vehicle = stop_vehicle +Integer.valueOf( jsonObject_one.getString("stop"));
                        not_working = not_working +Integer.valueOf( jsonObject_one.getString("nowork"));

                        int tot  = halt+running_vehicle+stop_vehicle+not_working;
                        tot_vehicle.setText(tot+"");
                        halt_vehicle.setText(halt+"");
                        run_cont.setText(running_vehicle+"");
                        stop_con.setText(stop_vehicle+"");
                        not_work.setText(not_working+"");
                        counter.setText(counte+"");

                        JSONArray jsonArray = jsonObject_one.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int json_array = jsonArray.length();
                            Admin_sec_bean admin_sec_bean = new Admin_sec_bean();
                            admin_sec_bean.setCircle(jsonObject.getString("etpno"));
                            admin_sec_bean.setEta_no(jsonObject.getString("vehicleno"));
                            if(counte == 1) {
                                vehicle_search[i] = jsonObject.getString("vehicleno");
                                etp_no[i] = jsonObject.getString("etpno");
                            }

                            beanClassDashbaord.add(admin_sec_bean);


                        }

                        mAdapter = new LivestatusAdapter(beanClassDashbaord, LivestatusActivity.this);
                        //  mAdapter = new SimpleAdapter(list,this);
                        mRecyclerView.setAdapter(mAdapter);

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                }

            });
        }

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
                    if (text.contains(query)) {
                        String cir_element = vehicle_search[i];
                        Admin_sec_bean admin_sec_bean = new Admin_sec_bean();

                        beanClassDashbaord.get(i).getEta_no();
                        Admin_sec_bean beanClassForRecyclerView_contactsss = new Admin_sec_bean(vehicle_search[i], etp_no[i]);

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

                mRecyclerView.setLayoutManager(new LinearLayoutManager(LivestatusActivity.this));
                mAdapter = new LivestatusAdapter(filteredList, LivestatusActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    /*  @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
          switch (id) {
              case android.R.id.home:
                  //onBackPressed();
                  Intent inty = new Intent(LivestatusActivity.this,NavigationActivity.class);
                  startActivity(inty);
                //  finish();

                  break;
          }
          return super.onOptionsItemSelected(item);
      }*/
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
}