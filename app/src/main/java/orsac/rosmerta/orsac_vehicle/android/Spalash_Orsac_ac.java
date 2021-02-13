package orsac.rosmerta.orsac_vehicle.android;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static java.lang.Thread.sleep;


/**
 * Created by Diwash Choudhary on 1/30/2017.
 */
public class Spalash_Orsac_ac extends Activity {
    ImageView head;
    ImageView image;
    Thread background;
    boolean is_click=false;
    private boolean isNetDialogShowing = false;

    String deviceId="";
    private android.app.AlertDialog internetDialog;
    RelativeLayout rela_main;
    final static int REQUEST_LOCATION = 199;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE};

    /*,
    Manifest.permission.SEND_SMS,
    Manifest.permission.RECEIVE_SMS*/;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    PreferenceHelper preferenceHelper;
    //SharedPreferences sharedPreferences ;
    TextView tv_orsac,tv_rtl;
    ImageView iv_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trcak_spalsh_orsac_activity);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        AnimationSpalash();

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        rela_main=(RelativeLayout)findViewById(R.id.rela_main);
        TextView tv = (TextView) this.findViewById(R.id.TextView03);
        tv.setSelected(true);
        preferenceHelper = new PreferenceHelper(this);

        if(preferenceHelper.getUser_id().equalsIgnoreCase("null")){
                 ter_con ();
        }
        // enableLoc();
        this.image = (ImageView) findViewById(R.id.splash_Rotate);
        this.head = (ImageView) findViewById(R.id.rl);

        if (ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED
                /*|| ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[5]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[6]) != PackageManager.PERMISSION_GRANTED*/) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[4])
                    /*|| ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this, permissionsRequired[6])*/){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(Spalash_Orsac_ac.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Spalash_Orsac_ac.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(Spalash_Orsac_ac.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Spalash_Orsac_ac.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
            rela_main.performClick();
        }
        rotateImage();

        this.head.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));

       /* rela_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_click=true;
                if(preferenceHelper.getUser_id().equalsIgnoreCase("null")) {
                    ter_con();
                }else {
                    Intent inty = new Intent(getApplication(), SearchVehicleWise.class);
                    startActivity(inty);

                    finish();
                }

            }
        });*/

    }
