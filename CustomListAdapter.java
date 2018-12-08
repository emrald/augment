package com.trivediinfoway.theinnontheriver;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataClass> movieItems;
    List<DataClass> arrDataFilter;
    static boolean flag = false;

    public CustomListAdapter(Activity activity, List<DataClass> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
        arrDataFilter = movieItems;
        arrDataFilter = new ArrayList<DataClass>();
        arrDataFilter.addAll(movieItems);
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView tvtitlebusiness = (TextView) convertView.findViewById(R.id.tvtitlebusiness);
        final DataClass m = movieItems.get(position);

        tvtitlebusiness.setText(m.getDiscount());

        return convertView;
    }
}