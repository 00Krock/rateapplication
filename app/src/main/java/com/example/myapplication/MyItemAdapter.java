package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyItemAdapter extends ArrayAdapter {
    private int res;
    public MyItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RateItem> objects) {
        super(context, resource, objects);
        this.res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(this.res, parent,false);
        }

        RateItem item = (RateItem)getItem(position);
        TextView title = itemView.findViewById(R.id.itemTitle);
        TextView detail = itemView.findViewById(R.id.itemDetail);

        title.setText(item.getCname());
        detail.setText(""+item.getCval());

        return itemView;
    }
}