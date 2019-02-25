package com.example.quynh.virtualrunproject.facebooksdk;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by quynh on 2/15/2019.
 */

public class FacebookLogin {

    public interface ResultHandler{
        void responeResult(JSONObject object);
    }

    public static void loadUserProfile(AccessToken token, final ResultHandler resultHandler){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    resultHandler.responeResult(object);
                    Log.d("Login", "respone: " + response.toString());
                    Log.d("Login", "object: " + object.toString());
                    String firstName = object.getString("first_name");
                    String lastName = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
