package orsac.rosmerta.orsac_vehicle.android;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;


public class ServiceList extends AppCompatActivity {
    ArrayList<String> vendor_phn, veendor_name, vendor_add;
    ArrayList<String> vendor_phn_copy, veendor_name_copy, vendor_add_copy;

    String com_name_service;
    RecyclerView recyclerview;
    TextView textView4;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("Contact Detail");
        preferenceHelper = new PreferenceHelper(this);


        textView4 = (TextView) findViewById(R.id.textView4);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager (getApplicationContext(), 1);
        recyclerview.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerview.addItemDecoration(itemDecoration);
        Intent inty = getIntent();
        com_name_service = inty.getStringExtra("vendor_name");
        textView4.setText(com_name_service);

        try{
            new ServiceList_asyn().execute();

        }catch (Exception e){
            e.getMessage();
        }

        vendor_phn = new ArrayList<>();
        veendor_name = new ArrayList<>();
        vendor_add = new ArrayList<>();

        vendor_phn_copy = new ArrayList<>();
        veendor_name_copy = new ArrayList<>();
        vendor_add_copy = new ArrayList<>();
        //arya 0 -1
        vendor_phn.add("9937383917");
        vendor_phn.add("8093673715");
        veendor_name.add("Sumit kumar rout");
        veendor_name.add("Sanjay pasayat");
        vendor_add.add("Co:dilip Pradhan, 1st floor pradhan complex, infront of A1 Bazar, near kali mandir, hatatota, talcher:759100");
        vendor_add.add("At: gajapatinagar, post brajarajnagar, dist jharsuguda:768216");


        //Atlanta 2-18
        vendor_phn.add("9437891445");
        vendor_phn.add("7684058405");
        vendor_phn.add("9114111560");
        vendor_phn.add("9040434671");
        vendor_phn.add("9439282636");
        vendor_phn.add("9439153222");
        vendor_phn.add("9861194741");
        vendor_phn.add("9778615071");
        vendor_phn.add("9861185782");
        vendor_phn.add("8984993158");
        vendor_phn.add("9438010477");
        vendor_phn.add("9438552978");
        vendor_phn.add("9078855245");
        vendor_phn.add("9861033160");
        vendor_phn.add("7735202766");
        vendor_phn.add("7978380270");
        vendor_phn.add("7008821752");
///

        veendor_name.add("Chandra Kant Mohanty");
        veendor_name.add("Razat Naik");
        veendor_name.add("Siba Prasad Mishra");
        veendor_name.add("Satyabarat Bharati");
        veendor_name.add("Ramaraw");
        veendor_name.add("Ravinder Senapati");
        veendor_name.add("Sandeep Jain");
        veendor_name.add("Sibasis Parida");
        veendor_name.add("Pravakar Swain");
        veendor_name.add("Mr.Ravindra");
        veendor_name.add("Bhagyarabi Gantayat");
        veendor_name.add("barbil");
        veendor_name.add("Ganesh");
        veendor_name.add("Mr. Gopesh");
        veendor_name.add("Mr.Saraj");
        veendor_name.add("Mr Laddhu");
        veendor_name.add("Amiya Ranjan");

        //
        vendor_add.add("Joda");
        vendor_add.add("Sundargarh");
        vendor_add.add("Talcher");
        vendor_add.add("Bhubaneswar");
        vendor_add.add("Cuttack");
        vendor_add.add("Berahampur");
        vendor_add.add("Sambalpur");
        vendor_add.add("Bhubaneswar");
        vendor_add.add("Talcher");
        vendor_add.add("Na");
        vendor_add.add("Jajpur Road");
        vendor_add.add("Badbil");
        vendor_add.add("Jajpur Road");
        vendor_add.add("Bhubneswar");
        vendor_add.add("Na");
        vendor_add.add("Cuttack");
        vendor_add.add("Cuttack");

        //etracn  19-20
        vendor_phn.add("7504117037");
        vendor_phn.add("9178765962");

        veendor_name.add("Sujit Majhi");
        veendor_name.add("Vikas Manthan");

        vendor_add.add("eTrans Solution, Duburi, KNIC, Jajpur Road, Odisha , PIN - 755026");
        vendor_add.add("eTrans Solution, Military Chowk, Near Kalinga Weigh Bridge, Jajpur Road, Odisha , PIN - 755026");