// this is using when we want animation on splash scrren
    private void AnimationSpalash () {
        tv_orsac = findViewById ( R.id.tv_orsac );
        tv_rtl = findViewById ( R.id.tv_rtl );
        iv_logo = findViewById ( R.id.iv_logo );

        Animation animFadein, animslideup;
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animslideup = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        final AnimationSet s = new AnimationSet(true);
        s.setInterpolator(new AccelerateInterpolator ());
        s.addAnimation(animslideup);
        s.addAnimation(animFadein);
        tv_orsac.startAnimation(s);
        tv_rtl.startAnimation(s);
        iv_logo.startAnimation(s);
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Spalash_Orsac_ac.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }
    public BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);

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

    private void removeInternetDialog() {
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
            isNetDialogShowing = false;
            internetDialog = null;
        }
    }

    private void showInternetDialog() {
        isNetDialogShowing = true;
        android.app.AlertDialog.Builder internetBuilder = new android.app.AlertDialog.Builder(this);
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
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeInternetDialog();
                                //finish();
                                internetDialog.dismiss();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }

    class changeUrl extends AsyncTask<String, String, String> {
        //ProgressDialog progressDialog;
        // String user_names, passwords,last_90_days_st,active_etp,inactive_etp;
        //int total_count =0,total_i3ms=0,total_etp=0;
        // ProgressBar pbHeaderProgress;

        // int total_active=0;
        //int total_inactive = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pbHeaderProgress = (ProgressBar) findViewById(R.id.progress_bar);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            //String getDeviceToken = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://gpstrack.nhmodisha.in/orsacandroidthird/rest/CallService/changeUrl");
                HttpResponse response = httpclient.execute(httpGet);
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
                        JSONObject jsonObj_   =  new JSONObject(getResponse);

                        String changeUrl = jsonObj_.getString("URL");
                        preferenceHelper.putUrls(changeUrl);


                        if (preferenceHelper.getAppInstall() == 0) {
                            if (preferenceHelper.getUrls().equalsIgnoreCase("https://vtscloud.co.in/rtlorsacandroidcloud/rest/CallService")) {
                                Toast.makeText(Spalash_Orsac_ac.this, "Change URL", Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println("Install Count Api called once");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    new install_app().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                else
                                    new install_app().execute();
                                //  new install_app().execute(deviceId);
                            }
                        }
                        //  Toast.makeText(Spalash_Orsac_ac.this,changeUrl+"",Toast.LENGTH_LONG).show();
                        //sleep(5 * 1000);
                        if(preferenceHelper.getUser_id().equalsIgnoreCase("null")) {
                            ter_con();
                        }else {
                            Thread timer= new Thread()
                            {
                                public void run()
                                {
                                    try {
                                        sleep(5000);
                                    }
                                    catch (InterruptedException e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                    finally {

                                        Intent inty = new Intent(getApplication(), SearchVehicleWise.class);
                                        startActivity(inty);
                                        finish();
                                    }
                                }
                            };
                            timer.start();
                        }

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    //  pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        }

    }


    class install_app extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String macAddress = null;
            try {

                 macAddress = android.provider.Settings.Secure.getString(Spalash_Orsac_ac.this.getApplicationContext().getContentResolver(), "android_id");

               // Toast.makeText(Spalash_Orsac_ac.this, "Mac Address " + macAddress, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/setFirstTimeInstall?data1="+macAddress+"&data2=OMVTS1");

                HttpResponse response = httpclient.execute(httpGet);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {

                    try {
                        JSONObject jsonObj_   =  new JSONObject(getResponse);


                        String status = jsonObj_.getString("Status");  //29336


                        int count = Integer.parseInt(jsonObj_.getString("Message")) + 51680 ;


                        //downloadcount(count);
                        Toast.makeText(Spalash_Orsac_ac.this, " Total Download --  "+count, Toast.LENGTH_LONG).show();

                        preferenceHelper.putAppInstall(1);

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }

                }
            });
        }

    }

    /* private void enableLoc() {

         if (googleApiClient == null) {
             googleApiClient = new GoogleApiClient.Builder(this)
                     .addApi(LocationServices.API)
                     .addConnectionCallbacks(this)
                     .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                         @Override
                         public void onConnectionFailed(ConnectionResult connectionResult) {
                         }
                     }).build();
             googleApiClient.connect();

             LocationRequest locationRequest = LocationRequest.create();
             locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

             LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

             builder.setAlwaysShow(true);

             PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
             result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                 @Override
                 public void onResult(LocationSettingsResult result) {
                     final Status status = result.getStatus();
                     switch (status.getStatusCode()) {
                         case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                             try {
                                 // Show the dialog by calling startResolutionForResult(),
                                 // and check the result in onActivityResult().

                                 status.startResolutionForResult((Activity) Spalash_Orsac_ac.this, REQUEST_LOCATION);
                             } catch (IntentSender.SendIntentException e) {
                                 // Ignore the error.
                             }
                             break;
                     }
                 }
             });

         }
     }
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnectionReciever);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(internetConnectionReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    background = new Thread() {
                        public void run() {

                            try {
                                sleep(5 * 1000);


                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }
                            finally {
                                if(preferenceHelper.getUser_id().equalsIgnoreCase("null")) {
                                    ter_con();
                                }else {
                                    Intent inty = new Intent(getApplication(), SearchVehicleWise.class);
                                    startActivity(inty);

                                    finish();
                                }
                            }
                        }
                    };

                    // start thread

                    background.start();
                    break;
                case Activity.RESULT_CANCELED:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[4])
                   /* || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Spalash_Orsac_ac.this,permissionsRequired[6])*/){
                //txtPermissions.setText("Permissions Required");
                AlertDialog.Builder builder = new AlertDialog.Builder(Spalash_Orsac_ac.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Spalash_Orsac_ac.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    private void proceedAfterPermission() {

        background = new Thread() {
            public void run() {
                try {
                 /*   TelephonyManager telephonyManager;
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    if (ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                 *///   deviceId = telephonyManager.getDeviceId();

                    if (isConnection()) {
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                            new changeUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }

                            else{
                            new changeUrl().execute();
                        }
                    } else {
                        Toast.makeText(Spalash_Orsac_ac.this, "No Internet , Please Try After sometime", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }
            }

        };
        background.start();
    }
    public void ter_con(){
        // boolean agreed = sharedPreferences.getBoolean("agreed",false)if (!agreed) {
        new AlertDialog.Builder(Spalash_Orsac_ac.this)
                .setTitle(getResources().getString(R.string.call))
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           /* SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("agreed", true);*/
                        Thread timer= new Thread()
                        {
                            public void run()
                            {
                                try {
                                    sleep(5000);
                                }
                                catch (InterruptedException e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                finally {
                                    preferenceHelper.putUser_id("true");
                                    //editor.commit();

                                    Intent inty = new Intent(getApplication(), SearchVehicleWise.class);
                                    startActivity(inty);

                                    finish();
                                }
                            }
                        };
                        timer.start();

                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setMessage(getResources().getString(R.string.term))
                .setCancelable(false)
                .show();
        // }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Spalash_Orsac_ac.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    public void rotateImage() {
        Animation animationToLeft = new TranslateAnimation(200.0f, -200.0f, 0.0f, 0.0f);
        animationToLeft.setDuration(10000);
        animationToLeft.setRepeatMode(2);
        animationToLeft.setRepeatCount(-1);
        this.image.setAnimation(animationToLeft);
    }

    /*@Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/
}
