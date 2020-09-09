package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

import customfonts.MyEditText;
import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;

/**
 * Created by Diwash Choudhary on 2/23/2017.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Map map;
   EditText user_name, password;
    MyTextView login;
    View view;
    TextView tv;
ImageView show_pass;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(LoginActivity.this,"",Toast.LENGTH_LONG);
        intialize();
        tv = (TextView) this.findViewById(R.id.TextView03);

        tv.setSelected(true);

        map = new HashMap();


    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();

    }

    private void intialize() {
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
       // show_pass=(ImageView)findViewById(R.id.show_pass);

        login = (MyTextView) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_names = user_name.getText().toString();
            passwords = password.getText().toString();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading....");


            AndyUtils.showCustomProgressDialog(LoginActivity.this, "Authenticating your account..", false, null);
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
                HttpPost httppost = new HttpPost(UrlContants.LOGIN);

                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                        //JSONObject jsonRes = jsonObj.getJSONObject("response");

                        boolean getStatus = false;
                        try {
                            getStatus = Boolean.parseBoolean(jsonObj.getString("Success"));
                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

                        if (getStatus == true) {

                            String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");


                            //preferenceHelper.putUser_id(getUserId);


                            Toast.makeText(LoginActivity.this, "" + getMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "" + jsonObj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                    progressDialog.dismiss();
                }
            });
        }

    }   /*Testing Asyn same work like Login_info method */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
/*
    private void login_info() {

        String user_name = "orsac_admin";
        String user_password = "orsac@admin321";
        String url = UrlContants.LOGIN_ORSCA + user_name + "&password=" + user_password;
        */
/*map.put("username", user_name.getText().toString());
        map.put("password", password.getText().toString());
        *//*

        // ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        // progressDialog.setMessage("Loding.....");
        // progressDialog.setCancelable(false);
        try {
            AndyUtils.showCustomProgressDialog(LoginActivity.this,
                    "Authenticating Your Account...", false, null);

        } catch (Exception e) {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loding.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        aQuery.progress(null).ajax(url, null, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (isConnection()) {
                    Intent inty = new Intent(LoginActivity.this, NavigationActivity.class);

                    startActivity(inty);
                    // Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    */
/*try {
                        String sucess = object.getString("success");
                        if (sucess.equalsIgnoreCase("true")) {
                            String message = object.getString("message");
                            AndyUtils.removeCustomProgressDialog();
                            finish();
                            Intent inty = new Intent(LoginActivity.this, NavigationActivity.class);

                            startActivity(inty);
                           // Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();
                        } else {
                            Snackbar.make(view, "Login Failed", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*//*

                }

            }
        });

    }
*/

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                // new Authentication().execute();
                HashMap<String , String> map_credential = new HashMap<>();
                map_credential.put("omvtsorsac","Omvts@4444#orsac");
                map_credential.put("diromines","Omvts@diromines");
                map_credential.put("jtdiromines","Omvts@jtdiromines");
                map_credential.put("splsecymines","Omvts@splsecymines");
                map_credential.put("orsac","Omvts@orsac");
                map_credential.put("jtdiromines","Omvts@jtdiromines");
                //
                map_credential.put("RaplAdmin","Adminrapl6432");
                map_credential.put("Admin@Fastrackers","Admin@Fastrackers2912");
                map_credential.put("TrimbleAdmin@","Trimble@Admin8654");
                map_credential.put("Admin@AtlComp","AdminAtlanta@367");
                map_credential.put("Adminaria","Adminaria@Admin098");
                map_credential.put("EtransPvt@Admin","PvttransE@Admin687");
                map_credential.put("AdminITIPL@ITIPL","AdminITIPL@Itriangle654");
                map_credential.put("Admin@MapMyIndia","MMI@cispl532");
                map_credential.put("Orduino@Admin","AdminOrduino@562");
                ////

                for (Map.Entry<String, String> entry : map_credential.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    String u_name = ""+ entry.getKey();
                    String U_password = ""+entry.getValue();
                    String user_names = user_name.getText().toString();
                    String  passwords = password.getText().toString();
                    if (isConnection()) {

                        // login_info();
                        if (user_names.equals(u_name) && passwords.equals(U_password)) {
                            finish();
                            Intent inty = new Intent(LoginActivity.this, NavigationActivity.class);
                            inty.putExtra("username",u_name);
                            startActivity(inty);
                            // Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                finish();
                        }else {
                            Toast.makeText(this, "Wrong UserId or Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No Internet , Please Try After sometime", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent myIntent = new Intent();
                setResult(RESULT_OK, myIntent);
                super.finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