        //MapmyIndia 21-23
        vendor_phn.add("7008350884");
        vendor_phn.add("9437364818");
        vendor_phn.add("7008615033");

        veendor_name.add("Ajay Pradhan");
        veendor_name.add("paradeep");
        veendor_name.add("Bibhu");

        vendor_add.add("Jagannathpur, Keonjhar Town, Keonjhar - 758001");
        vendor_add.add("Nimidihi, Infront of Kanya Ashram, Paradeep");
        vendor_add.add("Keonjhar");


        //Fastrackerz 24-41
        vendor_phn.add("8599005813");
        vendor_phn.add("9853018412");
        vendor_phn.add("9938738159");
        vendor_phn.add("9439486675");
        vendor_phn.add("9853456726");
        vendor_phn.add("9439977178");
        vendor_phn.add("9778862385");
        vendor_phn.add("9861063968");
        vendor_phn.add("9937074755");
        vendor_phn.add("9938688665");
        vendor_phn.add("8658048656");
        vendor_phn.add("9937475010");
        vendor_phn.add("9348757380");
        vendor_phn.add("7205194648");
        vendor_phn.add("9937579924");
        vendor_phn.add("9078073682");
        vendor_phn.add("8018086066");
        vendor_phn.add("7735202766");

        //
        veendor_name.add("Manas Ranjan Parida");
        veendor_name.add("Manoj Nayak");
        veendor_name.add("Basant Kumar Dale");
        veendor_name.add("Tilu");
        veendor_name.add("Sushant Das");
        veendor_name.add("Askash Mahakud");
        veendor_name.add("DILSHAN WARSHI");
        veendor_name.add("Satyabrat");
        veendor_name.add("Mr Azad khan,Mr Biswajit");
        veendor_name.add("Mr Rakesh,");
        veendor_name.add("BIKASH PRUSETH");
        veendor_name.add("FIROZZ");
        veendor_name.add("CHUDAMANI CHHACHHAN");
        veendor_name.add("NAWAB");
        veendor_name.add("PINTU");
        veendor_name.add("Rajesh");
        veendor_name.add("Raghunath Routray");
        veendor_name.add("Saroj Nayak");

        vendor_add.add("Penguine, Plot no- T4/24,Civiltownship,Rourkela-769015");
        vendor_add.add("Sai Touch Solutions, Duburi,Sukinda,Jajpur");
        vendor_add.add("Sai Touch Solutions, Chandikhol,Paradeep,Keonjhar");
        vendor_add.add("Valley City Koira, Parking area,Near sai sidhi transport.Masjid road,koira, sundergarh.odisha");
        vendor_add.add("Valley city Cybernatics, Chand Garaj Back Site Banspani");
        vendor_add.add("Valley city Cybernatics, BHADARASAI");
        vendor_add.add("FORTUNE INFOTECH, FORTUNE INFOTECH,CWS MARKET COMPLEX,SOUTH BALANDA,TALCHER,DIST- ANGUL,ODISHA");
        vendor_add.add("GLOMAX, Ramachandra Bazar,1st gate,Talcher Town,Angul");
        vendor_add.add("VACRON TECHNOLOGY, CYBER MULTI MEDIA,Near Badbil bus stand,Badbil,keonjhar");
        vendor_add.add("VACRON TECHNOLOGY, Sukinda,jajpur");
        vendor_add.add("VK GLOBAL SERVICES, KULDA MCL AREA(SUNDARGARH)");
        vendor_add.add("VK GLOBAL SERVICES, SUNDARGARH BYPASS");
        vendor_add.add("VK GLOBAL SERVICES, SUNDARGARH TOWN");
        vendor_add.add("VK GLOBAL SERVICES, BANDHBAHAL AREA");
        vendor_add.add("VK GLOBAL SERVICES, BRAJRAJNAGAR AREA");
        vendor_add.add("Sonsy Tech, Jajpur/Keonjhar/Bhubaneswar/Cuttack");
        vendor_add.add("ORDUINO LABS PVT LTD, Balahar Chhack,Talche");
        vendor_add.add("Smart Security, Butupali boudh,");


        //Orduino 42-51

