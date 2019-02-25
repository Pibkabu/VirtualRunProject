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
import com.example.quynh.virtualrunproject.entity.UserAccount;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quynh on 2/17/2019.
 */

public class UserAccountServices {

    public static void getAllUserAccount(Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/user";
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

    public static void getUserAccountWithEmail(String email, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/user/email?email=" + email;
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

    public static void addUserAccount(UserAccount account, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/user/add";
        Map<String, String> params = new HashMap<>();
        params.put("email", account.getEmail());
        params.put("password", account.getPassword());
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

    public static void accountLogin(UserAccount account, Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/user/login";
        Map<String, String> params = new HashMap<>();
        params.put("email", account.getEmail());
        params.put("password", account.getPassword());
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
