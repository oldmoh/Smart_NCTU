package com.example.user.googlemapstest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 2016/4/27.
 */
public class PathJSONParseTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private GoogleMap googleMap;

    public PathJSONParseTask(GoogleMap map) {
        this.googleMap = map;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jsonObject = null;
        JSONArray mroutes = null;
        JSONArray mlegs = null;
        JSONArray msteps = null;
        List<List<HashMap<String, String>>> routes = new ArrayList<>();


        try {
            jsonObject = new JSONObject(jsonData[0]);

            mroutes = jsonObject.getJSONArray("routes");

            for (int i = 0; i < mroutes.length(); i++) {
                mlegs = ((JSONObject)mroutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();

                for (int j = 0; j < mlegs.length(); j++) {
                    msteps = ((JSONObject)mlegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < msteps.length(); k++) {
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)msteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("lat", Double.toString((list.get(l)).latitude));
                            hashMap.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hashMap);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
            points = new ArrayList<>();
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


