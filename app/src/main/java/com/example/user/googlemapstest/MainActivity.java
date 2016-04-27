package com.example.user.googlemapstest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.internal.LocationRequestUpdateData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<DataObject> data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        // Card Recycler
        setRecyclerView();
        // View Component
        setViewComponent();
    }

    private void setRecyclerView() {
        // RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        data = readData();
        adapter = new RecyclerViewAdapter(data, this);
        recyclerView.setAdapter(adapter);
        // Behavior of Card Selected
        ((RecyclerViewAdapter) adapter).setOnItemClickListener(new RecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                data.remove(position);
                ((RecyclerViewAdapter) adapter).deleteItem(position);
                adapter.notifyItemRemoved(position);

                Toast.makeText(MainActivity.this, "Item" + position + "is clicked!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setViewComponent() {
        // ToolBar
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Main");
        setSupportActionBar(myToolbar);
        // DrawerLayout
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        // Navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
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

    // Set Menu
    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_option, menu);
        return true;
    }
    // Behaviors of Option Menu Item Selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // Read Data from SD card
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
    // Write Data into SD card
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

    // Behavior of Navigation
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_map:  // go to GoogleMap
                Intent IntentMap = new Intent();
                IntentMap.setClass(MainActivity.this, Main2Activity.class);
                startActivity(IntentMap);
                break;
            case R.id.nav_calender:
            case R.id.nav_profile:
            default:
        }
        // close navigation bar
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
