package com.example.user.googlemapstest;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.MapFragment;


public class Main2Activity extends AppCompatActivity{

    private MapSettings mapSettings;
    private EditText edtxt1, edtxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Map custom settings
        mapSettings = new MapSettings();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(mapSettings);

        setViewComponent();
    }

    public void setViewComponent() {
        // ToolBar
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Navigation");
        setSupportActionBar(myToolbar);
        // edit text
        edtxt1 = (EditText) findViewById(R.id.begin);
        edtxt2 = (EditText) findViewById(R.id.end);
        // floating action btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatbtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fab","Clicked");
                if(edtxt1.isShown()) {
                    edtxt1.setVisibility(View.GONE);
                    edtxt2.setVisibility(View.GONE);
                } else {
                    edtxt1.setVisibility(View.VISIBLE);
                    edtxt2.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
