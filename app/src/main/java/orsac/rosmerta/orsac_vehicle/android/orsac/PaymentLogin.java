package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

public class PaymentLogin extends AppCompatActivity {
    AppCompatEditText et_mob , et_otp;
    Button btn_start;
    private ImageView tool_back_icon;
    String vehicleNo;
    MyTextView btn_history;
     @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.payment_login );

        et_mob = findViewById ( R.id.et_mob );
        et_otp = findViewById ( R.id.et_otp );
        btn_start = findViewById ( R.id.btn_start );
         tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
         btn_history = findViewById ( R.id.btn_history );

      try{
          vehicleNo = getIntent ().getStringExtra ( "vehicleno" );
      }catch (Exception e){

      }

        btn_start.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                if(btn_start.getText ().toString ().equalsIgnoreCase ( "NEXT" )) {
                    if (et_mob.getText ( ).length () == 0) {
                        Toast.makeText ( PaymentLogin.this , "Please Enter your Mobile No" , Toast.LENGTH_SHORT ).show ( );

                    } else if (et_mob.getText ( ).length () < 10) {
                        Toast.makeText ( PaymentLogin.this , "Invalid Mobile No" , Toast.LENGTH_SHORT ).show ( );

                    } else if (et_mob.getText ( ).length () == 10) {
                        et_otp.setVisibility ( View.VISIBLE );
                        btn_start.setText ( "SUBMIT" );
                        btn_history.setVisibility ( View.VISIBLE );
                    }
                }else  if(btn_start.getText ().toString ().equalsIgnoreCase ( "SUBMIT" )){
                    if (et_otp.getText ( ).length () == 0) {
                        Toast.makeText ( PaymentLogin.this , "Please Enter OTP" , Toast.LENGTH_SHORT ).show ( );

                    } else if (et_otp.getText ( ).length () < 4) {
                        Toast.makeText ( PaymentLogin.this , "Invalid OTP" , Toast.LENGTH_SHORT ).show ( );

                    } else if (et_otp.getText ( ).length () == 4) {

                        Intent inty = new Intent ( PaymentLogin.this,PaymentRenewalPage.class );
                        inty.putExtra ( "vehicleNo",vehicleNo );
                        inty.putExtra ( "mobNo",et_mob.getText ().toString () );
                        startActivity ( inty );
                        finish ();
                    }
                }

            }
        } );
         btn_history.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(btn_start.getText ().toString ().equalsIgnoreCase ( "SUBMIT" )){
                     if (et_otp.getText ( ).length () == 0) {
                         Toast.makeText ( PaymentLogin.this , "Please Enter OTP" , Toast.LENGTH_SHORT ).show ( );

                     } else if (et_otp.getText ( ).length () < 4) {
                         Toast.makeText ( PaymentLogin.this , "Invalid OTP" , Toast.LENGTH_SHORT ).show ( );

                     } else if (et_otp.getText ( ).length () == 4) {

                         Intent inty = new Intent ( PaymentLogin.this,PaymentHistoryActivity.class );

                         startActivity ( inty );
                         finish ();
                     }
                 }
             }
         });

         tool_back_icon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onbackClick();
             }
         });



     }


    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

}