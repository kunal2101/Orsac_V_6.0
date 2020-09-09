package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.ItemOffsetDecoration;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.UrlContants;

public class CircleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private Myadapter mAdapter;
    private static List<CircelBean> array_admin;
    CircelBean etaBean;
    private Toolbar tb;
    PreferenceHelper preferenceHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
String[] circlename ={"JODA","KOIRA","BALANGIR","BARIPADA","BERHAMPUR","BARGARH" ,"KALAHANDI","CUTTACK","JAJPUR ROAD",
              "JHARSUGUDA","KEONJHAR","KORAPUT& RAYAGADA","ROURKELA","SAMBALPUR","TALCHER"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orsac_circle_recy);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_title.setText("Circle DashBoard");
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        preferenceHelper = new PreferenceHelper(this);

        //login_info();
       /* tool_title=(MyTextView)findViewById(R.id.tool_title);
        tool_title.setVisibility(View.GONE);
        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.GONE);
        tb = (Toolbar) findViewById(R.id.toolBar);
        tb.setTitleTextColor(Color.WHITE);
        tb.setTitle("Circle DashBoard");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/
        array_admin = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
/*
        mAdapter = new Myadapter();
        recyclerView.setAdapter(mAdapter);
*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if(isConnection()){
            new Authentication().execute();
        }else {
            Toast.makeText(CircleActivity.this,"No Internet....",Toast.LENGTH_LONG).show();
        }
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }


    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_circle, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CircelBean adminModel = array_admin.get(position);

            holder.name.setText(adminModel.getTitle());
            holder.circle_progre.setProgress(Integer.parseInt(adminModel.getNum()));

            holder.card_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int etpno_cont= Integer.parseInt(adminModel.getNum());
                    if(etpno_cont>0) {
                        Intent intent = new Intent(CircleActivity.this, Orsac_circle_detail.class);
                        intent.putExtra("circle", adminModel.getTitle());
                        startActivity(intent);
                    }else {
                        Toast.makeText(CircleActivity.this,"No Record available",Toast.LENGTH_LONG).show();
                    }
                                    }
            });
        }

        @Override
        public int getItemCount() {
            return array_admin.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected MyTextView num;
            protected TextView name;
            protected ArcProgress circle_progre;
            protected CardView card_header;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                circle_progre = (ArcProgress) itemLayputView.findViewById(R.id.circle_progre);
                name = (TextView) itemLayputView.findViewById(R.id.name_com);
                card_header = (CardView) itemLayputView.findViewById(R.id.card_header);
            }
        }

    }

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home:
                    //onBackPressed();
                    Intent inty = new Intent(CircleActivity.this,NavigationActivity.class);
                    startActivity(inty);
                    finish();

                    break;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) CircleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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


          //  AndyUtils.showCustomProgressDialog(CircleActivity.this, "Authenticating your account..", false, null);
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
            nameValuePairs.add(new BasicNameValuePair("u", user_names));
            nameValuePairs.add(new BasicNameValuePair("p", passwords));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                // HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/omvtslogin?u="+user_names+"&p="+passwords);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls()+"/circlewiseTrip");

                // Add your data
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                        //JSONObject jsonObj = new JSONObject(getResponse);

                      //  JSONArray jsonArray = jsonObj.getJSONArray("");

                        JSONArray jsonArray = new JSONArray(getResponse);
                        array_admin = new ArrayList<>();
                        for (int aind = 0; aind < jsonArray.length(); aind++) {
                            //JSONArray jsonArray1 = jsonArray.getJSONArray(aind);
                            JSONObject jsonObject = jsonArray.getJSONObject(aind);
                            //String temp = jsonArray1.join("~");
                           // String getTitle = jsonArray1.getString(0).toString();
                            //String getValue = jsonArray1.getString(1).toString();
                            //String message = object.getString("Message");
                            CircelBean admin_bean = new CircelBean();
                            admin_bean.setTitle(jsonObject.getString("circleName"));
                            admin_bean.setNum(jsonObject.getString("tripcount"));
                            array_admin.add(admin_bean);
                        }
                      //  for(int j= array_admin.size();j<15;j++){
                        //    etaBean = new CircelBean(circlename[array_admin.size()], "0");
                         //   array_admin.add(etaBean);
                        //}

                      /*  etaBean = new CircelBean("JODA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KOIRA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("BALANGIR", "0");
                        array_admin.add(etaBean);

                        etaBean = new CircelBean("BARIPADA", "0");
                        array_admin.add(etaBean);

                        etaBean = new CircelBean("BERHAMPUR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("BARGARH", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KALAHANDI", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("CUTTACK", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("JAJPUR ROAD", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("JHARSUGUDA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KEONJHAR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KORAPUT& RAYAGADA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("ROURKELA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("SAMBALPUR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("TALCHER", "0");
                        array_admin.add(etaBean);*/


                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                        recyclerView.addItemDecoration(itemDecoration);
                        mAdapter = new Myadapter();
                        recyclerView.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   //progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */


/*
    private void login_info() {
        String url = UrlContants.CIRCLE_DETAIL;

        ProgressDialog progressDialog = new ProgressDialog(CircleActivity.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);


        aQuery.progress(progressDialog).ajax(url, null, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (isConnection()) {
                    try {
                        JSONArray jsonArray = object.getJSONArray("CirclewiseTrip");

                        array_admin = new ArrayList<>();
                        for (int aind = 0; aind < jsonArray.length(); aind++) {
                            JSONArray jsonArray1 = jsonArray.getJSONArray(aind);
                            //String temp = jsonArray1.join("~");
                            String getTitle = jsonArray1.getString(0).toString();
                            String getValue = jsonArray1.getString(1).toString();
                            //String message = object.getString("Message");
                            CircelBean admin_bean = new CircelBean();
                            admin_bean.setTitle(getTitle);
                            admin_bean.setNum(getValue);
                            array_admin.add(admin_bean);
                        }
 for(int j= array_admin.size();j<15;j++){
                            etaBean = new CircelBean(circlename[array_admin.size()], "0");
                            array_admin.add(etaBean);
                        }

etaBean = new CircelBean("JODA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KOIRA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("BALANGIR", "0");
                        array_admin.add(etaBean);

                        etaBean = new CircelBean("BARIPADA", "0");
                        array_admin.add(etaBean);

                        etaBean = new CircelBean("BERHAMPUR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("BARGARH", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KALAHANDI", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("CUTTACK", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("JAJPUR ROAD", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("JHARSUGUDA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KEONJHAR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("KORAPUT& RAYAGADA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("ROURKELA", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("SAMBALPUR", "0");
                        array_admin.add(etaBean);
                        etaBean = new CircelBean("TALCHER", "0");
                        array_admin.add(etaBean);


                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                        recyclerView.addItemDecoration(itemDecoration);
                        mAdapter = new Myadapter();
                        recyclerView.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
*/


}

