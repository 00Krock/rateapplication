package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    private int res;
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<RateItem> objects) {
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

        Map<String,String> map = (Map<String, String>) getItem(position);
        TextView title = itemView.findViewById(R.id.itemTitle);
        TextView detail = itemView.findViewById(R.id.itemDetail);

        title.setText("币种:" + map.get("ItemTitle"));
        detail.setText("汇率:" + map.get("ItemDetail"));

        return itemView;
    }
}