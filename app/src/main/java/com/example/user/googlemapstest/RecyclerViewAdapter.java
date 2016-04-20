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
        TextView title,content;
        ImageView imageView;
        ImageButton pin,favorite;

        // constructor of DataObjectHolder
        public DataObjectHolder(View itemView) {
            super(itemView);
            switch (getItemViewType()) {
                case SCHOOL:
                    title = (TextView) itemView.findViewById(R.id.card_title);
                    content = (TextView) itemView.findViewById(R.id.card_content);
                    imageView = (ImageView) itemView.findViewById(R.id.card_image);
                    pin = (ImageButton) itemView.findViewById(R.id.card_pin);
                    favorite = (ImageButton) itemView.findViewById(R.id.card_favorite);
                    itemView.setOnClickListener(this);
                    break;
                case CLUB:
                    title = (TextView) itemView.findViewById(R.id.card_title);
                    content = (TextView) itemView.findViewById(R.id.card_content);
                    imageView = (ImageView) itemView.findViewById(R.id.card_image);
                    pin = (ImageButton) itemView.findViewById(R.id.card_pin);
                    favorite = (ImageButton) itemView.findViewById(R.id.card_favorite);
                    itemView.setOnClickListener(this);
                    break;
                case E_CAMPUS:
                    Log.d("E_campus", "asdfsafsfasf");
                    title = (TextView) itemView.findViewById(R.id.card_title);
                    content = (TextView) itemView.findViewById(R.id.card_content);
                    pin = (ImageButton) itemView.findViewById(R.id.card_pin);
                    itemView.setOnClickListener(this);
                    break;
                default:
                    title = (TextView) itemView.findViewById(R.id.card_title);
                    content = (TextView) itemView.findViewById(R.id.card_content);
                    imageView = (ImageView) itemView.findViewById(R.id.card_image);
                    pin = (ImageButton) itemView.findViewById(R.id.card_pin);
                    favorite = (ImageButton) itemView.findViewById(R.id.card_favorite);
                    itemView.setOnClickListener(this);
                    break;
            }
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
        switch (viewType) {
            case SCHOOL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
                break;
            case CLUB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_2, parent, false);
                break;
            case E_CAMPUS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_3, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
                break;
        }
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder dataObjectHolder, final int position) {
        switch (dataObjectHolder.getItemViewType()) {
            case SCHOOL:
                dataObjectHolder.title.setText(dataSet.get(position).getTittle());
                dataObjectHolder.content.setText(dataSet.get(position).getContent());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = (InputStream) new URL("https://pbs.twimg.com/profile_images/440852605510488065/pFHkEztN_400x400.jpeg").getContent();
                            Drawable drawable = Drawable.createFromStream(inputStream, "Image."+position);
                            dataObjectHolder.imageView.setImageDrawable(drawable);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                dataObjectHolder.pin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataObject temp = dataSet.get(position);
                        dataSet.remove(position);
                        dataSet.add(0, temp);
                        notifyItemChanged(0);
                        notifyItemChanged(position);
                    }
                });

                dataObjectHolder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;

            case CLUB:
                dataObjectHolder.title.setText(dataSet.get(position).getTittle());
                dataObjectHolder.content.setText(dataSet.get(position).getContent());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = (InputStream) new URL("http://www.songshanculturalpark.org/images/5dded13e-5144-4583-bd19-a651b16d41a9/%E5%9C%92%E5%8D%80%E5%AE%98%E7%B6%B2%E5%85%A7%E5%AE%B9%E9%A0%81-0120164614094625.jpg").getContent();
                            Drawable drawable = Drawable.createFromStream(inputStream, "Image."+position);
                            dataObjectHolder.imageView.setImageDrawable(drawable);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                dataObjectHolder.pin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataObject temp = dataSet.get(position);
                        dataSet.remove(position);
                        dataSet.add(0, temp);
                        notifyItemChanged(0);
                        notifyItemChanged(position);
                    }
                });

                dataObjectHolder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;

            case E_CAMPUS:
                dataObjectHolder.title.setText(dataSet.get(position).getTittle());
                dataObjectHolder.content.setText(dataSet.get(position).getContent());

                dataObjectHolder.pin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataObject temp = dataSet.get(position);
                        dataSet.remove(position);
                        dataSet.add(0, temp);
                        notifyItemChanged(0);
                        notifyItemChanged(position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("ViewType", ""+dataSet.get(position).getViewType());
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
