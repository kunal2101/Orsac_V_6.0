package orsac.rosmerta.orsac_vehicle.android.orsac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import orsac.rosmerta.orsac_vehicle.android.R;

public class PdfWebView extends AppCompatActivity {
WebView webview;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_pdf_web_view );
        webview = findViewById ( R.id.webView );
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl("http://vts.odishaminerals.gov.in:8022/download_pdf/onehourpdf.pdf/");
        //webview.loadUrl("https://www.journaldev.com/9333/android-webview-example-tutorial");
    }
    String getPdfUrl(){
        return "http://vts.odishaminerals.gov.in:8022/download_pdf/onehourpdf.pdf/";
    }
}