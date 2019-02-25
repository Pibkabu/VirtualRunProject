package com.example.quynh.virtualrunproject.customGUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import com.example.quynh.virtualrunproject.R;

/**
 * Created by quynm on 1/18/2018.
 */

public class MySpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> listItems;
    private int spinnerLayout;

    public MySpinnerAdapter(Context context, String[] objects, int spinnerLayout) {
        super(context, spinnerLayout, objects);
        this.listItems = new ArrayList<>(Arrays.asList(objects));
        this.spinnerLayout = spinnerLayout;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(spinnerLayout, parent, false);
        TextView testItem = (TextView) row.findViewById(R.id.items);
        testItem.setText(listItems.get(position));
        return row;
    }
}


