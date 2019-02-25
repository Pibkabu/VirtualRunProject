package com.example.quynh.virtualrunproject.userlogintracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quynh on 2/17/2019.
 */

public class UserAccountPrefs {
    private Context context;
    private SharedPreferences userAccount;

    public UserAccountPrefs(Context context) {
        this.context = context;
        this.userAccount = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    public void saveUserLogin(String loginString){
        SharedPreferences.Editor editor = userAccount.edit();
        editor.putString("userAccount", loginString);
        editor.commit();
    }

    public String getUserAccount(){
        return userAccount.getString("userAccount", "");
    }
}
