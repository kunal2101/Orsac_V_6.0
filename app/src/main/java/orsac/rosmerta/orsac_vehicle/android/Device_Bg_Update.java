package orsac.rosmerta.orsac_vehicle.android;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;

public class Device_Bg_Update extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    String loginid_code;
    EditText bg_deviec_amount,bg_device_update;
    Button update,btn_date;
    DatePickerDialog picker;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__bg__update);
        bg_deviec_amount= (EditText)findViewById(R.id.bg_deviec_amount);
        bg_device_update= (EditText)findViewById(R.id.bg_device_update);
        update= (Button)findViewById(R.id.update);
        btn_date =(Button)findViewById(R.id.btn_date);
        preferenceHelper = new PreferenceHelper(this);

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
        tool_title.setText("BG Update");
        //picker.getDatePicker().setMinDate(System.currentTimeMillis());
        //picker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

        // Spinner element
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Device_Bg_Update.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        ArrayList<HashMap<String,String>> com_name = new ArrayList<>();
        HashMap<String,String > map = new HashMap<>();
        map.put("10023","Rosemerta Autotech Private Ltd.");
        com_name.add(map);
        map.put("10034","Fasttrackerz");
        com_name.add(map);
        map.put("12342","Atlanata");
        com_name.add(map);
        map.put("76543","Arya Omnitalk Wireless Solution Pvt Ltd.");
        com_name.add(map);
        map.put("87643","Trimble Mobility Solution Pvt Ltd.");
        com_name.add(map);
        map.put("99817","eTrans Solutions Pvt Ltd.");
        com_name.add(map);
        map.put("54324","Orduino Labs");
        com_name.add(map);
        map.put("34277","CE Info Systems Pvt. Ltd.");
        com_name.add(map);
        map.put("76243","iTriangle Infotech Pvt. Ltd");
        com_name.add(map);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Fasttrackerz");
        categories.add("Rosemerta Autotech Private Ltd.");
        categories.add("Atlanata");
        categories.add("Arya Omnitalk Wireless Solution Pvt Ltd.");
        categories.add("Trimble Mobility Solution Pvt Ltd.");
        categories.add("eTrans Solutions Pvt Ltd.");
        categories.add("Orduino Labs");
        categories.add("CE Info Systems Pvt. Ltd.");
        categories.add("iTriangle Infotech Pvt. Ltd");

        // Creating adapter for spinner
       // ArrayAdapter<HashMap<String,String>> dataAdapter = new ArrayAdapter<HashMap<String, String>>(this, android.R.layout.simple_spinner_item, com_name);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bg_amt_ = bg_deviec_amount.getText().toString().trim();
                String bg_dev_ = bg_device_update.getText().toString().trim();
                new getUpdateStatus().execute(loginid_code,bg_amt_,bg_dev_);
            }
        });
    }

   protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        if(item.equalsIgnoreCase("Fasttrackerz")){
            loginid_code = "10009";

        }else if(item.equalsIgnoreCase("Rosemerta Autotech Private Ltd.")){
            loginid_code = "10002";

        }else if(item.equalsIgnoreCase("Atlanata")){
            loginid_code = "10011";

        }else if(item.equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")){
            loginid_code = "10012";

        }else if(item.equalsIgnoreCase("Trimble Mobility Solution Pvt Ltd.")){
            loginid_code = "10010";

        }else if(item.equalsIgnoreCase("eTrans Solutions Pvt Ltd.")){
            loginid_code = "10364";

        }else if(item.equalsIgnoreCase("Orduino Labs")){
            loginid_code = "87141";

        }else if(item.equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
            loginid_code = "34277";

        }else if(item.equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")){
            loginid_code = "34276";

        }
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

            progressDialog = new ProgressDialog(Device_Bg_Update.this);
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
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +UrlContants.GET_BG_AMOUNT_UPDATE+"?loginid="+args[0]+"&bgamount="+args[1]+"&vehicleallowed="+args[2]);
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
                                    //change_result= jsonObj1.getString("EnableCode");
                                    //countt = countt+1;

                                    Intent inty = new Intent(Device_Bg_Update.this,Device_Manag_admin.class);
                                    inty.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );

                                    startActivity(inty);
                                    finish();
                                    Toast.makeText(Device_Bg_Update.this, jsonObj1.getString("Message"), Toast.LENGTH_SHORT).show();
                                }






                            } catch (Exception ev) {
                                System.out.print(ev.getMessage());
                            }
                        }else{
                            Toast.makeText(Device_Bg_Update.this,"Date Out Of Range .....",Toast.LENGTH_LONG).show();

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
