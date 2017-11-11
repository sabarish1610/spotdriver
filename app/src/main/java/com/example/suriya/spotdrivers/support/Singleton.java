package com.example.suriya.spotdrivers.support;

import android.app.Application;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.cars.fragment.model.LocationModel;
import com.example.suriya.spotdrivers.fragment.car.*;
import com.example.suriya.spotdrivers.fragment.car.Model;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;

/**
 * Created by Suriya on 17-07-2017.
 * This class is used control the creation of the object
 */

public class Singleton extends Application {
    /*
    getting class name
     */
public static final String TAG = Singleton.class.getSimpleName();
    // creating class object
    private static Singleton mSingleton;
    private MySharedPreference mySharedPreference;
    private RetInterface retInterface;
    SharedPreferences.Editor editor;
    //class instance will be initialized on app launch
    private com.example.suriya.spotdrivers.fragment.car.Model carModel;
    private ProgressDialog progressDialog;
    private LocationModel mLocationModel;
    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mSingleton = this;
    }
    //Public static method to get the instance of this class

    /**
     *
     * @return
     */
    public static synchronized Singleton getInstance() {
        if(mSingleton == null)
        mSingleton = new Singleton();
        return mSingleton;
    }

    /**
     *
     * @param listener
     */
    public void setConnectivityListener(ConnectivityReciever.ConnectivityReceiverListener listener) {
        ConnectivityReciever.connectivityReceiverListener = listener;
    }

    /**
     *
      */
    public Model myCarModel()
    {
        if (carModel == null)
            carModel = new Model();
        return carModel;
    }
    public LocationModel myLocationModel()
    {
        if(mLocationModel == null)
            mLocationModel = new LocationModel();
        return mLocationModel;
    }
    public void myTost(Context context, String toastMessage)
    {
         Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
    public RetInterface myRetInterface()
    {
        if (retInterface == null)
        {
            retInterface = ApiClient.getApiClient().create(RetInterface.class);
        }
        return retInterface;
    }
    public ProgressDialog getProgressDialog(Context context)
    {
            progressDialog = new ProgressDialog(context);
        return progressDialog;
    }
    public void progressBar(ProgressDialog progress)
    {
        // progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }
}
