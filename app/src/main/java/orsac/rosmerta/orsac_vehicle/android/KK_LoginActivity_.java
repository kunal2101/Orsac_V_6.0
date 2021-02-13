package orsac.rosmerta.orsac_vehicle.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.orsac.AndyUtils;

/**
 * Created by Diwash Choudhary on 2/23/2017.
 */
public class KK_LoginActivity_ extends AppCompatActivity implements View.OnClickListener {

    Map map;
    EditText user_name, password;
    Button login;
    View view;
    TextView tv;
    private Boolean exit = false;
    private MyTextView tool_title;
   boolean countss = true ;
   PreferenceHelper preferenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_login_);
        Toast.makeText(this, "ADMIN PAGE", Toast.LENGTH_LONG).show();
        intialize();
        preferenceHelper = new PreferenceHelper(this);

        try{
            if (preferenceHelper.getUsername().length()>0 && preferenceHelper.getUserPass().length()>0 ){
                user_name.setText(preferenceHelper.getUsername());
                password.setText(preferenceHelper.getUserPass());

            }
        }catch (Exception ev){
        System.out.print(ev.getMessage());
        }


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
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (countss) {
                            if (password.getText().toString().length() == 0) {
                                Toast.makeText(KK_LoginActivity_.this, "Kindly Insert the Password..", Toast.LENGTH_SHORT).show();
                            } else {


                                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                countss = false;
                            }
                            return true;

                        } else {

                            password.setInputType(129);
                            countss = true;
                        }
                    }
                }

                     /*   if(event.getRawX() <= (password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                {
                    // your action here
                    Toast.makeText(KK_LoginActivity_.this, "click left", Toast.LENGTH_SHORT).show();
                    return true;
                }*/
                return false;
            }
        });

        password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_names = user_name.getText().toString().trim();
            passwords = password.getText().toString().trim();
            progressDialog = new ProgressDialog(KK_LoginActivity_.this);
            progressDialog.setMessage("Loading....");


            AndyUtils.showCustomProgressDialog(KK_LoginActivity_.this, "Authenticating your account..", false, null);
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
                HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+UrlContants.OMVTS_LOGIN+"?u="+user_names+"&p="+passwords);

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
                        JSONObject jsonObj = new JSONObject(getResponse);
                        //JSONObject jsonRes = jsonObj.getJSONObject("response");

                        boolean getStatus = false;
                        try {
                            //getStatus = Boolean.parseBoolean(jsonObj.getString("success"));
                            getStatus = jsonObj.getBoolean("success");
                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

                        if (getStatus) {

                           /* String getMessage = jsonObj.getString("Message");
                            JSONArray arrData = jsonObj.getJSONArray("VehicleDetails");
                            JSONObject jsonData = arrData.getJSONObject(0);

                            String getUserId = jsonData.getString("userid");*/

                           String username = jsonObj.getString("u");
                           String pass = jsonObj.getString("p");



                           preferenceHelper.putUsername(username);
                            preferenceHelper.putUserpass(pass);

                            progressDialog.dismiss();
                            Intent inty = new Intent(KK_LoginActivity_.this, Otp_Verify.class);
                            inty.putExtra("username",user_name.getText().toString().trim());
                            startActivity(inty);
                            // Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            //finish();


                            //preferenceHelper.putUser_id(getUserId);


                           // Toast.makeText(KK_LoginActivity_.this, "" + getMessage, Toast.LENGTH_LONG).show();
                           /* Intent intent = new Intent(KK_LoginActivity_.this, AdminPage.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            progressDialog.dismiss();
                            AndyUtils.removeCustomProgressDialog();
                            Toast.makeText(KK_LoginActivity_.this, "Wrong Credentials" , Toast.LENGTH_LONG).show();
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

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) KK_LoginActivity_.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

                if (isConnection()){
                    String u  = user_name.getText().toString().trim();
                    String pp =  password.getText().toString().trim();
                    if (user_name.getText().toString().trim().equalsIgnoreCase("8328961595") && password.getText().toString().trim().equalsIgnoreCase("Orsac@123") ){
                        new Authentication().execute();

                    }else {
                        Toast.makeText(this, "Invalid UserName  Password", Toast.LENGTH_SHORT).show();
                    }
                }
                // new Authentication().execute();
              /*  boolean status = false;

                    HashMap<String, String> map_credential = new HashMap<>();
                    map_credential.put("omvtsorsac", "Omvts@4444#orsac");
                    map_credential.put("diromines", "Omvts@diromines");
                    map_credential.put("jtdiromines", "Omvts@jtdiromines");
                    map_credential.put("splsecymines", "Omvts@splsecymines");
                    map_credential.put("orsac", "Omvts@orsac");
                    map_credential.put("jtdiromines", "Omvts@jtdiromines");

                    ////
                    map_credential.put("RaplAdmin", "Adminrapl6432");
                    map_credential.put("Admin@Fastrackers", "Admin@Fastrackers2912");
                    map_credential.put("TrimbleAdmin@", "Trimble@Admin8654");
                    map_credential.put("Admin@AtlComp", "AdminAtlanta@367");
                    map_credential.put("Adminaria", "Adminaria@Admin098");
                    map_credential.put("EtransPvt@Admin", "PvttransE@Admin687");
                    map_credential.put("AdminITIPL@ITIPL", "AdminITIPL@Itriangle654");
                    map_credential.put("Admin@MapMyIndia", "MMI@cispl532");
                    map_credential.put("Orduino@Admin", "AdminOrduino@562");
                    map_credential.put("ab", "ab");
                    //
                    for (Map.Entry<String, String> entry : map_credential.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        String u_name = "" + entry.getKey();
                        String U_password = "" + entry.getValue();
                        String user_names = user_name.getText().toString().trim();
                        String passwords = password.getText().toString().trim();


                        // login_info();


                        if (user_names.equals(u_name) && passwords.equals(U_password)) {
                            status = true;

                            preferenceHelper.putUsername(user_names);
                            preferenceHelper.putUserpass(passwords);
                            break;

                        }
                    }

                if(status){
                  //  finish();
                    Intent inty = new Intent(KK_LoginActivity_.this, NavigationActivity.class);
                    inty.putExtra("username",user_name.getText().toString().trim());
                    startActivity(inty);
                    // Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    //finish();

                }else {
                    Toast.makeText(this, "Wrong UserId or Password", Toast.LENGTH_SHORT).show();
                }*/

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
