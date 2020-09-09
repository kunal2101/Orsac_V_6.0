package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import java.util.HashMap;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.Orsac.Model.Pojo_Device_Management;

public class Device_Manag_admin extends AppCompatActivity {
    private List<Pojo_Device_Management> array_admin;
    private RecyclerView recyclerView;
    Pojo_Device_Management pojo_device_management;
    private Myadapter mAdapter;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    FrameLayout frame_device_update;
    String change_result;
    int countt = 0;
    PreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__manag_admin);
        preferenceHelper = new PreferenceHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewadmin);
        frame_device_update = (FrameLayout)findViewById(R.id.frame_device_update);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("BG Detail");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        array_admin = new ArrayList<Pojo_Device_Management>();
        if(isConnection()){
            new getAllVendorDetail().execute();

        }else {
            Toast.makeText(this, "No Internet Connection....", Toast.LENGTH_SHORT).show();
        }
//        pojo_device_management = new Pojo_Device_Management("Rosemerta Autotech Private Ltd.",  "13754", "13456","3750000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("Fasttrackerz",  "18484", "20000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("Atlanata",  "15985", "17000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("Arya Omnitalk Wireless Solution Pvt Ltd.",  "3047", "5000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("Trimble Mobility Solution Pvt Ltd.",  "468", "0000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("eTrans Solutions Pvt Ltd.",  "182", "500");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("Orduino Labs",  "2574", "5000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("CE Info Systems Pvt. Ltd.",  "4435", "5000");
//        array_admin.add(pojo_device_management);
//        pojo_device_management = new Pojo_Device_Management("iTriangle Infotech Pvt. Ltd",  "20340", "22000");
//        array_admin.add(pojo_device_management);


        frame_device_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(Device_Manag_admin.this,Device_Bg_Update.class);
                inty.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );
                startActivity(inty);
                //finish();
            }
        });


    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Device_Manag_admin.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }
    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_mangement, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final Myadapter.ViewHolder holder, int position) {
            final Pojo_Device_Management adminModel = array_admin.get(position);

            holder.com_name.setText(adminModel.getCom_name());
            holder.ins_dev.setText("Total Installed \n"+adminModel.getInstall_device());
            holder.bg_valid.setText("BG Device Limit  \n"+adminModel.getBg_device());
            holder.bg_amount.setText("BG Amount  \n"+adminModel.getBg_amont());
//            if(countt == 0) {
//                if (Integer.parseInt(adminModel.getInstall_device()) < Integer.parseInt(adminModel.getBg_device())) {
//                    holder.value.setText("Active");
//                } else {
//                    holder.value.setText("InActive");
//                }
//            }else {
//                holder.value.setText(change_result);
//
//            }
            if(adminModel.getBgStatus().equalsIgnoreCase("1")){
                holder.value.setText("Click Here to \n"+"InActive");
            } else if (adminModel.getBgStatus().equalsIgnoreCase("0")){
                holder.value.setText("Click Here to \n"+"Active");
            }

            holder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value_status = holder.value.getText().toString();
                    if(value_status.equalsIgnoreCase("Click Here to \n"+"InActive")){
                        new getUpdateStatus().execute("fasle",adminModel.getLoginid());

                    }else if (value_status.equalsIgnoreCase("Click Here to \n"+"Active")){
                        new getUpdateStatus().execute("true",adminModel.getLoginid());

                    }
                    //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/bgstatusEnableFromMobile?loginid=10002&setEnable=true
                    Toast.makeText(Device_Manag_admin.this, "Click On CAll Button", Toast.LENGTH_SHORT).show();
                   }
            });
        }
        @Override
        public int getItemCount() {
            return array_admin.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView com_name;
            protected TextView ins_dev;
            protected TextView bg_valid;
            protected TextView bg_amount;
            protected TextView value;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                com_name = (TextView) itemLayputView.findViewById(R.id.com_name);
                ins_dev = (TextView) itemLayputView.findViewById(R.id.ins_dev);
                bg_valid = (TextView) itemLayputView.findViewById(R.id.bg_valid);
                bg_amount =(TextView) itemLayputView.findViewById(R.id.bg_amount);
                value = (TextView) itemLayputView.findViewById(R.id.value);

            }
        }
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    class getAllVendorDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Device_Manag_admin.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();




            try {
                HttpClient httpclient = new DefaultHttpClient();
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataSpecific?dataRange="+args[0]);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_BG_DETAIL+"?loginid=all");
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
                        JSONArray jsonArray_   =  new JSONArray(getResponse);

                        if(jsonArray_.length()>0) {
                            try {
                                for (int i = 0; i < jsonArray_.length(); i++) {

                                    JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                    Pojo_Device_Management admin_bean = new Pojo_Device_Management();


                                    admin_bean.setCom_name(jsonObj1.getString("compName"));
                                    admin_bean.setBg_amont(jsonObj1.getString("bgamount"));

                                    admin_bean.setBg_device(jsonObj1.getString("vehicleallowed"));
                                    admin_bean.setInstall_device(jsonObj1.getString("installedCnt"));
                                    admin_bean.setLoginid(jsonObj1.getString("loginid"));
                                    admin_bean.setBgStatus(jsonObj1.getString("bgstatus"));

                                    array_admin.add(admin_bean);
                                }

                                mAdapter = new Myadapter();
                                recyclerView.setAdapter(mAdapter);

                            } catch (Exception ev) {
                                System.out.print(ev.getMessage());
                            }
                        }else{
                            Toast.makeText(Device_Manag_admin.this,"Date Out Of Range .....",Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }

            });
        }

    }
    class getUpdateStatus extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        int total_count =0,total_i3ms=0,total_etp=0;
        int total_active=0;
        int total_inactive = 0;
        int total_i3ms_s=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Device_Manag_admin.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // AndyUtils.showCustomProgressDialog(ActivityLogin.this, "Authenticating your account", "Please wait...", false);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            AndyUtils.removeCustomProgressDialog();


           // http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/bgstatusEnableFromMobile?loginid=10002&setEnable=true


            try {
                HttpClient httpclient = new DefaultHttpClient();
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataSpecific?dataRange="+args[0]);
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_BG_STATUS_UPDATE+"?loginid="+args[1]+"&setEnable="+args[0]);
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
                        JSONArray jsonArray_   =  new JSONArray(getResponse);

                        if(jsonArray_.length()>=0) {
                            try {
                                for (int i = 0; i < jsonArray_.length(); i++) {

                                    JSONObject jsonObj1 = jsonArray_.getJSONObject(i);


                                    change_result= jsonObj1.getString("EnableCode");
                                        countt = countt+1;
                                    Toast.makeText(Device_Manag_admin.this, jsonObj1.getString("Message"), Toast.LENGTH_SHORT).show();
                                }

                                Intent inty = new Intent(Device_Manag_admin.this,Device_Manag_admin.class);
                                startActivity(inty);
                                finish();
                                   // new getAllVendorDetail().execute();
                               // mAdapter = new Myadapter();
                                //recyclerView.setAdapter(mAdapter);


                            } catch (Exception ev) {
                                System.out.print(ev.getMessage());
                            }
                        }else{
                            Toast.makeText(Device_Manag_admin.this,"Date Out Of Range .....",Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    progressDialog.dismiss();
                }

            });
        }

    }


}
