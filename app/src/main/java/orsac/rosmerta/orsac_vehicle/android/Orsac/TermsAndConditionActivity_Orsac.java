package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

/**
 * Created by Rubal on 08-09-2015.
 */
public class TermsAndConditionActivity_Orsac extends AppCompatActivity {
    private MyTextView tool_title;
    private ImageView tool_back_icon;

    private Toolbar tb;
    private TextView termsConditions;
    private WebView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String mydata_test = "<p><b>INTRODUCTION:</b> This App is published by or on behalf of  Orsac Private Limited, a company incorporated under the Companies Act, 1956 (hereinafter referred to as \\\"Orsac\\\", \\\"we\\\" or \\\"us\\\" which expressions shall, unless it repugnant to the context or meaning hereof, be deemed to mean and include its successors and permitted assigns).</p>  \n" +
                "<p>By downloading or otherwise accessing the App you agree to be bound by the following terms and conditions (\\\"Terms\\\") and our privacy policy and our cookies policy. If you have any queries about the App or these Terms, you can contact us at <i>support@orsac.com</i>. If you do not agree with these Terms, you should stop using the App immediately.</p>" +
                "<p><b>GENERAL RULES RELATING TO CONDUCT:</b> The App is made available for your own, personal use. The App must not be used for any commercial purpose whatsoever or for any illegal or unauthorised purpose. When you use the App you must comply with all applicable laws in India and with any applicable international laws, including the local laws in your country of residence (together referred to as \\\"Applicable Laws\\\").<p><br> \n" +
                "<p>You agree that when using the App you will comply with all Applicable Laws and these Terms. In particular, but without limitation, you agree not to:</p><br>\n" +
                "<p>(a) Use the App in any unlawful manner or in a manner which promotes or encourages illegal activity including (without limitation) copyright infringement; or</p><br>\n" +
                "<p>(b) Attempt to gain unauthorised access to the App or any networks, servers or computer systems connected to the App; or</p><br>\n" +
                "<p>(c) Modify, adapt, translate or reverse engineer any part of the App or re-format or frame any portion of the pages comprising the App, save to the extent expressly permitted by these Terms or by law.</p><br>\n" +
                "<p>You agree to indemnify Orsac in full and on demand from and against any loss, damage, costs or expenses which they suffer or incur directly or indirectly as a result of your use of the App otherwise than in accordance with these Terms or Applicable Laws.</p><br>\n" +
               "<p>Copyright  2017 Orsac, All rights reserved.</p>";

        tool_title.setText("Terms & Conditions");
        terms = (WebView) findViewById(R.id.terms);
        terms.loadData(String.format(htmlText, mydata_test), "text/html", "utf-8");

    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
}
