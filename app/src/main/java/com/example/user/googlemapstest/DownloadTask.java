package com.example.user.googlemapstest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 2016/3/26.
 */
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
        //MapSettings.ParserTask parserTask = new MapSettings.ParserTask();
        //parserTask.execute(result);
    }
}
