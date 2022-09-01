package com.example.food.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class CustomWebView extends WebView {

        public CustomWebView(@NonNull Context context) {
                super(context);
                initView();
        }

        public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
                super(context, attrs);
                initView();
        }

        private void initView() {
                this.getSettings().setJavaScriptEnabled(true);
                this.getSettings().setDomStorageEnabled(true);
                this.getSettings().setLoadWithOverviewMode(true);
                this.getSettings().setUseWideViewPort(true);
                this.getSettings().setBuiltInZoomControls(true);
                this.getSettings().setDisplayZoomControls(false);
                this.getSettings().setSupportZoom(true);
                this.getSettings().setAllowContentAccess(true);
                this.getSettings().setAppCacheEnabled(true);
                this.getWebViewClient(new WebViewClient());
        }

        private void getWebViewClient(WebViewClient webViewClient) {
        }

//        void observeAndroidBridge(mListener: AndroidBridge.OnAndroidBridgeListener? = null){
//                this.addJavascriptInterface(AndroidBridge(mListener), Constants.WebKitAction.androidBridge);
//        }
}
//        constructor(context: Context) : super(context) {
//        initView()
//        }
//
//        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        initView()
//        }
//
//private fun initView() {
//        this.settings.javaScriptEnabled = true
//        this.settings.useWideViewPort = true
//        this.settings.loadWithOverviewMode = true
//        this.settings.domStorageEnabled = true
//
//        this.webViewClient = WebViewClient()
//        this.settings.setAppCacheEnabled(true)
//        this.settings.allowFileAccess = true
//        }
//
//        fun observeAndroidBridge(mListener: AndroidBridge.OnAndroidBridgeListener? = null){
//        this.addJavascriptInterface(AndroidBridge(mListener), Constants.WebKitAction.androidBridge)
//        }
//        }
