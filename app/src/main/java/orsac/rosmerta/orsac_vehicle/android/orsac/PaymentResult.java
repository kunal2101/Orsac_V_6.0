package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import orsac.rosmerta.orsac_vehicle.android.R;

public class PaymentResult extends AppCompatActivity {
   ImageView btn_share;
   String email,com_name;
   Button btn_finish;
   TextView tv_scoress,tv_username;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.payment_result );
        tv_scoress = findViewById ( R.id.tv_scoress );
        tv_username = findViewById ( R.id.tv_username );
        try {
            email = getIntent ().getStringExtra ( "email" );
            com_name = getIntent ().getStringExtra ( "company" );

            tv_scoress.setText ( com_name );
            tv_username.setText ( getIntent ().getStringExtra ( "vehicleNo" ) );

        }catch (Exception e){
            e.getMessage ();
        }
        btn_share = findViewById ( R.id.btn_share );
        btn_finish = findViewById ( R.id.btn_finish );
        btn_finish.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                finish ();
            }
        } );
        btn_share.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                sendmail ();
            }
        } );

    }
    public  void sendmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {email};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, " Transaction Detail");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "This is your  Transaction  detail of   \n Please find the Deatil for Your Reference ");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

}