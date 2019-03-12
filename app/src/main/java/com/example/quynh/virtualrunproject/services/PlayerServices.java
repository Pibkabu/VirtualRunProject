package com.example.quynh.virtualrunproject.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.quynh.virtualrunproject.connection.AppController;
import com.example.quynh.virtualrunproject.connection.ConnectionAddress;
import com.example.quynh.virtualrunproject.connection.CustomRequest;
import com.example.quynh.virtualrunproject.customGUI.MyLoadingDialog;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quynh on 3/3/2019.
 */

public class PlayerServices {
    public static void getPlayerRecordWithId(int userId, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/id?userId=" + userId;
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
                Log.d("UserAccountServices", "onResponse: " + error);
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getRaceParticipants(int raceId, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/race?raceId=" + raceId;
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
                Log.d("UserAccountServices", "onResponse: " + error);
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void playerRegister(Player player, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/register";
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("paticipant", gson.toJson(player));
        Log.d("CHECKREGISTER", "playerRegister: " + gson.toJson(player));
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingDialog.dismiss();
                receiveResponse.onReceive(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog
                loadingDialog.dismiss();
                Log.e("UserAccountServices", "onResponse: ", error);
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
