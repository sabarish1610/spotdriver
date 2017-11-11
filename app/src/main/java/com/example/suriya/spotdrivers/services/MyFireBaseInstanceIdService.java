package com.example.suriya.spotdrivers.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.suriya.spotdrivers.support.SupportConstant;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sabarish on 20/6/17.
 */

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {
    private SharedPreferences sharedPreferences;
    private static final String LOG_ID = "log_id";
    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences = getApplicationContext().getSharedPreferences(SupportConstant.FCM_SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SupportConstant.FCM_ID,recent_token);
        editor.commit();
        Log.d(LOG_ID,recent_token);
    }
}
