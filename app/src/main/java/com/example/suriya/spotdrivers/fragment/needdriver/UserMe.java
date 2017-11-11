package com.example.suriya.spotdrivers.fragment.needdriver;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.services.MyFireBaseMessageService;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 24-07-2017.
 */

public class UserMe extends Fragment implements View.OnClickListener {
    private TextView name, age, mobileNumber, emergencyNumber1, emergencyNumber2, gender, tv_message;
    private ImageView proficPic,passwordChange;
    private String sMobileNumber;
    private ProgressBar progress;
    private RetInterface retrofit;
    private EditText et_old_password, et_new_password;
    private  SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private BroadcastReceiver broadcastReceiver;
    private ProgressDialog progressDialog;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.need_driver_me_fragment, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        sMobileNumber = sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID,null);
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        progressBar = (ProgressBar) view.findViewById(R.id.driver_profile_pic);
         collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        broadcastReceiverFunction();
        init(view);
        return view;
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
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void init(View view) {
        progressDialog = new ProgressDialog(getActivity());
        Singleton.getInstance().progressBar(progressDialog);
        //((NeedDriverActivity)getActivity()).progressBar(progressDialog);
        //name = (TextView)view.findViewById(R.id.logged_in_user_name);
        age = (TextView) view.findViewById(R.id.logged_in_user_age);
        mobileNumber = (TextView)view.findViewById(R.id.logged_in_user_mobile_number);
        emergencyNumber1 = (TextView) view.findViewById(R.id.logged_in_user_emerg1);
        emergencyNumber2 = (TextView) view.findViewById(R.id.logged_in_user_emerg2);
        gender = (TextView) view.findViewById(R.id.logged_in_user_gender);
        proficPic = (ImageView) view.findViewById(R.id.logged_in_user_profile_pic);
        passwordChange = (ImageView) view.findViewById(R.id.user_change_password);
        passwordChange.setOnClickListener(this);
        getDetails(sMobileNumber);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                logout();
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

    private void getDetails(String sMobileNumber) {
        progressDialog.show();
        retrofit = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse>  call = retrofit.getUserProfileCompletly(sMobileNumber);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                progressDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(ser.getUserDetails().getImage_path()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).into(proficPic);
                collapsingToolbarLayout.setTitle(ser.getUserDetails().getName()+" "+ser.getUserDetails().getLast_name());
                age.setText("Age : "+ser.getUserDetails().getAge());
                mobileNumber.setText("Mobile Number : "+ser.getUserDetails().getUser_mobile());
                emergencyNumber1.setText("Emergency Contact 1 : "+ser.getUserDetails().getEmergency_contact_1());
                emergencyNumber2.setText("Emergency Contact 2 : "+ser.getUserDetails().getEmergency_contact_2());
                gender.setText("Gender : " +ser.getUserDetails().getGender());
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
            case R.id.user_change_password:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
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
       // dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(sMobileNumber, old_password, new_password);

                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePasswordProcess(String sMobileNumber, String old_password, String new_password) {
        retrofit = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.changeUserPassword(sMobileNumber, old_password, new_password);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                if (ser.getResult().contentEquals(SupportConstant.SUCCESS))
                {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), ser.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(ser.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
}
