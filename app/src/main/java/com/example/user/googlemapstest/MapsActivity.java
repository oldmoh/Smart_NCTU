package com.example.user.googlemapstest;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Message;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
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
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(URL);
                //ConnThread connThread = new ConnThread(MapsActivity.this, URL);
                //connThread.start();
            }
        });
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... Url) {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                java.net.URL url = new java.net.URL(Url[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line ="";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                data = stringBuffer.toString();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.d("Async","Exception from download");
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject = null;
            JSONArray mroutes = null;
            JSONArray mlegs = null;
            JSONArray msteps = null;
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();


            try {
                jsonObject = new JSONObject(jsonData[0]);

                mroutes = jsonObject.getJSONArray("routes");

                for (int i = 0; i < mroutes.length(); i++) {
                    mlegs = ((JSONObject)mroutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    for (int j = 0; j < mlegs.length(); j++) {
                        msteps = ((JSONObject)mlegs.get(j)).getJSONArray("steps");

                        for (int k = 0; k < msteps.length(); k++) {
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)msteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("lat", Double.toString(((LatLng)list.get(l)).latitude));
                                hashMap.put("lng", Double.toString(((LatLng)list.get(l)).longitude));
                                path.add(hashMap);
                            }
                        }
                        routes.add(path);
                    }
                }
            }catch (JSONException e) {
                Log.d("Parser", "IOException");
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;
            MarkerOptions markerOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0 ; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(5);
                polylineOptions.color(Color.BLUE);
            }
            mMap.addPolyline(polylineOptions);
        }

        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
}
