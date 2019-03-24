package com.example.quynh.virtualrunproject.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.quynh.virtualrunproject.connection.AppController;
import com.example.quynh.virtualrunproject.connection.ConnectionAddress;
import com.example.quynh.virtualrunproject.connection.CustomRequest;
import com.example.quynh.virtualrunproject.customGUI.MyLoadingDialog;

import org.json.JSONObject;

/**
 * Created by quynh on 3/10/2019.
 */

public class HostingServices {
    public static void getOngoingRacesUserHosting(int userId, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/hosting/ongoing?userId=" + userId;
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
                Log.d("PlayerService", "onResponse: " + error);
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getPastRacesUserHosting(int userId, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/hosting/past?userId=" + userId;
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
                Log.d("PlayerService", "onResponse: " + error);
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
