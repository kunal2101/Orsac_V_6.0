package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;
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

import customfonts.MyEditText;
import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;

public class PaymentRenewalPage extends AppCompatActivity {
    String vehicleNo,mobNo;
    PreferenceHelper preferenceHelper;
    TextView txt_sim_vali,txt_comname;
    MyEditText et_email;
    private ImageView tool_back_icon;
    MyTextView btn_back,mytv_next;
    AppCompatEditText  et_imeino,et_vehicleNo,et_mob;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.paymnet_renewal );
        preferenceHelper = new PreferenceHelper(this);
        txt_sim_vali = findViewById ( R.id.txt_sim_vali );
        txt_comname  = findViewById ( R.id.txt_comname );
        et_mob       = findViewById ( R.id.et_mob );
        et_vehicleNo       = findViewById ( R.id.et_vehicleNo );
        et_imeino       = findViewById ( R.id.et_imeino );
        et_email       = findViewById ( R.id.et_email );
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        btn_back       =   findViewById ( R.id.btn_back );
        mytv_next      =   findViewById ( R.id.mytv_next );

        try{
            vehicleNo = getIntent ().getStringExtra ( "vehicleNo" );
            mobNo = getIntent ().getStringExtra ( "mobNo" );
            et_mob.setText ( mobNo );
            new Authentication ().execute (  );
        }catch (Exception e){
            e.getMessage ();
        }
        mytv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent ( PaymentRenewalPage.this,PaymentResult.class );
                inty.putExtra ( "email",et_email.getText ().toString () );
                inty.putExtra ( "company",txt_comname.getText ().toString () );
                inty.putExtra ( "vehicleNo",vehicleNo );

                startActivity ( inty );
                finish ();
            }
        });

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });



    }
    class SimValidilty extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/getsimvalidity?vehicleno="+vehicleNo);
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
                        try {
                            // getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                            txt_sim_vali.setText ( "Sim Validity \n"+jsonObj.optString("simvalidity")  );

                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    // progressDialog.dismiss();
                }
            });
        }

    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(PaymentRenewalPage.this,
                    "Loading...", false, null);
         }

        protected String doInBackground(String... args) {

            String getResponse = null;
            try {
                HttpClient httpclient = new DefaultHttpClient ();
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+ "getlivevehicles?vehicleno=" + vehicleNo);
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
                    try {
                        JSONArray jsonArray = new JSONArray(getResponse);
                        if(jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);


                            txt_comname.setText(jsonObject.getString("company"));
                            et_vehicleNo.setText("  "+jsonObject.getString("vehical_no")+"  ");
                            et_imeino.setText(jsonObject.getString("unitid"));

                            new SimValidilty ().execute();

                        }else {
                            Toast.makeText(PaymentRenewalPage.this, "Invalid Vehicle Number/IMEI No..", Toast.LENGTH_LONG).show();
                            onbackClick();
                        }

                        AndyUtils.removeCustomProgressDialog();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                }
            });
            AndyUtils.removeCustomProgressDialog();
        }
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }


}