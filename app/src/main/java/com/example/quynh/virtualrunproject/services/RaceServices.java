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
import com.example.quynh.virtualrunproject.entity.Race;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quynh on 2/17/2019.
 */

public class RaceServices {

    public static void getRacesWithDistanceRange(double from, double to, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/distance?from=" + from + "&to=" + to;
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
                Log.d("RaceServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getRacesWithDistanceRange", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void searchRacesWithName(Race race, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/search";
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("name", gson.toJson(race));
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
                Log.e("RaceServices", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong searchRacesWithName", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getAllRaces(final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races";
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
                Log.d("RaceServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getAllRaces", Toast.LENGTH_LONG).show();
                //dialog
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getRaceById(int id, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/id?id=" + id;
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
                Log.d("RaceServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getRaceById", Toast.LENGTH_LONG).show();
                //dialog
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getOngoingRaces(final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/ongoing";
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
                Log.d("RaceServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getOngoingRaces", Toast.LENGTH_LONG).show();
                //dialog
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void getALlEndedRaces(final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/ended/all";
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
                Log.d("RaceServices", "onResponse: " + error);
                Toast.makeText(context, "Service Error, There's something wrong getALlEndedRaces", Toast.LENGTH_LONG).show();
                //dialog
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void createRace(Race race, int userId, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/add";
        Map<String, String> params = new HashMap<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        params.put("raceJson1", gson.toJson(race));
        params.put("userId", String.valueOf(userId));
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
                Log.e("RaceServices", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong createRace", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    public static void editRaceInfo(Race race, final Context context, final OnReceiveResponse receiveResponse){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(context);
        //dialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String URL = ConnectionAddress.connection + "/races/edit";
        Map<String, String> params = new HashMap<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        params.put("raceJson", gson.toJson(race));
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
                Log.e("RaceServices", "onResponse: ", error);
                Toast.makeText(context, "Service Error, There's something wrong editRaceInfo", Toast.LENGTH_LONG).show();
            }
        });
        customRequest.setRetryPolicy(AppController.myRetryPolicy);
        AppController.getInstance().addToRequestQueue(customRequest);
    }
}
