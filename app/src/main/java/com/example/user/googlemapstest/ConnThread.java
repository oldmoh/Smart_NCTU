package com.example.user.googlemapstest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 2016/3/21.
 */
public class ConnThread extends Thread {
    Activity activity;
    String url;

    ConnThread(Activity activity, String URL) {
        this.activity = activity;
        this.url = URL;
    }

    @Override
    public void run() {
        String data = DownloadTask();
        Log.d("JSON",data);
        ParseJSON(data);
    }

    private String DownloadTask() {
        String data = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            data = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        }
        return data;
    }

    private void ParseJSON(String data) {
        JSONObject jsonObject = null;
        JSONArray mroutes = null;
        JSONArray mlegs = null;
        JSONArray msteps = null;
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        try {
            jsonObject = new JSONObject(data);

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
        /*
        Bundle bundle = new Bundle();
        //bundle.putStringArrayList("routes", routes);
        Message message = new Message();
        message.setData(bundle);

        */
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

