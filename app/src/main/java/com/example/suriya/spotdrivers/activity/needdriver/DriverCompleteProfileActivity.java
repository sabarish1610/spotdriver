package com.example.suriya.spotdrivers.activity.needdriver;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.chat.chatroomfragment.activity.ChattingRoom;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.example.suriya.spotdrivers.support.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.suriya.spotdrivers.retrofit.ApiClient.getApiClient;

public class DriverCompleteProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView userName, experience, cost, age, gender, mobileNumber, licenceType,
            licenceNumber, preferredLocality, jobType;
    private ImageView driverProfilePic;
    private RetInterface retrofit;
    private Button callButton,chattingRoom;
    private String userId,mobile;
    private ServerResponse ser;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_complete_profile);
        Bundle extras = getIntent().getExtras();
        if (extras !=null)
        {
             mobile = extras.getString(SupportConstant.MOBILE_NUMBER,null);
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID,null);
        progressDialog = Singleton.getInstance().getProgressDialog(DriverCompleteProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        init();
        getDriverDetails(mobile);
       // Log.i("mobile",mobile);
    }
    private void init() {
        callButton = (Button) findViewById(R.id.callbutton);
        chattingRoom =(Button) findViewById(R.id.chat_button);
        callButton.setOnClickListener(this);
        chattingRoom.setOnClickListener(this);
        driverProfilePic = (ImageView) findViewById(R.id.user_profile_photo);
        progressBar = (ProgressBar) findViewById(R.id.image_loading);
        driverProfilePic.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_profile_name);
        experience = (TextView) findViewById(R.id.complete_profile_experience);
        cost = (TextView) findViewById(R.id.driver_profile_cost);
        age = (TextView) findViewById(R.id.in_driver_age);
        gender = (TextView) findViewById(R.id.in_driver_gender);
        mobileNumber = (TextView) findViewById(R.id.in_driver_mobile);
        licenceType = (TextView) findViewById(R.id.in_driver_licence_type);
        licenceNumber = (TextView) findViewById(R.id.in_driver_licence_number);
        preferredLocality = (TextView) findViewById(R.id.in_driver_locality);
        jobType = (TextView) findViewById(R.id.in_driver_job_type);
    }

    private void getDriverDetails(String mobile) {
        progressDialog.show();
        retrofit= getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.getCompleteDriverProfile(mobile);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                 ser = response.body();
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(ser.getDriver().getProfile_image_path()).listener(new RequestListener<String, GlideDrawable>() {
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
                }).into(driverProfilePic);
                userName.setText(ser.getDriver().getName()+" "+ser.getDriver().getLast_name());
                experience.setText("with "+ser.getDriver().getExperience()+" years of Experience");
                cost.setText("Cost : "+ser.getDriver().getCharges());
                age.setText("Age : "+ser.getDriver().getAge());
                gender.setText("Gender : " +ser.getDriver().getGender());
                mobileNumber.setText("Mobile Number : "+ser.getDriver().getMobile_number());
                licenceType.setText("Licence Type : "+ser.getDriver().getDriver_licence_type());
                licenceNumber.setText("Licence Number : "+ser.getDriver().getDriver_licence_number());
                preferredLocality.setText("Prefered Locations : "+ser.getDriver().getLocation_1()+", "+ser.getDriver().getLocation_2());
                jobType.setText("Job Type : "+ser.getDriver().getDriver_job_catagory());
                progressDialog.dismiss();
               // Toast.makeText(DriverCompleteProfileActivity.this, ser.getMessage(), Toast.LENGTH_SHORT).show();
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
            case R.id.callbutton:
               // Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
                boolean result = Utility.checkCallPermission(DriverCompleteProfileActivity.this);
                if (result)
                uploadCallDetails();
                break;
            case R.id.chat_button:
                getIntoChattingRoom();
                break;
            case R.id.user_profile_photo:
                showImage();
                break;
        }
    }

    private void showImage() {
        final Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View image = getLayoutInflater().inflate(R.layout.image_layout
                , null);
        final ProgressBar progress = (ProgressBar) image.findViewById(R.id.imageloading);
        progress.setVisibility(View.VISIBLE);
        ImageView imageSet = (ImageView) image.findViewById(R.id.view_image);
        Glide.with(getApplicationContext()).load(ser.getDriver().getProfile_image_path()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(imageSet);
        settingsDialog.setContentView(image);

        settingsDialog.show();
        final ImageView bu = (ImageView) image.findViewById(R.id.cancel);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss();
            }
        });
    }

    private void uploadCallDetails()
    {
        retrofit = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.uploadCallDetails(userId,mobile);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
                if (ser.getResult().contentEquals(SupportConstant.SUCCESS))
                    makeCall();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
    private void getIntoChattingRoom() {
        retrofit = getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.createChatRoom(userId, mobile);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().equals("success")){
                    Intent chattingRoom = new Intent(getApplicationContext(), ChattingRoom.class);
                    chattingRoom.putExtra(SupportConstant.SEND_TO_USER_ID, mobile);
                    chattingRoom.putExtra(SupportConstant.CHAT_ROOM_ID, resp.getUser().getChatRoomId());
                    chattingRoom.putExtra(SupportConstant.FIRST_NAME, ser.getDriver().getName());
                    chattingRoom.putExtra(SupportConstant.IMAGE_PATH, ser.getDriver().getProfile_image_path());
                    startActivity(chattingRoom);

                }
                  /*  fragment = new ChattingRoom();
                Bundle bundle = new Bundle();
                bundle.putString(SupportConstant.SEND_TO_USER_ID,sendtoUserId);
                bundle.putString(SupportConstant.CHAT_ROOM_ID,resp.getUsers().getChatRoomId());
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.register,fragment);
                ft.commit();*/
               // Toast.makeText(getActivity(), resp.getUsers().getChatRoomId(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

        }

    private void makeCall() {

        String call = ser.getDriver().getMobile_number();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(call)));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}
