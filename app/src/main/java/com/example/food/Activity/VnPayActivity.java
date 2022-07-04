package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.food.R;

public class VnPayActivity extends AppCompatActivity {

    WebView webViewVnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vn_pay);


        setControls();
        setEvents();
    }

    private void setEvents() {
    }

    private void setControls() {
        webViewVnPay = findViewById(R.id.web_view_vnpay);
        setUpWebView(webViewVnPay);
    }

    private void setUpWebView(WebView webViewVnPay) {
        webViewVnPay.loadUrl("https://sandbox.vnpayment.vn/tryitnow/Home/CreateOrder");
        webViewVnPay.getSettings().setJavaScriptEnabled(true);

        webViewVnPay.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void performClick(String str){
                Toast. makeText (VnPayActivity.this, str , Toast. LENGTH_SHORT ).show() ;
            }
        }, "Ok");
        webViewVnPay.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }



            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }






        });
    }

    @Override
    public void onBackPressed() {
        if(webViewVnPay.canGoBack()){
            webViewVnPay.goBack();
        }else super.onBackPressed();
    }
}