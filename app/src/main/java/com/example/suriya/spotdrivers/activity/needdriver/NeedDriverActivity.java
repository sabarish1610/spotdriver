package com.example.suriya.spotdrivers.activity.needdriver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.cars.fragment.SelectAndStartRide;
import com.example.suriya.spotdrivers.cars.fragment.activity.CarSupportActivity;
import com.example.suriya.spotdrivers.chat.chatroomfragment.ChatRooms;
import com.example.suriya.spotdrivers.fragment.FilterLocation;
import com.example.suriya.spotdrivers.fragment.needdriver.DriverList;
import com.example.suriya.spotdrivers.fragment.needdriver.UserMe;
import com.example.suriya.spotdrivers.gpssupport.Jobbo;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedDriverActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReciever.ConnectivityReceiverListener {
    private Fragment driverListFrament;
    public BottomNavigationView navigation;
    private CircleImageView profilePic;
    private SharedPreferences sharedPreferences;
    private TextView userName, userMobileNumber, tv_message;
    private EditText et_old_password, et_new_password;
    private String sMobileNumber;
    private RetInterface retrofit;
    private ProgressBar progress;
    private AlertDialog dialog;
   // private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_driver);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        sharedPreferences = getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        sMobileNumber = sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID,null);
        profilePic = (CircleImageView)header.findViewById(R.id.user_profilr_pic);
        userName = (TextView)header.findViewById(R.id.user_name_name);
        userMobileNumber = (TextView) header.findViewById(R.id.user_mobile_number_number);
        //userName.setText("sabarish");
         getDetails(sMobileNumber);
/**
 * set profile pic
 */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


       // mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String in = intent.getStringExtra("notif");
        Log.d("iddddddd", String.valueOf(Jobbo.getIntoCarLocationPage));
        if(SupportConstant.GET_DETAIL_FOR_FROM == intent.getIntExtra(SupportConstant.GET_DETAIL_FOR_FROM_STRING,0))
        {

            navigation.setSelectedItemId(R.id.car_details);
            return;
        }
        if (in != null)
        {
            navigation.setSelectedItemId(R.id.driver_chat);
        }else
        {
            navigation.setSelectedItemId(R.id.drivers);
        }

    }

    private void getDetails(String sMobileNumber) {
        //progressDialog.show();
        retrofit = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retrofit.getUserProfileCompletly(sMobileNumber);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse ser = response.body();
             //   progressDialog.dismiss();
             //   progressBar.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(ser.getUserDetails().getImage_path()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                      //  progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                      //  progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).into(profilePic);
                userName.setText(ser.getUserDetails().getName()+" "+ser.getUserDetails().getLast_name());
                //ser.getUserDetails().getName()+" "+ser.getUserDetails().getLast_name()
               // age.setText("Age : "+ser.getUserDetails().getAge());
                userMobileNumber.setText(ser.getUserDetails().getUser_mobile());
               // emergencyNumber1.setText("Emergency Contact 1 : "+ser.getUserDetails().getEmergency_contact_1());
               // emergencyNumber2.setText("Emergency Contact 2 : "+ser.getUserDetails().getEmergency_contact_2());
               // gender.setText("Gender : " +ser.getUserDetails().getGender());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.car_details:
                    fragmentCarDetails();
                    return true;
                case R.id.drivers:
                    fragmentDriverList();
                    return true;
                case R.id.driver_chat:
                    fragmentChat();
                    return true;
                /*case R.id.user_profile:
                   fragmentMe();
                    return true;*/
            }
            return false;
        }

    };
    public void fragmentCarDetails() {
        Fragment fragment = new SelectAndStartRide();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.need_driver_content,fragment).commit();

    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }*/
    public void carSupportActivity(Context context)
    {
        Intent intent  = new Intent(context, CarSupportActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void fragmentChat() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.need_driver_content,new ChatRooms()).commit();
    }

    private void fragmentMe() {
        Fragment fragment = new UserMe();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.need_driver_content, fragment).commit();
    }

    public void fragmentDriverList()
    {
        driverListFrament = new DriverList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.need_driver_content, driverListFrament);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void fragmentFilter()
    {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.need_driver_content, new FilterLocation()).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar upload_car_image clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view upload_car_image clicks here.
        int id = item.getItemId();

        if (id == R.id.car_fragment) {
            navigation.setSelectedItemId(R.id.car_details);
        } else if (id == R.id.driver_fragment) {
            navigation.setSelectedItemId(R.id.drivers);
        } else if (id == R.id.chat_fragment) {
            navigation.setSelectedItemId(R.id.driver_chat);
        }else if (id == R.id.password_change) {
            showDialog(NeedDriverActivity.this);

        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    private void showDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getLayoutInflater();
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
        dialog.show();
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
                    Toast.makeText(getApplicationContext(), ser.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log","onResume");
        Singleton.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
       if (isConnected)
           Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        else
           Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }
}
