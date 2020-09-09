package orsac.rosmerta.orsac_vehicle.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;

public class Otp_Verify  extends AppCompatActivity {
    private Button done, resend;
    private TextView smsText, timeText;
    private EditText otpCode;
    String phoneNumber;

    private Map map;
    private ProgressDialog progress;
    private AlertDialog internetDialog;
    private boolean isNetDialogShowing = false, isRecieverRegistered= false;
    int total=10;
    ProgressBar bar;
    String otp;
    // String messageText;
    String PINString;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__verify);
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
        tool_title.setText("OTP Verification");
        initview();

        generatePIN();
        new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeText.setText("Time remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timeText.setText("Click on Resend button to re-generate the OTP !");
                resend.setBackgroundResource(R.drawable.bg_primary_button);

                resend.setEnabled(true);
            }

        }.start();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (otpCode.getText().toString().trim().equalsIgnoreCase(PINString)) {
                        Intent inty = new Intent(Otp_Verify.this,Device_Manag_admin.class);
                        startActivity(inty);
                        Toast.makeText(Otp_Verify.this, "OTP Match", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Otp_Verify.this, "OTP Don't Match", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }

            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePIN();
            }
        });
    }
    public void generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        //randomPIN.toString(PINString);
          PINString = String.valueOf(randomPIN);
       // Toast.makeText(this, ""+PINString, Toast.LENGTH_SHORT).show();
        new Authentication().execute();
    }
    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    private void initview() {
        timeText = (TextView) findViewById(R.id.timeText);
        smsText = (TextView) findViewById(R.id.smsText);
        otpCode = (EditText) findViewById(R.id.editTextCode);
        smsText.setText("We have send a SMS with OTP : 8328961595"  );

        done = (Button) findViewById(R.id.done);
        resend = (Button) findViewById(R.id.resend);
        done.setEnabled(false);
        resend.setEnabled(false);

        final TextWatcher mTextEditorWatcher = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    done.setBackgroundResource(R.drawable.bg_disabled_button);
                    done.setEnabled(false);


                } else if (s.length() == 4) {
                    done.setBackgroundResource(R.drawable.bg_primary_button);
                    done.setEnabled(true);

                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
        otpCode.addTextChangedListener(mTextEditorWatcher);

    }
    /*  public void recivedSms(String message)
      {
          try
          {
       otp= message;
              otpCode.setText(otp);
          }
          catch (Exception e)
          {

          }
      }*/
    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Otp_Verify.this);
            progressDialog.setMessage("Loading....");


            AndyUtils.showCustomProgressDialog(Otp_Verify.this, "Authenticating your account..", false, null);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
//

            try {
                HttpClient httpclient = new DefaultHttpClient();
                // HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/omvtslogin?u="+user_names+"&p="+passwords);
                HttpGet httppost = new HttpGet("http://otp.smseasy.in/api/mt/SendSMS?user=nhmvts&password=nhmvts&senderid=WEBSMS&channel=trans&DCS=0&flashsms=0&number=7982050330&text=" + "Your%20ORSAC%20ADMIN%20OTP%20is%20"+PINString);

                // Add your data
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                progressDialog.dismiss();
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

                    progressDialog.dismiss();
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(internetConnectionReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        isRecieverRegistered= true;
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (isRecieverRegistered) {
            unregisterReceiver(internetConnectionReciever);

        }

    }

    public BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo activeWIFIInfo = connectivityManager
                    .getNetworkInfo(connectivityManager.TYPE_WIFI);

            if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
                removeInternetDialog();
            } else {
                if (isNetDialogShowing) {
                    return;
                }
                showInternetDialog();
            }
        }
    };
    private void showInternetDialog() {
        //AndyUtils.removeCustomProgressDialog();
        isNetDialogShowing = true;
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(
                Otp_Verify.this);
        internetBuilder.setCancelable(false);
        internetBuilder
                .setTitle(getString(R.string.dialog_no_internet))
                .setMessage(getString(R.string.dialog_no_inter_message))
                .setPositiveButton(getString(R.string.dialog_enable_3g),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(
                                        Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                removeInternetDialog();
                            }
                        })
                .setNeutralButton(getString(R.string.dialog_enable_wifi),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // User pressed Cancel button. Write
                                // Logic Here
                                startActivity(new Intent(
                                        Settings.ACTION_WIFI_SETTINGS));
                                removeInternetDialog();
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeInternetDialog();
                                finish();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }
    private void removeInternetDialog() {
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
            isNetDialogShowing = false;
            internetDialog = null;

        }
    }
}

