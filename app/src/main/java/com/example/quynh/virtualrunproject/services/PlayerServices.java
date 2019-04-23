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
    public static void getPlayerRecordWithId(int userId, final Context context, final OnReceiveResponse receiveResponse){
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
                Log.d("PlayerService", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getPlayerRecordWithId", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getParticipateProfile(int raceId, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/profile?raceId=" + raceId;
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
                Toast.makeText(context, "Service Error, There's something wrong getParticipateProfile", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getAttendingRaces(int userId, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/attending/ongoing?userId=" + userId;
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
                Toast.makeText(context, "Service Error, There's something wrong getAttendingRaces", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getRaceParticipants(int raceId, final Context context, final OnReceiveResponse receiveResponse){
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
                Log.d("PlayerService", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getRaceParticipants", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getFinishResult(int raceId, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/race/finish?raceId=" + raceId;
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
                Toast.makeText(context, "Service Error, There's something wrong getFinishResult", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void playerRegister(Player player, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/register";
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("paticipant", gson.toJson(player));
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
                Log.e("PlayerService", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong playerRegister", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void cancelRegister(Player player, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/register/cancel";
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("paticipant", gson.toJson(player));
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
                Log.e("PlayerService", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong cancelRegister", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void sendResult(Player player, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/players/sendResult";
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("record", gson.toJson(player));
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
                Log.e("PlayerService", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong sendResult", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
