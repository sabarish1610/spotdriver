package com.example.suriya.spotdrivers.fragment.drivers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.activity.otp.SmsActivity;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 16-07-2017.
 */

public class DriverLogin extends Fragment implements View.OnClickListener{
    private Button signIn;
    private TextView signUp, forgetPassword;
    private EditText driverPhoneNumber, driverPassword;
    private RetInterface retInreface;
    private ProgressBar progressBar;
    public DriverLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().getSupportActionBar().hide();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.i_am_driver, container, false);
        initView(view);
        return view;
    }
/*
intilize Button and TextView
 */
    private void initView(View view) {
        driverPhoneNumber = (EditText)view.findViewById(R.id.driver_phone_login);
        driverPassword = (EditText)view.findViewById(R.id.driver_password_login);
        signIn = (Button)view.findViewById(R.id.driver_login);
        signUp = (TextView)view.findViewById(R.id.i_am_driver_new_user);
        forgetPassword = (TextView)view.findViewById(R.id.i_am_driver_forget_password);
        progressBar = (ProgressBar) view.findViewById(R.id.driverSpinner);
        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.driver_login:
                String sDriverPhoneNumber = driverPhoneNumber.getText().toString().trim();
                String sDriverPassword = driverPassword.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
               /* SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SupportConstant.LOGGED_IN_STATUS,true);
                editor.putString(SupportConstant.LOGGEDIN_TYPE,"driver");
                editor.commit();
                getActivity().startActivity(new Intent(getActivity(), IAmDriverActivity.class));*/
               driverLoginProcess(sDriverPhoneNumber, sDriverPassword);
                break;
            case R.id.i_am_driver_new_user:
               //getActivity().startActivity(new Intent(getActivity(), CreateDriverProfileAcitivity.class));
                 OTPIntent(SupportConstant.NEW_USER);
              //  getActivity().startActivity(new Intent(getActivity(), SmsActivity.class));
                break;
            case R.id.i_am_driver_forget_password:
                OTPIntent(SupportConstant.FORGET_PASSWORD);
                break;
        }
    }

    private void OTPIntent(String OTPType) {
        Intent intent = new Intent(getActivity(), SmsActivity.class);
        intent.putExtra(SupportConstant.USER_TYPE, SupportConstant.DRIVER);
        intent.putExtra(SupportConstant.OTP_TYPE, OTPType);
        getActivity().startActivity(intent);
    }

    private void driverLoginProcess(String sDriverPhoneNumber, String sDriverPassword) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.FCM_SHARE,Context.MODE_PRIVATE);
        String fcmId= sharedPreferences.getString(SupportConstant.FCM_ID, null);
        retInreface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInreface.loginForDriver(sDriverPhoneNumber, sDriverPassword, fcmId);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse res = response.body();
                if (res.getResult().equals(SupportConstant.SUCCESS))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SupportConstant.LOGGED_IN_STATUS,true);
                    editor.putString(SupportConstant.LOGGEDIN_TYPE,SupportConstant.DRIVER);
                    editor.putString(SupportConstant.LOGGEDIN_USERID,res.getDriver().getMobile_number());
                    editor.commit();
                   // Toast.makeText(getActivity(), res.getUser().getMobile(), Toast.LENGTH_SHORT).show();
                   // Singleton.getInstance().myPreference().storeUserLoginDetails(res,SupportConstant.DRIVER);
                    getActivity().startActivity(new Intent(getActivity(), IAmDriverActivity.class));
                }else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
