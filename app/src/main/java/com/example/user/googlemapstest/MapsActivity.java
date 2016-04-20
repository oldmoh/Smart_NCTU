package com.example.user.googlemapstest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);//可有可無  但是下一航要是沒有就連btn都沒有
        mMap.setMyLocationEnabled(true);

        Location myLocation = mMap.getMyLocation();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                LatLng des = latLng;
                Location me = mMap.getMyLocation();
                LatLng start = new LatLng(me.getLatitude(), me.getLongitude());

                mMap.addMarker(new MarkerOptions().position(start).title("Marker in Dorm.9"));
                mMap.addMarker(new MarkerOptions().position(des).title("destination"));

                String origin = "origin=" + start.latitude + "," + start.longitude;
                String destination = "destination=" + des.latitude + "," + des.longitude;
                String mode = "mode=walking";
                String parameter = origin + "&" + destination + "&" + mode + "&";
                String URL = "https://maps.googleapis.com/maps/api/directions/json?" + parameter + "key=AIzaSyAZ5XnHPDzdcv90GTBEaxAbf83OkNLlTEU";
                ConnThread connThread = new ConnThread(MapsActivity.this, URL);
                connThread.start();
            }
        });
    }

}
