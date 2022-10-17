package com.sxm.warmer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sxm.warmer.R;
import com.sxm.warmer.util.TempList;

import java.util.List;

public class TempListAdapter extends ArrayAdapter<TempList> {
    private int resourceId;
    private List<TempList> tempLists;

    public TempListAdapter(Context context, int textViewResourceId, List<TempList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        tempLists = objects;
    }

    public void update(List<TempList> tempLists) {
        this.tempLists.clear();
        this.tempLists.addAll(tempLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TempList tempList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView type = view.findViewById(R.id.type);
        TextView temp = view.findViewById(R.id.temp);
        type.setText(tempList.type);
        temp.setText(tempList.temp);
        return view;
    }
}
