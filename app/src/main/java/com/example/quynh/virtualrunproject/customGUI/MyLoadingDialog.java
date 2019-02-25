package com.example.quynh.virtualrunproject.customGUI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.quynh.virtualrunproject.R;

/**
 * Created by quynh on 2/17/2019.
 */

public class MyLoadingDialog extends Dialog {
    public MyLoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
