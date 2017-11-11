package com.example.suriya.spotdrivers.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;

/**
 * Created by Suriya on 29-07-2017.
 */

public class MySharedPreference {
    //sharedPreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;
    //

    public MySharedPreference(Context _context) {
        this._context = _context;
        sharedPreferences = _context.getSharedPreferences(SupportConstant.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void storeUserLoginDetails(ServerResponse serverResponse, String loginType)
    {
        editor.putBoolean(SupportConstant.LOGGED_IN_STATUS,true);
        editor.putString(SupportConstant.LOGGEDIN_TYPE, loginType);
        if (loginType.contentEquals(SupportConstant.USER))
        editor.putString(SupportConstant.LOGGEDIN_USERID,serverResponse.getUser().getMobile());
        else if (loginType.contentEquals(SupportConstant.DRIVER))
            editor.putString(SupportConstant.LOGGEDIN_USERID,serverResponse.getDriver().getMobile_number());
        editor.commit();
        Log.i("loggin type", loginType);
    }
    public LoggedInDetails retrieveLoginDetails()
    {
        if(sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID,null) != null)
        {
            boolean loggedInStatus = sharedPreferences.getBoolean(SupportConstant.LOGGED_IN_STATUS,false);
            String logginedInUserId = sharedPreferences.getString(SupportConstant.LOGGEDIN_TYPE, null);
            String logginedInType = sharedPreferences.getString(SupportConstant.LOGGEDIN_TYPE, null);
            LoggedInDetails loggedInDetails = new LoggedInDetails(loggedInStatus,logginedInType,logginedInUserId);
            return loggedInDetails;
        }
        return null;
    }
    public void logout()
    {
        editor.clear();
        editor.commit();
        _context.startActivity(new
                Intent(_context.getApplicationContext(), LoginActivity.class));
    }
}