        vendor_phn.add("8018086069");
        vendor_phn.add("9437073387");
        vendor_phn.add("9438010477");
        vendor_phn.add("9938467400");
        vendor_phn.add("7978139366");
        vendor_phn.add("9861909694");
        vendor_phn.add("7978456621");
        vendor_phn.add("8917247512");
        vendor_phn.add("8249180268");
        vendor_phn.add("9439153222");

        veendor_name.add("CHANDRASEKHAR ROUT");
        veendor_name.add("Sambeet Kumar Mohanty");
        veendor_name.add("Bhagyarabi Gantayat");
        veendor_name.add("Mr Das");
        veendor_name.add("ASHUTOSH MISHRA");
        veendor_name.add("DEBA PRASAD MISHRA");
        veendor_name.add("CHINMAY ROUT");
        veendor_name.add("SUNIL KUMAR BAGHASINGH");
        veendor_name.add("GAGAN");
        veendor_name.add("RABINDRA SENAPATI");

        vendor_add.add("ORDUINO LABS PVT LTD, ROOM NO -214,2nd FLOOR,OCAC TOWER,ACHARYAVIHAR,BHUBANESWAR");
        vendor_add.add("ALFA TRADE LINKSPlot no c- 42,Palaspalli,bubaneswar");
        vendor_add.add("COMPUTER SOLUTIONNear Hotel Meadeast,Chandikhol,Jajpur");
        vendor_add.add("MAA ENTERPRISESSERENDA, BARBIL, KEONJHAR");
        vendor_add.add("PUSPANJALI TRADERSMISHRAPADA,ANUGUL,ODISHA");
        vendor_add.add("PRISMA SOLUTIONSCHANDRASEKHARPUR,BHUBANESWAR");
        vendor_add.add("SAI SHEETAL ENTERPRISESRASULGARH,BHUBANESWAR");
        vendor_add.add("SUPRIT TRADERSMANGULI CHOWK ,CUTTACK");
        vendor_add.add("TRACKTECHPlot No- 549/1653,BOMIKHAL, BHUBANESWAR");
        vendor_add.add("SUNSHINE AGENCYGandhi Nagar, 5th Lane Ext.Old Spectrum Colony, Plot No.20, Berhampur");


        //iTriangle 52-54
        vendor_phn.add("9560609863");
        vendor_phn.add("9560609863");
        //
        veendor_name.add("Customer Care");
        veendor_name.add("Arif Wani");

        vendor_add.add("Customer Care");
        vendor_add.add("Bhubaneswar");


        //RAPL 55-62
        vendor_phn.add("8114379524");
        vendor_phn.add("9937036080");
        vendor_phn.add("9437040474");
        vendor_phn.add("9040220440");
        vendor_phn.add("8249679367");
        vendor_phn.add("7978524025");
        vendor_phn.add("9861194741");
        vendor_phn.add("8249921293");

        //

        veendor_name.add("Mr. Tanmaya Ranjan Sahoo");
        veendor_name.add("Mr. Deepak Sahoo");
        veendor_name.add("Mr. Sushant");
        veendor_name.add("Mr. Basant Sahoo");
        veendor_name.add("Mr. Anurag Mishra");
        veendor_name.add("Mr. Pavitra Panda");
        veendor_name.add("Mr. Sandeep Jain");
        veendor_name.add("Mr. Pramod Ojha");

        vendor_add.add("Ideal Financial Services, Near FCI Godown, in front of Shri Metalic Office, Barbil");
        vendor_add.add("Global Solar Venture, Sharma Chhaka, Near Railway Crossing, Talcher");
        vendor_add.add("Sarla Motors, Vedvyas Chowk, Rourkela");
        vendor_add.add("Krishna Motors, Near Hansita Filling Station, Belphar, Jharsuguda");
        vendor_add.add("Good Will India, First Gate, (Association Parking Yard), Berhampur");
        vendor_add.add("Mahavir Enterprises, Inside Diamond Weighbridge, Manguli Chowk, Cuttak");
        vendor_add.add("Jain Hardware, 9 number, Near Truck owner association, Barjrajnagar");
        vendor_add.add("Rosmerta Autotech Pvt. Ltd., Plt No. 969/2437, Part Street, Forest Park Road, New Bapuji Nagar, Bhubaneswar");


        //trimble 63
        vendor_phn.add("9810761109");

