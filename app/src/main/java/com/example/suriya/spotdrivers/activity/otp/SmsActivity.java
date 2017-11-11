package com.example.suriya.spotdrivers.activity.otp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.services.HttpService;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.example.suriya.spotdrivers.support.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 02-08-2017.
 */

public class SmsActivity extends AppCompatActivity implements View.OnClickListener {
    private RetInterface retInterface;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp;
    private EditText inputMobile, inputOtp;
    private ProgressBar progressBar;
    //private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    private SharedPreferences  sharedPreference;
    private SharedPreferences.Editor editor;
    private String userType, mobileNumber, otpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);
        //sharedPreference
        sharedPreference = getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);
        //getDetails
        Intent intent = getIntent();
        userType = intent.getStringExtra(SupportConstant.USER_TYPE);
        otpType = intent.getStringExtra(SupportConstant.OTP_TYPE);
        editor.putString(SupportConstant.USER_TYPE, userType);
        editor.putString(SupportConstant.OTP_TYPE, otpType);
        editor.commit();
        Log.d(SupportConstant.USER_TYPE, userType);
        //adapter

        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_sms:
                boolean result = Utility.checkMessagePermission(SmsActivity.this);
                if (result){
                 mobileNumber = inputMobile.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                // validating mobile number
                // it should be of 10 digits length
                if (isValidPhoneNumber(mobileNumber)) {

                    // request for sms
                    progressBar.setVisibility(View.VISIBLE);
                    // requesting for sms
                    if (otpType.contentEquals(SupportConstant.NEW_USER))
                    requestForSMS();
                    else if (otpType.contentEquals(SupportConstant.FORGET_PASSWORD))
                        forgetPassword();

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                }}
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                editor.putBoolean(SupportConstant.WAITING_FOR_SMS, false);
                editor.commit();
               // pref.setIsWaitingForSms(false);
                break;
    }}

    private void forgetPassword() {
        editor.putString(SupportConstant.FORGET_PASSWORD_MOBILE_NUMBER,mobileNumber);
        editor.commit();
        //String userType = "user";
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.otpForForgetPassword(mobileNumber, userType);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().contentEquals(SupportConstant.SUCCESS))
                {
                    editor.putBoolean(SupportConstant.WAITING_FOR_SMS, true);
                    editor.commit();
                    viewPager.setCurrentItem(1);
                    txtEditMobile.setText(sharedPreference.getString(SupportConstant.FORGET_PASSWORD_MOBILE_NUMBER, null));
                    layoutEditMobile.setVisibility(View.VISIBLE);

                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * sending the OTP to server and activating the user
     */

    private void requestForSMS() {
        editor.putString(SupportConstant.NEWLY_REGISTERING_MOBILE_NUMBER,mobileNumber);
        editor.commit();
        String userType = "user";
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.sendOtp(mobileNumber, userType);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().contentEquals(SupportConstant.SUCCESS))
                {
                    editor.putBoolean(SupportConstant.WAITING_FOR_SMS, true);
                    editor.commit();
                    viewPager.setCurrentItem(1);
                    txtEditMobile.setText(sharedPreference.getString(SupportConstant.NEWLY_REGISTERING_MOBILE_NUMBER, null));
                    layoutEditMobile.setVisibility(View.VISIBLE);
                    //Toast.makeText(SmsActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }
}
