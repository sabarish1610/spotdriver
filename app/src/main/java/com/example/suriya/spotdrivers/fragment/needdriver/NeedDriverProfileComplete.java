package com.example.suriya.spotdrivers.fragment.needdriver;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.fragment.drivers.CompleteProfile;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 18-07-2017.
 */

public class NeedDriverProfileComplete extends Fragment {
   private String sName, sLastName, sGender, sMobile, sAge, sEmail, sEm1, sEm2, sProfilePic, sPassword,sConfirmPassword, fcmId;
    private EditText password, confirmPassword;
    private TextView errorPassword, errorConfirmPassword;
    private RetInterface reInterface;
    private ProgressDialog progress;
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.need_driver_profile_complete,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.driver_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Bundle bundle = getArguments();
        sName = bundle.getString(SupportConstant.FIRST_NAME,null);
        sLastName = bundle.getString(SupportConstant.LAST_NAME, null);
        sGender = bundle.getString(SupportConstant.GENDER,null);
        sAge = bundle.getString(SupportConstant.AGE,null);
        sMobile = bundle.getString(SupportConstant.MOBILE_NUMBER,null);
        sEmail = bundle.getString(SupportConstant.EMAIL,null);
        sEm1 = bundle.getString(SupportConstant.EMERGENCY_CONTACT_1,null);
        sEm2 = bundle.getString(SupportConstant.EMERGENCY_CONTACT_2, null);
        sProfilePic = bundle.getString(SupportConstant.PROFILE_PIC, null);
        password = (EditText)view.findViewById(R.id.need_driver_password);
        confirmPassword = (EditText)view.findViewById(R.id.need_driver_confirm_password);
        errorPassword = (TextView)view.findViewById(R.id.user_error_password);
        errorConfirmPassword = (TextView)view.findViewById(R.id.user_error_confirm_password);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.FCM_SHARE, Context.MODE_PRIVATE);
        fcmId = sharedPreferences.getString(SupportConstant.FCM_ID, null);
//        Log.d("fcm_id", fcmId);
        Log.d("name",sName);
        Log.d("lastName", sLastName);
        Log.d("profilPic", sProfilePic);
        //
        password.addTextChangedListener(new NeedDriverProfileComplete.MyTextWatcher(password));
        confirmPassword.addTextChangedListener(new NeedDriverProfileComplete.MyTextWatcher(confirmPassword));
        progressBar();
        return view; 
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.finish_profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.finish:
                submit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        progress.show();
        sPassword = password.getText().toString().trim();
        sConfirmPassword = confirmPassword.getText().toString().trim();
        if(!checkingPassword())
        {
            return;
        }
        if(!comparePassword())
        {
            return;
        }
        //uploadToServer(sName, sLastName, sGender, sMobile, sAge, sEmail, sEm1, sEm2, sProfilePic,sConfirmPassword);
    }

  /*  private void uploadToServer(String sName, String sLastName, String sGender, String sMobile, String sAge, String sEmail,
                                String sEm1, String sEm2, String sProfilePic, String sConfirmPassword) {
        reInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = reInterface.insertUserDetails(sName, sLastName, sAge, sGender, sMobile, sEmail,sEm1, sEm2, sConfirmPassword,
                sProfilePic, fcmId);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().contentEquals(SupportConstant.SUCCESS)) {
                    progress.dismiss();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }*/

    private class MyTextWatcher implements TextWatcher {
        View view;
        private MyTextWatcher (View view)
        {
            this.view=view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.need_driver_confirm_password:
                    comparePassword();
                    break;
                case R.id.need_driver_password:
                    checkingPassword();
                    break;
            }


        }
    }
    private boolean checkingPassword()
    {
        sPassword = password.getText().toString().trim();
        if (!isValidPassword(sPassword))
        {
            errorPassword.setText(getString(R.string.password_error));
            errorPassword.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }else {
            errorPassword.setText("");

        }
        return true;
    }
    private boolean comparePassword()
    {
        sConfirmPassword=confirmPassword.getText().toString().trim();
        if(!sPassword.equals(sConfirmPassword)){
            errorConfirmPassword.setText(getString(R.string.confirm_password_error));
            errorConfirmPassword.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        else {
            errorConfirmPassword.setText(getString(R.string.password_match));
            errorConfirmPassword.setTextColor(Color.rgb(0,128,0));
        }

        return true;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    private void progressBar()
    {
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Data's Uploading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }

}
