package com.example.suriya.spotdrivers.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.adapter.ViewpagerAdapter;
import com.example.suriya.spotdrivers.fragment.drivers.DriverLogin;
import com.example.suriya.spotdrivers.fragment.needdriver.UserLogin;
import com.example.suriya.spotdrivers.support.LoggedInDetails;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

public class LoginActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityReceiverListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewpagerAdapter viewpagerAdapter;
    private static final int REQUEST_CODE_PERMISSION = 2;
    //String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    String call = Manifest.permission.CALL_PHONE;
    String message = Manifest.permission.READ_SMS;
    String recMessage = Manifest.permission.RECEIVE_SMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //toolbarTab = (Toolbar) findViewById(R.id.toolbarTab);
        //setSupportActionBar(toolbarTab);
        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        viewPager=(ViewPager)findViewById(R.id.viewpager_layout);
        viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());
        viewpagerAdapter.addFragments(new DriverLogin(),"I am Driver");
        viewpagerAdapter.addFragments(new UserLogin(),"Need Driver");
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences sharesPreference = getSharedPreferences(SupportConstant.LOGGED_IN,Context.MODE_PRIVATE);
        try {
            if (ActivityCompat.checkSelfPermission(this, recMessage)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{call,message,recMessage},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sharesPreference.getBoolean(SupportConstant.LOGGED_IN_STATUS,false))
        {
            if (sharesPreference.getString(SupportConstant.LOGGEDIN_TYPE,null).equals(SupportConstant.DRIVER))
                startActivity(new Intent(this, IAmDriverActivity.class));
            else if (sharesPreference.getString(SupportConstant.LOGGEDIN_TYPE,null).equals(SupportConstant.USER))
                startActivity(new Intent(this, NeedDriverActivity.class));
        }
      /*if(Singleton.getInstance().myPreference().retrieveLoginDetails()!=null)
       {
           LoggedInDetails loggedInDetails = null;
           if (loggedInDetails.getLogginedInType().contentEquals(SupportConstant.DRIVER))
               startActivity(new Intent(this, IAmDriverActivity.class));
           else if (loggedInDetails.getLogginedInType().contentEquals(SupportConstant.USER))
               startActivity(new Intent(this, NeedDriverActivity.class));
       }*/

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReciever.isConnected();
        showSnack(isConnected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                SharedPreferences shared = getSharedPreferences(SupportConstant.LOGGED_IN,MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.clear().commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log","onResume");
        Singleton.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log","onRestart");
        finish();
    }
}
