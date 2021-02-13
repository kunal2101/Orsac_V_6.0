package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.PreferenceHelper;
import orsac.rosmerta.orsac_vehicle.android.R;
import orsac.rosmerta.orsac_vehicle.android.textview.TextView_Lato;
import orsac.rosmerta.orsac_vehicle.android.textview.Textview_lato_thin;
import orsac.rosmerta.orsac_vehicle.android.adapter.PaymentDetailsAdapter;

public class PaymentHistoryActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> getDatalist;
    private RecyclerView mRecyclerView;
    PaymentDetailsAdapter reportHistory ;
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    PreferenceHelper preferenceHelper;
    String etpno = "";
    CardView noPosts;
    TextView_Lato etp_no ;
    Textview_lato_thin imeino,veh_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.payment_history);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setVisibility( View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        etp_no = (TextView_Lato)findViewById(R.id.etp_no);
        imeino =  (Textview_lato_thin)findViewById(R.id.imeino) ;
        veh_no = (Textview_lato_thin)findViewById(R.id.veh_no);
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("Payment History");
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerview);
        noPosts = (CardView)findViewById(R.id.noPosts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager ( PaymentHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
        preferenceHelper = new PreferenceHelper(this);
        getDatalist = new ArrayList<> (  );
        for(int aind = 0 ; aind < 10; aind++){

            HashMap<String,String> map = new HashMap<>();
            map.put("Vehicleno","Vehicleno " +aind );
            map.put("transaction","Transaction Id " + aind);
            map.put("vendor","Vendor Name" + aind);
            map.put("datetime","10-sep-2020 18:23:23");
            //map.put("VehicleRegNo",jsObj.getString("InternalBatteryChargingStatus"));
            // map.put("DateTimeStart",jsObj.getString("MainBatteryStatus"));
            map.put("amount","Amount " + aind);
            map.put("status","Transaction Status " + aind);
          //  map.put("amount","Amount " + aind);



            getDatalist.add(map);
        }

        reportHistory = new PaymentDetailsAdapter (PaymentHistoryActivity.this, getDatalist, mRecyclerView);
        mRecyclerView.setAdapter(reportHistory);
/*
        if (isConnection()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new RouteDetailActivity.getReport ().executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new RouteDetailActivity.getReport ().execute();
            }
        }
*/

    }


    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) PaymentHistoryActivity.this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


}