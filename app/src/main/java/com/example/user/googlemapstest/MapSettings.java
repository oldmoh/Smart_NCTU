package com.example.user.googlemapstest;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;

/**
 * Created by User on 2016/4/21.
 */
public class MapSettings implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private int markerNum = 0;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void startNavigation(LatLng start, LatLng des) {
        String origin = "origin=" + start.latitude + "," + start.longitude;
        String destination = "destination=" + des.latitude + "," + des.longitude;
        String mode = "mode=walking";
        String parameter = origin + "&" + destination + "&" + mode + "&";
        String URL = "https://maps.googleapis.com/maps/api/directions/json?" + parameter + "key=AIzaSyAZ5XnHPDzdcv90GTBEaxAbf83OkNLlTEU";
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(URL);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        final UiSettings myUiSettings = googleMap.getUiSettings();
        myUiSettings.setMyLocationButtonEnabled(true);
        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setMapToolbarEnabled(false);

        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions temp = new MarkerOptions().position(latLng)
                        .snippet("經度: " + latLng.longitude)
                        .snippet("緯度: " + latLng.latitude)
                        .title("Marker: " + markerNum);
                markerNum++;
                googleMap.addMarker(temp);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                };
                return false;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });


    }
}
