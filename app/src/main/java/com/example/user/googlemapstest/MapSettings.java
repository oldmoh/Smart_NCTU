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
            googleMap.addPolyline(polylineOptions);
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
