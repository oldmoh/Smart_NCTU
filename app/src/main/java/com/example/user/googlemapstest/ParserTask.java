package com.example.user.googlemapstest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by User on 2016/3/26.
 */
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
