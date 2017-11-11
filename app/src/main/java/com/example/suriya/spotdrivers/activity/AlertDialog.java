package com.example.suriya.spotdrivers.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 07-08-2017.
 */

public class AlertDialog extends Activity {
    private RetInterface retInterface;
    private EditText et_old_password, et_new_password;
    private android.app.AlertDialog dialog;
    private TextView tv_message;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alert);
        Intent intent = getIntent();
        final String mobileNumber = intent.getStringExtra(SupportConstant.MOBILE_NUMBER);
        final String userType = intent.getStringExtra(SupportConstant.USER_TYPE);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText) view.findViewById(R.id.et_old_password);
        et_new_password = (EditText) view.findViewById(R.id.et_new_password);
        et_old_password.setHint("Enter Password");
        et_new_password.setHint("Confirm Password");
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_old_password.getText().toString();
                String confirmPassword = et_new_password.getText().toString();
                if (!password.isEmpty() && !confirmPassword.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(mobileNumber,userType,confirmPassword);

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePasswordProcess(String mobileNumber, String userType, String confirmPassword) {
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = null;
        if (userType.contentEquals(SupportConstant.DRIVER))
           call = retInterface.resetDriverPassword(mobileNumber,confirmPassword,userType);
        else if (userType.contentEquals(SupportConstant.USER))
            call = retInterface.resetUserPassword(mobileNumber,confirmPassword,userType);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                progress.setVisibility(View.INVISIBLE);
                if (SupportConstant.SUCCESS.contentEquals(ser.getResult()))
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);

            }
        });
    }
}