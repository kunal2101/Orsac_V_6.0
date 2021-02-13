package orsac.rosmerta.orsac_vehicle.android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import customfonts.MyEditText;
import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.ActivityPaymentWebView;
import orsac.rosmerta.orsac_vehicle.android.R;

import static android.content.ContentValues.TAG;

public class Fragment_New_Device_Purchas extends Fragment {

    ImageView pimage;
    TextView txt_comname;
    MyTextView txt_next;
    MyEditText myEdit_no_devcie;
    CheckBox chk_amt;

    String com_name;
    String payment_url;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_payment__details, container, false);

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_comname = (TextView)view.findViewById(R.id.txt_comname);
        txt_next    =  (MyTextView)view.findViewById(R.id.txt_next);
        chk_amt     = (CheckBox)view.findViewById(R.id.chk_amount);
        myEdit_no_devcie =(MyEditText)view.findViewById(R.id.myEdit_no_devcie);
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
            payment_url = "https://trackofy.com/orsac_device/orsacRenewalRecharge.php";
            chk_amt.setText(" I agree with Price of One Device (MRP in Rs. 8490)");

        }else if(com_name.equalsIgnoreCase("CE Info Systems Pvt. Ltd.")){
            payment_url = "https://www.mapmyindia.com/store/one-year-subscription-basic-plan-with-sim-buy-and-review";
        }else if(com_name.equalsIgnoreCase("Rosmerta Autotech Private Ltd.")){
            payment_url = "http://vts.orissaminerals.gov.in/orsacecomm";
            chk_amt.setText(" I agree with Price of One Device (MRP in Rs. 9500)");

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



        txt_comname.setText(com_name);






    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


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
