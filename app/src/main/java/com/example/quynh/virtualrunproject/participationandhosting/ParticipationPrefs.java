package com.example.quynh.virtualrunproject.participationandhosting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quynh on 3/4/2019.
 */

public class ParticipationPrefs {
    private Context context;
    private SharedPreferences participation;

    public ParticipationPrefs(Context context) {
        this.context = context;
        this.participation = context.getSharedPreferences("participation", Context.MODE_PRIVATE);
    }

    public void saveUserLogin(String participationString){
        SharedPreferences.Editor editor = participation.edit();
        editor.putString("userParticipate", participationString);
        editor.commit();
    }

    public String getUserAccount(){
        return participation.getString("userParticipate", "");
    }
}
