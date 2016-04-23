package com.example.user.googlemapstest;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.internal.LocationRequestUpdateData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataObject> data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // View Component
        setViewComponent();
    }

    public void setViewComponent() {
        // ToolBar
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Main");
        setSupportActionBar(myToolbar);
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        data = readData();
        adapter = new RecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);
        // set the behavior of card after clicking
        ((RecyclerViewAdapter) adapter).setOnItemClickListener(new RecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.d("Card", "Click");
                data.remove(position);
                ((RecyclerViewAdapter) adapter).deleteItem(position);
                adapter.notifyItemRemoved(position);

                Toast.makeText(MainActivity.this, "Item" + position + "is clicked!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> results = new ArrayList<>();
        for(int index = 0; index < 36; index++) {
            DataObject dataObject = new DataObject("Title." + index, "Content " + index, "2016/4/26", index%3);
            results.add(index, dataObject);
        }
        return results;
    }

    @Override
    protected void onPause(){
        super.onPause();
        // write data list back to SD card
        writeData(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // read data list from SD card
        data = readData();
    }

    // Set Menu and behavior of Menu Item clicked
    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Log.d("f","sdf");
                return true;
            case R.id.action_settings:
                Log.d("Setting","Clicked");
                return true;
            case R.id.action_Map:
                Intent IntentMap = new Intent();
                IntentMap.setClass(MainActivity.this, Main2Activity.class);
                startActivity(IntentMap);
                return true;
            case R.id.action_test:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<DataObject> readData() {
        String path = Environment.getExternalStorageDirectory().getPath();
        Log.d("Path", path);
        path = path + "/Smart_NCTU";
        File file = new File(path + "/datalist");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[fileInputStream.available()];
            while (fileInputStream.read(data) != -1) {
                stringBuilder.append(new String(data));
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<DataObject> dataList = new ArrayList<DataObject>();
        try {
            JSONArray jList = new JSONArray(stringBuilder.toString());
            for(int i = 0; i < jList.length(); i++) {
                JSONObject jObj = jList.getJSONObject(i);
                DataObject temp = new DataObject(jObj.getString("Title"), jObj.getString("Content"), jObj.getString("Date"), jObj.getInt("Viewtype"));
                dataList.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void writeData(ArrayList<DataObject> data) {
        // enviroment path
        String path = Environment.getExternalStorageDirectory().getPath();
        Log.d("Path", path);
        path = path + "/Smart_NCTU";
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }
        JSONArray jList = new JSONArray();
        try {
            for (int indx = 0; indx < data.size(); indx++) {
                JSONObject jObj = new JSONObject();
                jObj.put("Title", data.get(indx).getTitle());
                jObj.put("Content", data.get(indx).getContent());
                jObj.put("Date", data.get(indx).getDate());
                jObj.put("Viewtype", data.get(indx).getViewType());
                jList.put(jObj);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        String output = jList.toString();
        Log.d("Json List", output);
        try {
            File file = new File(path + "/datalist");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(output.getBytes());
            fileOutputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
