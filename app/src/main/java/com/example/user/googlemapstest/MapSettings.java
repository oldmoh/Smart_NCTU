package com.example.user.googlemapstest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by User on 2016/4/21.
 */
public class MapSettings implements OnMapReadyCallback {

    GoogleMap googleMap;
    private int markerNum = 0;

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

    public void startNavigation(LatLng start, LatLng des) {
        String origin = "origin=" + start.latitude + "," + start.longitude;
        String destination = "destination=" + des.latitude + "," + des.longitude;
        String mode = "mode=walking";
        String parameter = origin + "&" + destination + "&" + mode + "&";
        String URL = "https://maps.googleapis.com/maps/api/directions/json?" + parameter + "key=AIzaSyAZ5XnHPDzdcv90GTBEaxAbf83OkNLlTEU";
        PathDownloadTask downloadTask = new PathDownloadTask(googleMap);
        downloadTask.execute(URL);
    }

}