        //
        veendor_name.add("Vinay Pathak");

        vendor_add.add("Vinay Pathak ,Manager Sales");

       /* if (com_name_service.equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")) {
            Toast.makeText(ServiceList.this, "Arya Omnitalk Wir", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();
            for (int i = 0; i <= 1; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("Atlanta")) {
            Toast.makeText(ServiceList.this, "Atlanta", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 2; i <= 18; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);
        } else if (com_name_service.equalsIgnoreCase("CE Info Systems Pvt. Ltd.")) {
            Toast.makeText(ServiceList.this, "CE Info Systems Pvt. Ltd.", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 21; i <= 23; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("eTrans Solutions Private Limited")) {
            Toast.makeText(ServiceList.this, "eTrans Solutions Private Limited", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 19; i <= 20; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("Fastrackerz")) {
            Toast.makeText(ServiceList.this, "Fastrackerz", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 24; i <= 41; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")) {
            Toast.makeText(ServiceList.this, "iTriangle Infotech Pvt. Ltd", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 52; i <= 53; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("Orduino Labs")) {
            Toast.makeText(ServiceList.this, "Orduino Labs", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 42; i <= 51; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("Rosmerta Autotech Private Ltd.")) {
            Toast.makeText(ServiceList.this, "Rosmerta Autotech Private Ltd.", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 54; i <= 62; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        } else if (com_name_service.equalsIgnoreCase("Trimble Mobility Solutions India Pvt.")) {
            Toast.makeText(ServiceList.this, "Trimble Mobility Solutions India Pvt.", Toast.LENGTH_SHORT).show();
            vendor_phn_copy.clear();
            veendor_name_copy.clear();
            vendor_add_copy.clear();

            for (int i = 63; i <= 63; i++) {
                vendor_add_copy.add(vendor_add.get(i));
                veendor_name_copy.add(veendor_name.get(i));
                vendor_phn_copy.add(vendor_phn.get(i));

            }
            Myadapter mAdapter = new Myadapter();
            recyclerview.setAdapter(mAdapter);

        }*/


    }
    class ServiceList_asyn extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        ProgressBar pbHeaderProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pbHeaderProgress = (ProgressBar) findViewById(R.id.progress_bar);

            AndyUtils.showCustomProgressDialog(ServiceList.this,
                    "Fetching Information Please wait...", false, null);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("userid", "10001"));
            //nameValuePairs.add(new BasicNameValuePair("device_user", "android"));
            //nameValuePairs.add(new BasicNameValuePair("device_token", getDeviceToken));
            com_name_service = com_name_service.replace(" " , "%20");
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+"companyvendor?searchTag="+com_name_service);

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
                        //HashMap<String, String> map = new HashMap<>();
                        try {
                            for (int i = 0; i < jsonArray_.length(); i++) {

                                JSONObject jsonObj1 = jsonArray_.getJSONObject(i);
                                String  vendor_add_copy_st = jsonObj1.getString("addr");
                                String  veendor_name_copy_st = jsonObj1.getString("conperson");
                                String  vendor_phn_copy_st = jsonObj1.getString("phno");
                                vendor_add_copy.add(vendor_add_copy_st);
                                veendor_name_copy.add(veendor_name_copy_st);
                                vendor_phn_copy.add(vendor_phn_copy_st);

                            }
                            Myadapter mAdapter = new Myadapter();
                            recyclerview.setAdapter(mAdapter);

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }


                     AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }

    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        String routeId;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_service_center, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String phn = vendor_phn_copy.get(position);
            String address = vendor_add_copy.get(position);
            String name_ = veendor_name_copy.get(position);


            holder.from_to.setText(name_);
            holder.vendor_add.setText(address);
            holder.mobile_v.setText(phn);

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
            });
        }


        @Override
        public int getItemCount() {
            return vendor_phn_copy.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView from_to;
            protected TextView vendor_add;
            protected TextView  mobile_v;
            protected TextView  calll ;


            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                from_to = (TextView) itemLayputView.findViewById(R.id.from_to);
                vendor_add = (TextView) itemLayputView.findViewById(R.id.vendor_add);
                mobile_v = (TextView) itemLayputView.findViewById(R.id.mobile_v);
                calll = (TextView) itemLayputView.findViewById(R.id.calll);


            }
        }
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

}
