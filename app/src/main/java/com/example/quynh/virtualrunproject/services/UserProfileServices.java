package com.example.quynh.virtualrunproject.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.quynh.virtualrunproject.connection.AppController;
import com.example.quynh.virtualrunproject.connection.ConnectionAddress;
import com.example.quynh.virtualrunproject.connection.CustomRequest;
import com.example.quynh.virtualrunproject.customGUI.MyLoadingDialog;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quynh on 2/17/2019.
 */

public class UserProfileServices {
    public static void getAllUserProfile(final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = "";
        CustomRequest customRequest = new CustomRequest(URL, null, new Response.Listener<JSONObject>() {
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
                Log.d("UserProfileServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getAllUserProfile", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getUserProfileWithId(int id, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/profile/id?" + "id=" + id;
        CustomRequest customRequest = new CustomRequest(URL, null, new Response.Listener<JSONObject>() {
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
                Log.d("UserProfileServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getUserProfileWithId", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void addUserProfile(UserProfile profile, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Map<String, String> params = new HashMap<>();
        params.put("profileJson", gson.toJson(profile));
        Log.d("PROFILE", "addUserProfile: " + gson.toJson(profile));
        String URL = ConnectionAddress.connection + "/profile/add";
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
                Log.d("UserProfileServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong addUserProfile", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void updateUserProfile(UserProfile profile, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Map<String, String> params = new HashMap<>();
        params.put("profileJson", gson.toJson(profile));
        String URL = ConnectionAddress.connection + "/profile/update";
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
                Log.d("UserProfileServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong updateUserProfile", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
