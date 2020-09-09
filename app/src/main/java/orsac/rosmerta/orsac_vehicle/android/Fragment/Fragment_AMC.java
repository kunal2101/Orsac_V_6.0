package orsac.rosmerta.orsac_vehicle.android.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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
import orsac.rosmerta.orsac_vehicle.android.ActivityPaymentWebView;
import orsac.rosmerta.orsac_vehicle.android.Orsac.AndyUtils;
import orsac.rosmerta.orsac_vehicle.android.Orsac.VehicelWise_Deatail;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.SearchVehicleWise;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.internal.zzhl.runOnUiThread;

public class Fragment_AMC extends Fragment {

    ImageView pimage;
    TextView txt_comname,txt_search;
    MyTextView txt_next;
    MyEditText myEdit_no_devcie,edittxt_vehicleNo;
    CheckBox chk_amt;

    String com_name;
    String payment_url;
    String st_customer,st_lastpolling,st_deviceid,st_imeino,st_simno,st_mob1,st_mob2,st_dealer,st_vehicleno;
    PreferenceHelper preferenceHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_amc__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_comname = (TextView)view.findViewById(R.id.txt_comname);
        txt_search  =  (TextView)view.findViewById(R.id.txt_search);
        txt_next    =  (MyTextView)view.findViewById(R.id.txt_next);
        chk_amt     = (CheckBox)view.findViewById(R.id.chk_amount);
        myEdit_no_devcie =(MyEditText)view.findViewById(R.id.myEdit_no_devcie);
        edittxt_vehicleNo=(MyEditText)view.findViewById(R.id.edittxt_vehicleNo);
        preferenceHelper = new PreferenceHelper(getActivity());
        if(com_name.equalsIgnoreCase("eTrans Solutions Private Limited")){
            payment_url = "https://etranssolutions.com/report/salesPaymentPage?pageid=S410V";
            chk_amt.setText(" I agree with Price of One Device (MRP in Rs. 9500)");

        }else if(com_name.equalsIgnoreCase("Fastrackerz")){
            payment_url = "http://odishamines.fastrackerz.in/";

        }else if(com_name.equalsIgnoreCase("iTriangle Infotech Pvt. Ltd")){
            payment_url =  "https://www.instamojo.com/@ItriangleImz/";

        }else if(com_name.equalsIgnoreCase("Orduino Labs")){
            payment_url ="NA";
        }else if(com_name.equalsIgnoreCase("Arya Omnitalk Wireless Solution Pvt Ltd.")){
            payment_url ="NA";
        }else if(com_name.equalsIgnoreCase("Atlanta")){
            payment_url = "https://trackofy.com/orsac_device/orsacRenewalRecharge.php?action=device_renewal";
            chk_amt.setText(" I agree with Price of One Device (Approx MRP in Rs. 8490)");


        }else if(com_name.equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
            payment_url = "https://www.mapmyindia.com/store/one-year-subscription-basic-plan-with-sim-buy-and-review";
        }else if(com_name.equalsIgnoreCase("Rosmerta Autotech Private Ltd.")){
            payment_url = "http://vts.orissaminerals.gov.in/#/amc";
            chk_amt.setText(" I agree with Price of One Device (Approx MRP in Rs. 8000)");

        }else if(com_name.equalsIgnoreCase("Trimble Mobility Solutions India Pvt. Ltd.")){
            payment_url ="NA";
        }
        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chk_amt.isChecked()){
                    Intent intent = new Intent(getActivity(), ActivityPaymentWebView.class);
                    intent.putExtra("payment_url",payment_url);
                    startActivity(intent);


                }else {
                    Toast.makeText(getContext(), "Kindly Accept the Amount provided by "+com_name+" .....", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edittxt_vehicleNo.getText().toString().length()>5) {
                    new Authentication().execute();
                }
            }
        });



        txt_comname.setText(com_name);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent inty = getActivity().getIntent();
            com_name = inty.getStringExtra("com_name");
        }catch (Exception e){
            e.getMessage();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }



    public  void DialogVehicleDetail(){
        final Dialog myDialog = new Dialog(getActivity(), R.style.NewDialog);
        myDialog.getWindow();



        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.detail_page);
        TextView txt_customer = (TextView) myDialog.findViewById(R.id.txt_customer);
        TextView txt_imeino = (TextView) myDialog.findViewById(R.id.txt_imeino);
        TextView txt_simno = (TextView) myDialog.findViewById(R.id.txt_simno);
        TextView txt_mob_1 = (TextView) myDialog.findViewById(R.id.txt_mob_1);
        TextView txt_mob_2 = (TextView) myDialog.findViewById(R.id.txt_mob_2);
        TextView txt_vehno = (TextView) myDialog.findViewById(R.id.txt_vehNo);
        TextView txt_dealer = (TextView) myDialog.findViewById(R.id.txt_dealer);
        TextView txt_lastpolling = (TextView) myDialog.findViewById(R.id.txt_lastpolling);
        TextView txt_submit = (TextView) myDialog.findViewById(R.id.txt_submit);
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
        // txt_customer.setText(st_customer);
        txt_imeino.setText("ImeiNo :- " + st_imeino);
        txt_simno.setText(st_customer);
        txt_mob_1.setText(st_mob1);
        txt_mob_2.setText(st_mob2);
        txt_vehno.setText(st_vehicleno);
        txt_dealer.setText(st_dealer);
        txt_lastpolling.setText(st_lastpolling);








        myDialog.show();

    }
    class Authentication extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AndyUtils.showCustomProgressDialog(getActivity(),
                    "Loading...", false, null);
          }

        protected String doInBackground(String... args) {

            String getResponse = null;
            String getDeviceToken = null;
            String vehicleNo =  edittxt_vehicleNo.getText().toString().toUpperCase();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=49179
                //HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getlivevehicles?vehicleno=" + etpno);
               // HttpGet httppost = new HttpGet(preferenceHelper.getUrls() +"/"+ "getVehicledetails?vehicleno=" + "OR11J6391");
                HttpGet httppost = new HttpGet("http://vts.orissaminerals.gov.in/orsacecomm/rest/CallService/getVehicledetails?vehicleno="+vehicleNo);

//http://vts.orissaminerals.gov.in/orsacecomm/rest/CallService/getVehicledetails?vehicleno=OR11J6391
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
                    try {
                        JSONArray jsonArray = new JSONArray(getResponse);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                       if(jsonObject.has("success")) {
                           Toast.makeText(getActivity(), "Invalid Vehicle Number.", Toast.LENGTH_LONG).show();
                       }else {
                           st_customer = jsonObject.getString("customer");
                           st_lastpolling = jsonObject.getString("lastpolling");
                           st_deviceid = jsonObject.getString("deviceid");
                           st_imeino = jsonObject.getString("imeino");
                           st_simno = jsonObject.getString("simno");
                           st_mob1 = jsonObject.getString("mob1");
                           st_mob2 = jsonObject.getString("mob2");
                           st_dealer = jsonObject.getString("dealer");
                           st_vehicleno = jsonObject.getString("vehicleno");
                           DialogVehicleDetail();

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



    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Onstart Call ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume Call ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "OnStop Call ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause Call ");
    }

}
