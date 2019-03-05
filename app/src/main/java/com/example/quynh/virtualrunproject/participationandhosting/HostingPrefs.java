package com.example.quynh.virtualrunproject.participationandhosting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quynh on 3/4/2019.
 */

public class HostingPrefs {
    private Context context;
    private SharedPreferences hosting;

    public HostingPrefs(Context context) {
        this.context = context;
        this.hosting = context.getSharedPreferences("hosting", Context.MODE_PRIVATE);
    }

    public void saveUserLogin(String hostingString){
        SharedPreferences.Editor editor = hosting.edit();
        editor.putString("userHosting", hostingString);
        editor.commit();
    }

    public String getUserAccount(){
        return hosting.getString("userHosting", "");
    }
}
