package com.example.suriya.spotdrivers.introslide.prefManager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Suriya on 27-10-2017.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_DRIVER_LAUNCH = "IsFirstTimeDriverLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setFirstTimeDriverLaunch(boolean isFirstTimeDriver)
    {
        editor.putBoolean(IS_FIRST_TIME_DRIVER_LAUNCH, isFirstTimeDriver);
        editor.commit();
    }
    public boolean isFirstTimeDriverLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_DRIVER_LAUNCH, true);
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
