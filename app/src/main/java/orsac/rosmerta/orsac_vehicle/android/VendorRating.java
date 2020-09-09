package orsac.rosmerta.orsac_vehicle.android;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;

import static android.app.Activity.RESULT_OK;

public class VendorRating extends AppCompatActivity {
    private RecyclerView recyclerview;
    private String title[] = {"Rosmerta Autotech","Atlanta","Etrans","Trimble","Araya Omnitech"};
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    FrameLayout frame_floating;
    ArrayList<HashMap<String , String>> vendor_arra ;
    ArrayList<HashMap<String , String>> vendor_arra_sort ;
    ArrayList<String> com_name_arr;
    ArrayList<Double> vendor_active_arr ;
    PreferenceHelper preferenceHelper;


    //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=7
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.omvts_vendor_rating);
        frame_floating = (FrameLayout)findViewById(R.id.frame_floating);
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
        tool_title.setText("Vendor Rating");
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
     recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerview.setLayoutManager(layoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerview.addItemDecoration(itemDecoration);
        preferenceHelper = new PreferenceHelper(this);


        if(isConnection()) {
            new ServiceList_asyn().execute();
        }else{
            Toast.makeText(this, "No INternet Connection..", Toast.LENGTH_SHORT).show();
        }
        frame_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.orsac.gov.in/TenderFiles/Empaneled%20Vendor.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) VendorRating.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }
    public void sortmapKey() {

        // This map stores unsorted values
        Map<String, Integer> map = new HashMap<>();

        // Function to sort map by Key

            ArrayList<String> sortedKeys =
                    new ArrayList<String>(map.keySet());

            Collections.sort(sortedKeys);

            // Display the TreeMap which is naturally sorted
            for (String x : sortedKeys)
                System.out.println("Key = " + x +
                        ", Value = " + map.get(x));
        }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        HashMap<String,String> map;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_rating_list, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final HashMap<String,String> map = vendor_arra_sort.get(position);


            holder.com_name.setText(map.get("com_name"));
            holder.sl_no.setText(position+1+")  ");
            holder.pertentage.setText(map.get("active")+"%");
           if(map.get("bg_status").equalsIgnoreCase("Valid ")){
               holder.bg_status_.setTextColor(Color.parseColor("#2E816A"));
               holder.bg_status_.setText( map.get("bg_status"));

           }else {
               holder.bg_status_.setTextColor(Color.parseColor("#BF1A21"));
               holder.bg_status_.setText( map.get("bg_status"));
           }

          //  holder.bg_insall.setText("Total Installed -  " + map.get("insall"));
            holder.bg_insall.setText(map.get("insall"));

            holder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" +  map.get("phn_no")));
                    if (ActivityCompat.checkSelfPermission(VendorRating.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);

                }
            });
            holder.buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // NodataDia();
                    Intent intent = new Intent(VendorRating.this,Activity_Buynow_Tab.class);
                    intent.putExtra("com_name",map.get("com_name"));
                    startActivity(intent);
                }
            });

            // holder.vendor_add.setText(address);
          /*  holder.mobile_v.setText(phn);

            holder.calll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phn));
                    if (ActivityCompat.checkSelfPermission(ServiceList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            });*/
        }


        @Override
        public int getItemCount() {
            return vendor_arra_sort.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected MyTextView com_name;
            protected MyTextView vendor_add;
            protected  MyTextView sl_no;
            protected  TextView pertentage;
            protected TextView value;
            protected  TextView bg_status_;
            protected  TextView bg_insall;
            protected TextView buy_now ;



            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                com_name = (MyTextView) itemLayputView.findViewById(R.id.com_name);
                vendor_add = (MyTextView) itemLayputView.findViewById(R.id.vendor_add);
                sl_no = (MyTextView) itemLayputView.findViewById(R.id.sl_no);
                pertentage=(TextView) itemLayputView.findViewById(R.id.pertentage);
                value=(TextView) itemLayputView.findViewById(R.id.value);
                bg_status_=(TextView) itemLayputView.findViewById(R.id.bg_status_);
                bg_insall= (TextView)itemLayputView.findViewById(R.id.bg_insall);
                buy_now = (TextView)itemLayputView.findViewById(R.id.buy_now);



            }
        }
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    class ServiceList_asyn extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        ProgressBar pbHeaderProgress;
        int count_com = 0,count_Arya_count =0 ,count_Atlanta_conu =0 ,count_iTriangle=0,count_CE_=0,count_Fastrackerz =0 ,count_rosmert =0,count_trimble=0,count_Orduino_Labs=0;
        int etran_count = 0 ,Arya_count =0 ,Atlanta_conu =0 ,iTriangle_coun=0,CE_count__=0,fastrc_count=0 ,rosmerta_cont=0,trimble_count=0,Orduino_Labs_count=0;
        int itranagle_toatl_count=0,ce_total_count=0,fasttrack_total_count=0,rosmerata_total_count=0,atlanta_total_count=0,Arya_total_count=0,eTrans_total_count=0,Trimble_total_count=0,Orduino_Labs_Total_conut=0;
        String st_bg_status_itranagle,st_bg_status_ce,st_bg_status_fasttrack,st_bg_status_rosmerata,st_bg_status_atlanta,st_bg_status_Arya,st_bg_status_eTrans,st_bg_status_Trimble,st_bg_status_Orduino;
        String st_install_itranagle,st_install_ce,st_install_fasttrack,st_install_rosmerata,st_install_atlanta,st_install_Arya,st_install_eTrans,st_install_Trimble,st_install_Orduino;
        int st_intall_count_itranagle=0,st_install_count_ce=0,st_install_count_fasttrack=0,st_install_count_rosmerata=0,st_install_count_atlanta=0,st_install_count_Arya=0,st_install_count_eTrans=0,st_install_count_Trimble=0,st_install_count_Orduino=0;

        int intall_count =0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // pbHeaderProgress = (ProgressBar) findViewById(R.id.progress_bar);

            AndyUtils.showCustomProgressDialog(VendorRating.this,
                    "Fetching Information Please wait...", false, null);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            vendor_arra = new ArrayList<>();
            vendor_active_arr = new ArrayList<>();
            vendor_arra_sort = new ArrayList<>();

            com_name_arr= new ArrayList<>();


            try {
                HttpClient httpclient = new DefaultHttpClient();
             //   HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getMobDashDataNew?dataRange=6");
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.GET_MOB_DASH_DATA_NEW+"?dataRange=6");

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
                        // JSONObject jsonObj = new JSONObject(getResponse);

                        try {
                            for (int i = 0; i < jsonArray_.length(); i++) {

                                JSONObject jsonObj1 = jsonArray_.getJSONObject(i);



                                com_name_arr.add(jsonObj1.getString("com_name"));
                                if (jsonObj1.getString("com_name").equalsIgnoreCase("Orduino Labs")){
                                    count_Orduino_Labs =  count_Orduino_Labs +1;
                                    if(st_install_count_Orduino == 0) {
                                        st_install_Orduino = jsonObj1.getString("installeddevice");
                                        if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                            //  st_bg_status_Orduino = "BG Status -  Valid " ;
                                            st_bg_status_Orduino = "Valid " ;
                                        }else {
                                            //st_bg_status_Orduino ="BG Status -  Invalid ";
                                            st_bg_status_Orduino ="Invalid ";

                                        }
                                        st_install_count_Orduino++;
                                    }
                                    Orduino_Labs_count = Orduino_Labs_count +Integer.parseInt(jsonObj1.getString("active"));
                                    Orduino_Labs_Total_conut=Orduino_Labs_Total_conut+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                    if(count_Orduino_Labs == 7){//i3msactive
                                        double seve_count_avg = Orduino_Labs_count/7;
                                        double tota_count_avg = Orduino_Labs_Total_conut/7;
                                        double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("active",String.format("%.2f",active_count_div));
                                        map.put("com_name",jsonObj1.getString("com_name"));
                                        map.put("insall",st_install_Orduino);

                                        map.put("phn_no","8018086066");
                                        map.put("bg_status",st_bg_status_Orduino);
                                       // vendor_active_arr.add((double)active_count_div);
                                        vendor_active_arr.add(round(active_count_div,2));
                                        vendor_arra.add(map);
                                    }
                                }

                                if (jsonObj1.getString("com_name").equalsIgnoreCase("Trimble Mobility Solutions India Pvt. Ltd.")){
                                    count_trimble =  count_trimble +1;
                                    if(st_install_count_Trimble == 0) {
                                        st_install_Trimble = jsonObj1.getString("installeddevice");
                                        if(jsonObj1.isNull("i3msinactive")) {
                                            // st_bg_status_Trimble ="BG Status -  Invalid ";
                                            st_bg_status_Trimble ="Invalid ";
                                        } else {
                                            st_bg_status_Trimble ="Invalid ";

                                        }
                                        st_install_count_Trimble++;
                                    }
                                    trimble_count = trimble_count +Integer.parseInt(jsonObj1.getString("active"));
                                    Trimble_total_count=Trimble_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));



                                    if(count_trimble == 7){//i3msactive
                                        double seve_count_avg = trimble_count/7;
                                        double tota_count_avg = Trimble_total_count/7;
                                        double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("active",String.format("%.2f",active_count_div));
                                        map.put("com_name",jsonObj1.getString("com_name"));
                                        map.put("insall",st_install_Trimble);

                                        map.put("phn_no","9810761109");
                                        map.put("bg_status",st_bg_status_Trimble);
                                        //vendor_active_arr.add((double)active_count_div);
                                        vendor_active_arr.add(round(active_count_div,2));
                                        vendor_arra.add(map);
                                    }
                                }
                             if (jsonObj1.getString("com_name").equalsIgnoreCase("eTrans Solutions Private Limited")){
                                 count_com =  count_com +1;
                                 etran_count = etran_count +Integer.parseInt(jsonObj1.getString("active"));
                                 if(st_install_count_eTrans== 0) {
                                     st_install_eTrans = jsonObj1.getString("installeddevice");
                                     if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                         st_bg_status_eTrans = "Valid " ;
                                     }else {
                                         st_bg_status_eTrans ="Invalid ";
                                     }
                                     st_install_count_eTrans++;
                                 }

                                 eTrans_total_count=eTrans_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_com == 7){//i3msactive
                                     double seve_count_avg = etran_count/7;
                                     double tota_count_avg = eTrans_total_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("phn_no","7894427233");//7894427233
                                     map.put("insall",st_install_eTrans);

                                     map.put("bg_status",st_bg_status_eTrans);
                                    // vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                    vendor_arra.add(map);
                                }
                                }
                                if (jsonObj1.getString("com_name").equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")){
                                 count_Arya_count =  count_Arya_count +1;
                                    if(st_install_count_Arya== 0) {
                                        st_install_Arya = jsonObj1.getString("installeddevice");
                                        if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                            st_bg_status_Arya = "Valid " ;
                                        }else {
                                            st_bg_status_Arya ="Invalid ";
                                        }
                                        st_install_count_Arya++;
                                    }

                                    Arya_count = Arya_count +Integer.parseInt(jsonObj1.getString("active"));
                                    Arya_total_count=Arya_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_Arya_count == 7){//i3msactive
                                     double seve_count_avg = Arya_count/7;
                                     double tota_count_avg = Arya_total_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("insall",st_install_Arya);

                                     map.put("phn_no","9937049121");
                                     map.put("bg_status",st_bg_status_Arya);
                                     //vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                     vendor_arra.add(map);
                                 }
                             }

                             if (jsonObj1.getString("com_name").equalsIgnoreCase("Atlanta")){
                                 count_Atlanta_conu =  count_Atlanta_conu +1;
                                 Atlanta_conu = Atlanta_conu +Integer.parseInt(jsonObj1.getString("active"));
                                 if(st_install_count_atlanta== 0) {
                                     st_install_atlanta = jsonObj1.getString("installeddevice");
                                     if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                         st_bg_status_atlanta = "Valid " ;
                                     }else {
                                         st_bg_status_atlanta ="Invalid ";
                                     }
                                     st_install_count_atlanta++;
                                 }

                                 atlanta_total_count=atlanta_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_Atlanta_conu == 7){
                                     double seve_count_avg = Atlanta_conu/7;
                                     double tota_count_avg = atlanta_total_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("insall",st_install_atlanta);

                                     map.put("phn_no","9312912400");
                                     map.put("bg_status",st_bg_status_atlanta);
                                    // vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                     vendor_arra.add(map);
                                 }
                             }
                             if (jsonObj1.getString("com_name").equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")){
                                 count_iTriangle =  count_iTriangle +1;
                                 if(st_intall_count_itranagle == 0) {
                                     st_install_itranagle = jsonObj1.getString("installeddevice");
                                     if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                         st_bg_status_itranagle = "Valid " ;
                                     }else {
                                         st_bg_status_itranagle ="Invalid ";
                                     }
                                     st_intall_count_itranagle++;
                                 }

                                 iTriangle_coun = iTriangle_coun +Integer.parseInt(jsonObj1.getString("active"));
                                 itranagle_toatl_count=itranagle_toatl_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_iTriangle == 7){//i3msactive

                                     double seve_count_avg = iTriangle_coun/7;
                                     double tota_count_avg = itranagle_toatl_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;

                                     // etran_count = (etran_count/Integer.parseInt(jsonObj1.getString("i3msactive"))*100);
                                     int e_count = (iTriangle_coun/Integer.parseInt(jsonObj1.getString("i3msactive"))*100);
                                     int e_c= iTriangle_coun*100;
                                     int   e_c_ = e_c/Integer.parseInt(jsonObj1.getString("i3msactive"));
                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("phn_no","9999585750");
                                     map.put("insall",st_install_itranagle);

                                     map.put("bg_status",st_bg_status_itranagle);
                                    // vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                     //vendor_active_arr.add(String.format("%.2f",active_count_div));
                                     vendor_arra.add(map);
                                 }
                             }
                             if (jsonObj1.getString("com_name").equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
                                 count_CE_ =  count_CE_ +1;
                                 if(st_install_count_ce == 0) {
                                     st_install_ce = jsonObj1.getString("installeddevice");
                                     if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                         st_bg_status_ce = "Valid " ;
                                     }else {
                                         st_bg_status_ce ="Invalid ";
                                     }
                                     st_install_count_ce++;
                                 }


                                 CE_count__ = CE_count__ +Integer.parseInt(jsonObj1.getString("active"));
                                 ce_total_count=ce_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_CE_ == 7){//i3msactive
                                     double seve_count_avg = CE_count__/7;
                                     double tota_count_avg = ce_total_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;

                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("phn_no","9934180910");
                                     map.put("insall",st_install_ce);

                                     map.put("bg_status",st_bg_status_ce);
                                     //vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                     vendor_arra.add(map);
                                 }
                             }
                             if (jsonObj1.getString("com_name").equalsIgnoreCase("Fastrackerz")){
                                 count_Fastrackerz =  count_Fastrackerz +1;

                                 if(st_install_count_fasttrack == 0) {
                                     st_install_fasttrack = jsonObj1.getString("installeddevice");
                                     st_install_count_fasttrack++;
                                     if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                         st_bg_status_fasttrack = "Valid " ;
                                     }else {
                                         st_bg_status_fasttrack ="Invalid ";
                                     }
                                 }

                                 fastrc_count = fastrc_count +Integer.parseInt(jsonObj1.getString("active"));
                                 fasttrack_total_count=fasttrack_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                 if(count_Fastrackerz == 7){//i3msactive
                                     double seve_count_avg = fastrc_count/7;
                                     double tota_count_avg = fasttrack_total_count/7;
                                     double active_count_div = (seve_count_avg/tota_count_avg)*100;

                                     HashMap<String, String> map = new HashMap<>();
                                     map.put("active",String.format("%.2f",active_count_div));
                                     map.put("com_name",jsonObj1.getString("com_name"));
                                     map.put("phn_no","8448491850");
                                     map.put("insall",st_install_fasttrack);
                                     map.put("bg_status",st_bg_status_fasttrack);
                                     //vendor_active_arr.add((double)active_count_div);
                                     vendor_active_arr.add(round(active_count_div,2));
                                     vendor_arra.add(map);
                                 }
                             }
                                if (jsonObj1.getString("com_name").equalsIgnoreCase("Rosmerta Autotech Private Ltd.")){
                                    count_rosmert =  count_rosmert +1;
                                    if(st_install_count_rosmerata == 0) {
                                        st_install_rosmerata = jsonObj1.getString("installeddevice");
                                        if(Integer.parseInt(jsonObj1.getString("installeddevice"))< Integer.parseInt(jsonObj1.getString("i3msinactive"))){
                                            st_bg_status_rosmerata = "Valid " ;
                                        }else {
                                            st_bg_status_rosmerata ="Invalid ";
                                        }
                                        st_install_count_rosmerata++;
                                    }

                                    rosmerta_cont = rosmerta_cont +Integer.parseInt(jsonObj1.getString("active"));
                                    rosmerata_total_count=rosmerata_total_count+Integer.parseInt(jsonObj1.getString("i3msactive"));

                                    if(count_rosmert == 7){//i3msactive
                                        double seve_count_avg = rosmerta_cont/7;
                                        double tota_count_avg = rosmerata_total_count/7;
                                        double active_count_div = (seve_count_avg/tota_count_avg)*100;
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("active",String.format("%.2f",active_count_div));
                                        map.put("com_name",jsonObj1.getString("com_name"));
                                        map.put("phn_no","7381014072");
                                        map.put("insall",st_install_rosmerata);

                                        map.put("bg_status",st_bg_status_rosmerata);
                                       // vendor_active_arr.add((double)active_count_div);
                                        vendor_active_arr.add(round(active_count_div,2));
                                        vendor_arra.add(map);
                                    }
                                }


                            }


                            Collections.sort(vendor_active_arr);
                            Collections.reverse(vendor_active_arr);
                           for (int i = 0 ; i < vendor_arra.size();i++) {
                               for (int j = 0 ; j < vendor_arra.size();j++) {
                                   final HashMap<String,String> map = vendor_arra.get(j);
                                   String test_one = String.valueOf(vendor_active_arr.get(i));
                                   String test_ttt =(int)Double.parseDouble( map.get("active")) +"-----"+  test_one;
                                   if (vendor_active_arr.get(i) == Double.parseDouble( map.get("active"))  ) {
                                       HashMap<String, String> map_new = new HashMap<>();

                                       map_new.put("active",map.get("active"));
                                       map_new.put("com_name",map.get("com_name"));
                                       map_new.put("phn_no",map.get("phn_no"));
                                       map_new.put("bg_status",map.get("bg_status"));
                                       map_new.put("insall",map.get("insall"));

                                       vendor_arra_sort.add(map_new);

                                   }
                               }
                               }

                           /* vendor_arra_sort = new ArrayList<>();
                            final HashMap<String,String> map = vendor_arra.get(position);

                            for (int i = position ; i <=vendor_arra.size();i++){
                                for (int j = 1 ; j<vendor_arra.size()-1;j++){
                                    if (Integer.parseInt(String.valueOf(vendor_arra.get(j))) > Integer.parseInt(String.valueOf(vendor_arra.get(j + 1))))
                                    {
                                        // swap temp and arr[i]
                                        HashMap<String, String> temp = vendor_arra.get(j);
                                        vendor_arra.get(j) = vendor_arra.get(j + 1);
                                        arr[j+1] = temp;
                                    }
                                }
                            }*/

                            Myadapter mAdapter = new Myadapter();
                            recyclerview.setAdapter(mAdapter);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                            Toast.makeText(VendorRating.this, "exceeeeeee", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                        Toast.makeText(VendorRating.this, "exceeeeeee 3333333", Toast.LENGTH_SHORT).show();

                    }


                    AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }
        public  double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }

    }
    private void NodataDia(){
        final Dialog myDialog = new Dialog(VendorRating.this);
        myDialog.getWindow();
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialog_buy_now);
        TextView ok_close = (TextView)myDialog.findViewById(R.id.button);
        ok_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
                Toast.makeText(VendorRating.this,"Information Save Sucessfully",Toast.LENGTH_LONG).show();
            }
        });
        myDialog.show();
    }



}
