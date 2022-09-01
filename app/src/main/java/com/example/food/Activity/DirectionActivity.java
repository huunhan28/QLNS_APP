package com.example.food.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.food.R;

public class DirectionActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        float latitude = getIntent().getFloatExtra("latitude",0);
        float longtitude = getIntent().getFloatExtra("longitude",0);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        //webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl("http://maps.google.com/maps?saddr="+latitude+","+longtitude+"&daddr=10.395641,-254.273787&mode=driving");

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//                0, mLocationListener);
        /*var rad = function(x) {
          return x * Math.PI / 180;
        };

        var getDistance = function(p1, p2) {
          var R = 6378137; // Earth’s mean radius in meter
          var dLat = rad(p2.lat() - p1.lat());
          var dLong = rad(p2.lng() - p1.lng());
          var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(rad(p1.lat())) * Math.cos(rad(p2.lat())) *
            Math.sin(dLong / 2) * Math.sin(dLong / 2);
          var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
          var d = R * c;
          return d; // returns the distance in meter
        };*/

    }
}