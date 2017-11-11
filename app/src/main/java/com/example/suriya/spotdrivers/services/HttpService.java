package com.example.suriya.spotdrivers.services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.activity.needdriver.CreateNeedDriverProfile;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 01-08-2017.
 */

public class HttpService extends IntentService {
    private RetInterface retInterface;
    private SharedPreferences sharedPreferences;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * 
     */
    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sharedPreferences = getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        String mobileNumber = null;
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            Log.d("service otp", otp);
            String otpType = sharedPreferences.getString(SupportConstant.OTP_TYPE, null);
            if (otpType.contentEquals(SupportConstant.NEW_USER))
            mobileNumber = sharedPreferences.getString(SupportConstant.NEWLY_REGISTERING_MOBILE_NUMBER, null);
            else if (otpType.contentEquals(SupportConstant.FORGET_PASSWORD))
                mobileNumber = sharedPreferences.getString(SupportConstant.FORGET_PASSWORD_MOBILE_NUMBER, null);
            String userType = sharedPreferences.getString(SupportConstant.USER_TYPE, null);
           // String otpType = intent.getStringExtra(SupportConstant.OTP_TYPE);
            Log.d("mobotp", otp);
            Log.d("mobusert", userType);
            Log.d("mobotpt", otpType);
            Log.d("mob", mobileNumber);
            verifyOtp(mobileNumber, otp, userType, otpType);
        }
    }

    private void verifyOtp(final String mobileNumber, String otp, final String userType, final String otpType) {
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.verifyOtp(mobileNumber,otp);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
               ServerResponse resp = response.body();
                if (resp.getResult().contentEquals(SupportConstant.SUCCESS))
                {
                    if (otpType.contentEquals(SupportConstant.NEW_USER)) {
                        if (SupportConstant.DRIVER.contentEquals(userType)) {
                            Intent driver = new Intent(HttpService.this, CreateDriverProfileAcitivity.class);
                            driver.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            driver.putExtra(SupportConstant.MOBILE_NUMBER, mobileNumber);
                            startActivity(driver);
                        } else if (SupportConstant.USER.contentEquals(userType)) {
                            Intent user = new Intent(HttpService.this, CreateNeedDriverProfile.class);
                            user.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            user.putExtra(SupportConstant.MOBILE_NUMBER, mobileNumber);
                            startActivity(user);
                        }
                        Toast.makeText(HttpService.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                    }else if (otpType.contentEquals(SupportConstant.FORGET_PASSWORD))
                    {
                        //Toast.makeText(HttpService.this, "Sucess", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(HttpService.this, com.example.suriya.spotdrivers.activity.AlertDialog.class);
                        intent.putExtra(SupportConstant.MOBILE_NUMBER, mobileNumber);
                        intent.putExtra(SupportConstant.USER_TYPE, userType);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

}
