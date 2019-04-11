package com.example.quynh.virtualrunproject.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.quynh.virtualrunproject.connection.AppController;
import com.example.quynh.virtualrunproject.connection.ConnectionAddress;
import com.example.quynh.virtualrunproject.connection.CustomRequest;
import com.example.quynh.virtualrunproject.customGUI.MyLoadingDialog;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;

import org.json.JSONObject;

/**
 * Created by quynh on 4/11/2019.
 */

public class DonateAccountServices {
    public static void getRaceDonationRecord(int raceId, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/donate/account?raceId=" + raceId;
        CustomRequest customRequest = new CustomRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingDialog.dismiss();
                receiveResponse.onReceive(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Log.d("DonateAccountService", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getRaceById", Toast.LENGTH_LONG).show();
                //dialog
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
