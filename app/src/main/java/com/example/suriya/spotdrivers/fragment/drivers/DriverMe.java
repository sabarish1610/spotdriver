package com.example.suriya.spotdrivers.fragment.drivers;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentBreadCrumbs;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.Notification.Notification;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.services.MyFireBaseMessageService;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by Suriya on 24-07-2017.
 */

public class DriverMe extends Fragment implements View.OnClickListener{
    private ImageView loggedInDriverProfilePic;
    private TextView userName, experience, cost, age, gender, mobileNumber, licenceType, tv_message,
            licenceNumber, preferredLocality, jobType, viewCar;
    private String sMobileNumber;
    private RetInterface retInterface;
    private EditText et_old_password, et_new_password;
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;
   // private ProgressBar progress;
    BroadcastReceiver broadcastReceiver;
    private RetInterface retrofit;
    private ProgressDialog progressDialog;
   // private CollapsingToolbarLayout collapsingToolbarLayout;
   // private ProgressBar progressBar;
    private Activity activity;
    public DriverMe(Activity activity)
    {
        this.activity = activity;
    }

    /**
     * source code
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_me_fragment, container, false);
        init(view);
        setHasOptionsMenu(true);
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.driver_toolbar);
        toolbar.setTitle("Profile");
        ((IAmDriverActivity)getActivity()).visibl();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.driver_change_password:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void init(View view) {
        progressDialog = new ProgressDialog(getActivity());
        ((IAmDriverActivity)getActivity()).progressBar(progressDialog);
        sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        sMobileNumber = sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID, null);
        loggedInDriverProfilePic = (ImageView) view.findViewById(R.id.logged_in_profile_photo);
        userName = (TextView) view.findViewById(R.id.driver_collapsing);
       // progressBar = (ProgressBar) view.findViewById(R.id.driver_load_me_progress_bar);
      //  collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.driver_collapsing);
        experience = (TextView) view.findViewById(R.id.logged_in_driver_experience);
        cost = (TextView) view.findViewById(R.id.logged_in_driver_profile_cost);
        age = (TextView) view.findViewById(R.id.logged_in_driver_age);
        gender = (TextView) view.findViewById(R.id.logged_in_driver_gender);
        mobileNumber = (TextView) view.findViewById(R.id.logged_in_driver_mobile);
        licenceType = (TextView) view.findViewById(R.id.logged_in_driver_licence_type);
        licenceNumber = (TextView) view.findViewById(R.id.logged_in_driver_licence_number);
        preferredLocality = (TextView) view.findViewById(R.id.logged_in_driver_locality);
        jobType = (TextView) view.findViewById(R.id.logged_in_driver_job_type);
        viewCar = (TextView) view.findViewById(R.id.view_car_profile);
        viewCar.setOnClickListener(this);
        broadcastReceiverFunction();
        getProfile(sMobileNumber);
    }

    private void getProfile(String sMobileNumber) {
        progressDialog.show();
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.getCompleteDriverProfile(sMobileNumber);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                progressDialog.dismiss();
//                progressBar.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(ser.getDriver().getProfile_image_path()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                       // progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                       // progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(loggedInDriverProfilePic);
                if (ser.getDriver().getWithCar() == 1)
                    viewCar.setVisibility(View.VISIBLE);
                userName.setText(ser.getDriver().getName() + " " + ser.getDriver().getLast_name());
                experience.setText("with " + ser.getDriver().getExperience() + " years of Experience");
                cost.setText(ser.getDriver().getCharges());
                age.setText(ser.getDriver().getAge());
                gender.setText(ser.getDriver().getGender());
                mobileNumber.setText(ser.getDriver().getMobile_number());
                licenceType.setText(ser.getDriver().getDriver_licence_type());
                licenceNumber.setText(ser.getDriver().getDriver_licence_number());
                preferredLocality.setText(ser.getDriver().getLocation_1()+", "+ser.getDriver().getLocation_2());
                jobType.setText(ser.getDriver().getDriver_job_catagory());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
    private void broadcastReceiverFunction() {
        final Notification notification = new Notification(getActivity());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("push"))
                {
                    notification.pushNotification(MyFireBaseMessageService.title,MyFireBaseMessageService.message);
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter("push"));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
       // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText) view.findViewById(R.id.et_old_password);
        et_new_password = (EditText) view.findViewById(R.id.et_new_password);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        //progress = (ProgressBar) view.findViewById(R.id.progress);
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
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {

                    //progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(sMobileNumber, old_password, new_password);

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePasswordProcess(String sMobileNumber, String old_password, String new_password) {
        retrofit = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.changeDriverPassword(sMobileNumber, old_password, new_password);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                if (ser.getResult().contentEquals(SupportConstant.SUCCESS)) {
                    //progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), ser.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                  //  progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(ser.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.view_car_profile:
                ((IAmDriverActivity)getActivity()).carList(sMobileNumber);
                break;
        }
    }
}