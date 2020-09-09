package orsac.rosmerta.orsac_vehicle.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import customfonts.MyTextView;

public class ActivityPaymentWebView extends AppCompatActivity {
    private MyTextView tool_title;
    private ImageView tool_back_icon;
    private WebView webview_payment;

    String paymeny_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        webview_payment = (WebView)  findViewById(R.id.webview_payment);
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        tool_title.setText("Payment");

        try {
            Intent inty = getIntent();
            paymeny_url = inty.getStringExtra("payment_url");

        }catch (Exception e){
            e.getMessage();
        }
        webview_payment.loadUrl(paymeny_url);
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

}
