package com.example.quynh.virtualrunproject.mainfragmentscreens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quynh.virtualrunproject.R;

/**
 * Created by quynh on 3/21/2019.
 */

public class HostingFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosting_fragment_layout, container, false);
        return view;
    }
}
