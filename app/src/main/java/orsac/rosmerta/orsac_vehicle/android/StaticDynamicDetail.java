package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Admin_sec_bean;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Orsac_Admin_sec_Trip_count;
import orsac.rosmerta.orsac_vehicle.android.Orsac.SimpleAdapter;
import orsac.rosmerta.orsac_vehicle.android.Orsac.StaticDynamicAdapter;
import orsac.rosmerta.orsac_vehicle.android.Orsac.StaticDynamicModel;

public class StaticDynamicDetail extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public AutoCompleteTextView search;
    private List<String> list = new ArrayList<String>();
    public StaticDynamicAdapter mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    String com_name;
    ArrayAdapter<String> adapter;
    private String[] etp_no;
    private String[] eta;
    private ArrayList<StaticDynamicModel> beanClassDashbaord;
    CardView noPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_dynamic_detail);

        Intent inty = getIntent();
        com_name = inty.getStringExtra("cpany");

        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText("Company Detail");
        search = (AutoCompleteTextView) findViewById(R.id.search);
        noPosts = (CardView) findViewById(R.id.noPosts);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        beanClassDashbaord = new ArrayList<>();

        noPosts.setVisibility(View.GONE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        hideSoftKeyboard();

        addTextListener();
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(StaticDynamicDetail.this, "Your Selected ETP No is " +
                                adapter.getItem(position).toString(),
                        Toast.LENGTH_LONG).show();
                hideSoftKeyboard();
            }
        });

        if (isConnection()){
            new getStaticDynamicCount().execute();
        }
    }


    class getStaticDynamicCount extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(StaticDynamicDetail.this, R.style.mProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isConnection()) {
                Toast.makeText(getApplicationContext(), getString(R.string.dialog_no_inter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(StaticDynamicDetail.this, "", "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... params) {

            String getResponse = null;

            if (com_name.contains(" ")) {
                com_name = com_name.replaceAll(" ", "%20");
            }
            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httppost = new HttpGet(UrlContants.STATIC_DYNAMIC_detail + com_name);
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

                if (getResponse == null) {
                    noPosts.setVisibility(View.VISIBLE);

                } else {
                    noPosts.setVisibility(View.GONE);
                    JSONArray jsonArray_ = new JSONArray(getResponse);

                    ArrayList<String> tempStore = new ArrayList<String>();
                    etp_no = new String[jsonArray_.length()];
                    for (int i = 0; i < jsonArray_.length(); i++) {

                        JSONObject jsonObj = jsonArray_.getJSONObject(i);

                        StaticDynamicModel admin_sec_bean = new StaticDynamicModel();
                        admin_sec_bean.setEta_no(jsonObj.getString("etpno"));
                        admin_sec_bean.setVehregno(jsonObj.getString("vehicleregno"));
                        etp_no[i] = jsonObj.getString("etpno");

                        beanClassDashbaord.add(admin_sec_bean);

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


                    if (beanClassDashbaord.isEmpty()) {
                        noPosts.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new StaticDynamicAdapter(beanClassDashbaord, StaticDynamicDetail.this);
                    //  mAdapter = new SimpleAdapter(list,this);
                    mRecyclerView.setAdapter(mAdapter);
                    adapter = new ArrayAdapter<String>(StaticDynamicDetail.this, android.R.layout.simple_list_item_1, eta);

                    search.setAdapter(adapter);
                }
            }catch (Exception ev){
                System.out.print(ev.getMessage());
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
        }
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

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) StaticDynamicDetail.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
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

                final List<StaticDynamicModel> filteredList = new ArrayList<>();

                for (int i = 0; i < beanClassDashbaord.size(); i++) {

                    final String text = beanClassDashbaord.get(i).getEta_no().toLowerCase();

                    if (text.contains(query)) {

                        StaticDynamicModel beanClassForRecyclerView_contactsss = new StaticDynamicModel(beanClassDashbaord.get(i).getEta_no(), beanClassDashbaord.get(i).getVehregno());

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
                mRecyclerView.setLayoutManager(new LinearLayoutManager(StaticDynamicDetail.this));
                mAdapter = new StaticDynamicAdapter(filteredList, StaticDynamicDetail.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

}
