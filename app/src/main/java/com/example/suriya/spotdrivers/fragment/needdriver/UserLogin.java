package com.example.suriya.spotdrivers.fragment.needdriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.needdriver.CreateNeedDriverProfile;
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

public class UserLogin extends Fragment implements View.OnClickListener {
    private Button signIn;
    private TextView signUp, forgetPassword;
    private EditText userPhoneNumber, userPassword;
    private RetInterface retInterface;
    private ProgressBar progressBar;

    public UserLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.need_driver, container, false);
        //Intili the button and textview
        init(view);
        return view;

    }

    /**
     * This function using to Initilaze the Button and textview
     * @param view
     */
    private void init(View view) {
        signIn = (Button)view.findViewById(R.id.need_driver_login);
        signUp = (TextView)view.findViewById(R.id.need_driver_new_user);
        userPhoneNumber = (EditText)view.findViewById(R.id.need_driver_phone);
        userPassword = (EditText)view.findViewById(R.id.need_driver_password_login);
        forgetPassword = (TextView) view.findViewById(R.id.need_driver_forget_password);
        progressBar = (ProgressBar) view.findViewById(R.id.user_spinner);
        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.need_driver_login:
               // getActivity().startActivity(new Intent(getActivity(), NeedDriverActivity.class));
                String sUserPhoneNumber = userPhoneNumber.getText().toString().trim();
                String sPassword = userPassword.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                userLoginProcess(sUserPhoneNumber, sPassword);
                break;
            case R.id.need_driver_new_user:
                //getActivity().startActivity(new Intent(getActivity(), CreateNeedDriverProfile.class));
                OTPIntent(SupportConstant.NEW_USER);
                break;
            case R.id.need_driver_forget_password:
                OTPIntent(SupportConstant.FORGET_PASSWORD);
                break;
        }
    }

    private void OTPIntent(String OTPType) {
        Intent intent = new Intent(getActivity(), SmsActivity.class);
        intent.putExtra(SupportConstant.USER_TYPE, SupportConstant.USER);
        intent.putExtra(SupportConstant.OTP_TYPE, OTPType);
        getActivity().startActivity(intent);
    }

    private void userLoginProcess(String sUserPhoneNumber, String sPassword) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.FCM_SHARE,Context.MODE_PRIVATE);
        String fcmId= sharedPreferences.getString(SupportConstant.FCM_ID, null);
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.loginForUser(sUserPhoneNumber, sPassword, fcmId);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                if(ser.getResult().equals(SupportConstant.SUCCESS))
                {
                   SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SupportConstant.LOGGED_IN_STATUS,true);
                    editor.putString(SupportConstant.LOGGEDIN_TYPE,SupportConstant.USER);
                    editor.putString(SupportConstant.LOGGEDIN_USERID,ser.getUser().getMobile());
                    editor.commit();
                    progressBar.setVisibility(View.INVISIBLE);
                   // Singleton.getInstance().myPreference().storeUserLoginDetails(ser, SupportConstant.USER);

                    getActivity().startActivity(new Intent(getActivity(), NeedDriverActivity.class));
                }else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), ser.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(), ser.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
}
