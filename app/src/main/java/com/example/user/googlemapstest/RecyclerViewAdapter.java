package com.example.user.googlemapstest;

import android.app.Notification;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 2016/3/23.
 */
/*
* Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView
*
*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {
    private ArrayList<DataObject> dataSet;
    private static MyClickListener myClickListener;
    public static final int SCHOOL = 0, CLUB = 1, E_CAMPUS = 2;


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /*
        * A ViewHolder describes an item view (ex: card_view_row) and metadata about its place within the RecyclerView.
        * */
        TextView title,content,date;
        ImageView imageView;
        ImageButton pin,favorite;

        // constructor of DataObjectHolder
        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            date = (TextView)itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.content);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    // constructor of RecyclerView
    public RecyclerViewAdapter(ArrayList<DataObject> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridcard1, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder dataObjectHolder, final int position) {
        dataObjectHolder.title.setText(dataSet.get(position).getTitle());
        dataObjectHolder.date.setText(dataSet.get(position).getDate());
        dataObjectHolder.content.setText(dataSet.get(position).getContent());
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItem(DataObject dataObject, int index) {
        dataSet.add(index, dataObject);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        dataSet.remove(index);
        notifyItemRemoved(index);
    }

    public void exchangeItemOrder(int first, int second) {
        DataObject temp = dataSet.get(first);
        dataSet.set(first, dataSet.get(second));
        dataSet.set(second, temp);
    }

    // set click listener and call back
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View view);
    }
}
