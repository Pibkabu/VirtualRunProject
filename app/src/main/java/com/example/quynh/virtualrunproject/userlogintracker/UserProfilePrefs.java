package com.example.quynh.virtualrunproject.userlogintracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quynh on 2/17/2019.
 */

public class UserProfilePrefs {
    private Context context;
    private SharedPreferences userProfile;

    public UserProfilePrefs(Context context) {
        this.context = context;
        this.userProfile = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
    }

    public void saveUserProfile(String profile){
        SharedPreferences.Editor editor = userProfile.edit();
        editor.putString("userProfile", profile);
        editor.commit();
    }

    public String getProfile(){
        return userProfile.getString("userProfile", "");
    }
}
