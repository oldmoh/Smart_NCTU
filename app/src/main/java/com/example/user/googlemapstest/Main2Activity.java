package com.example.user.googlemapstest;

import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //setViewComponent();
    }

    public void setViewComponent() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatbtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng oldmoh = new LatLng(24.789699, 120.996823);
                LatLng des = new LatLng(24.794296, 120.993282);
                String origin = "origin=" + oldmoh.latitude + "," + oldmoh.longitude;
                String destination = "destination=" + des.latitude + "," + des.longitude;
                String mode = "mode=walking";
                String parameter = origin + "&" + destination + "&" + mode + "&";
                String URL = "https://maps.googleapis.com/maps/api/directions/json?" + parameter + "key=AIzaSyAZ5XnHPDzdcv90GTBEaxAbf83OkNLlTEU";
                Log.d("URL", parameter);
            }
        });
    }

}
