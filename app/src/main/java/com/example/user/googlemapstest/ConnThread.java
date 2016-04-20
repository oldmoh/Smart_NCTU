package com.example.user.googlemapstest;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        //JSONObject jsonObject=null;
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
        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException io) {

            }
            httpURLConnection.disconnect();
        }
        Log.d("JSON",data);/*
        try {
            jsonObject = new JSONObject(data);
        }catch (Exception e) {
            e.printStackTrace();
        }*/
        //connsucc(jsonObject);
    }



}

