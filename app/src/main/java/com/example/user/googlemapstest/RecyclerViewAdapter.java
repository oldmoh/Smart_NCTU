package com.example.user.googlemapstest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 2016/3/23.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {
    private ArrayList<DataObject> dataSet;
    private static MyClickListener myClickListener;

    /*
    * Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView
    *
    * */

    public static class DataObjectHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        /*
        * A ViewHolder describes an item view (ex: card_view_row) and metadata about its place within the RecyclerView.
        *
        * */
        TextView title,content;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_title);
            content = (TextView) itemView.findViewById(R.id.card_txt);
            //itemView.setOnClickListener(this);
        }
        /*
        @Override
        public void onClick(View view) {

        }
        */
    }

    // constructor
    public RecyclerViewAdapter(ArrayList<DataObject> dataSet, int viewType) {
        this.dataSet = dataSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder dataObjectHolder, int position) {
        dataObjectHolder.title.setText(dataSet.get(position).getTittle());
        dataObjectHolder.content.setText(dataSet.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItem(DataObject dataObject, int index) {
        dataSet.add(index, dataObject);
        notifyItemInserted(index);
    }

    public void deleteItem(DataObject dataObject, int index) {
        dataSet.remove(index);
        notifyItemRemoved(index);
    }

    // set click listener and interface of click listener
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View view);
    }

}
