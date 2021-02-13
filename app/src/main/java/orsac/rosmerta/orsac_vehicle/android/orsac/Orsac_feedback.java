package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.regex.Pattern;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by KK on 4/15/2017.
 */
public class Orsac_feedback extends AppCompatActivity {
    EditText feedbackEdittext, feedbackName, feedbackEmail, feedbackNum;
    Button button;
    private MyTextView tool_title;
    private ImageView tool_back_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackEdittext = (EditText) findViewById(R.id.feedbackEdittext);
        feedbackName = (EditText) findViewById(R.id.feedbackName);
        feedbackEmail = (EditText) findViewById(R.id.feedbackEmail);
        feedbackNum = (EditText) findViewById(R.id.feedbackNum);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText("Feedback");
        try {
            String inty_send = getIntent().getExtras().getString("send");
            if (inty_send.equalsIgnoreCase("1")) {
                finish();
            }
        }catch (Exception e){
            e.getMessage();
        }

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new SensEmail().execute();

            }
        });
        TelephonyManager tMgr = (TelephonyManager) Orsac_feedback.this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_NUMBERS") != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String mPhoneNumber = tMgr.getLine1Number();
      //  Toast.makeText(this, ""+mPhoneNumber, Toast.LENGTH_SHORT).show();
        feedbackNum.setText(mPhoneNumber);
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    public boolean validateUser() {

        final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
        boolean isValid = true;

        feedbackEdittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        String fullName = feedbackEdittext.getText().toString();
        if ("".equals(fullName) && !Pattern.compile(USERNAME_PATTERN).matcher(fullName).matches() ) {
            feedbackEdittext.setError("Please enter your Feedback");
            isValid = false;
        }/*else if (!Pattern.compile(USERNAME_PATTERN).matcher(fullName).matches()) {
            feedbackName.setError("Please enter your Full Name");
            isValid = false;
        }*/
        if ("".equals(feedbackName.getText().toString())) {
            feedbackName.setError("Please enter your Full Name");
            isValid = false;
        }
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        String email = feedbackEmail.getText().toString();
       /* if ("".equals(email)) {
            feedbackEmail.setError("Please enter a valid email address");
            isValid = false;
        } else if (!Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
            feedbackEmail.setError("Please enter a valid email address");
            isValid = false;
        }*/

        String number = feedbackNum.getText().toString();

        if ("".equals(number)) {
            feedbackNum.setError("Please enter valid phone number");
            isValid = false;
        } else if (number.length() < 10) {
            feedbackNum.setError("Number must be 10 digits long.");
            isValid = false;
        }

        return isValid;
    }
    class SensEmail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String user_names, passwords;
        String getResponse = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AndyUtils.showCustomProgressDialog(Orsac_feedback.this,
                    "Sending Msg Please wait...", false, null);

        }

        protected String doInBackground(String... args) {


            try {
                HttpClient httpclient = new DefaultHttpClient();
                //  HttpPost httppost = new HttpPost(UrlContants.DASHBORAD);
                //OD35B1747
                String st_msg= "Sender Name -"+feedbackName.getText().toString()+
                        "  "+"Sender Phone Number -"+feedbackNum.getText().toString()+"   Meassage-"+feedbackEdittext.getText().toString();
              st_msg= st_msg.replace(" ","%20");

                HttpGet httppost = new HttpGet("http://103.76.212.115/rtlorsacandroid/rest/CallService/sendmail?to=imkunal2101@gmail.com,orsactechhelpdesk@rosmertatech.com,orsacmvts@gmail.com,orsacomvts@gmail.com&sub=Orsac%20Feedback&body="+st_msg);
                // Add your data
                //  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                        JSONObject jsonObj = new JSONObject(getResponse);
                        try {
                            Toast.makeText(Orsac_feedback.this, ""+jsonObj.getString("Message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }


                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    AndyUtils.removeCustomProgressDialog();
                    // progressDialog.dismiss();
                }
            });
        }

    }

    protected void sendEmail() {
    /* try {
         GMailSender sender = new GMailSender("rtl.itdeveloper28@gmail.com", "Rosmerta@123");
         sender.sendMail("This is Subject",
                 "This is Body",
                 "imkunal2101@gmail.com",
                 "kunalpathak59@gmail.com");
     }catch (Exception e){
         e.getMessage();
     }*/
        /*String[] TO = {"orsactechhelpdesk@rosmertatech.com"};
        String[] CC = {"orsacmvts@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Orsac App Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sender Name -"+feedbackName.getText().toString()+
                "  "+"Sender Phone Number -"+feedbackNum.getText().toString()+"   Meassage-"+feedbackEdittext.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Orsac_feedback.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }*/
    }
}
