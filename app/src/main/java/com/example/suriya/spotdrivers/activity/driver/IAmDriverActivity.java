package com.example.suriya.spotdrivers.activity.driver;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.chat.chatroomfragment.ChatRooms;
import com.example.suriya.spotdrivers.fragment.car.CarListsFragment;
import com.example.suriya.spotdrivers.fragment.car.EntireDetailsOfCar;
import com.example.suriya.spotdrivers.fragment.drivers.DriverMe;

public class IAmDriverActivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityReceiverListener {
    private Fragment carList, driverCarDetails, driverChatRooms;
    private FragmentTransaction ftCarList, ftCarDriverDetails, ftDriverChatRooms;
    private BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_driver);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.user_chat);
        carList = new CarListsFragment();
        driverCarDetails = new EntireDetailsOfCar();
        //chatRoomFragment();
        checkConnection();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.user_chat:
                    chatRoomFragment();
                    return true;
                case R.id.driver_profile:
                   fragmentMe();
                    return true;
            }
            return false;
        }

    };
    public void visibl()
    {
        navigation.setVisibility(View.VISIBLE);
    }
    public void driverCarDetails(String carNumber)
    {
        Bundle bundle = new Bundle();
        bundle.putString("carNumber",carNumber);
        bundle.putString("FromDriver", "FromDriver");
        driverCarDetails.setArguments(bundle);
        ftCarDriverDetails = getFragmentManager().beginTransaction();
        ftCarDriverDetails.replace(R.id.content, driverCarDetails).addToBackStack("").commit();
    }
    private void chatRoomFragment() {
        driverChatRooms = new ChatRooms();
        ftDriverChatRooms = getFragmentManager().beginTransaction();
        ftDriverChatRooms.replace(R.id.content, driverChatRooms).commit();
    }
    public void carList(String sMobileNumber)
    {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("FromDriver", "FromDriver");
            bundle.putString("GetDriverCarsList", sMobileNumber);
            carList.setArguments(bundle);
            if(!(getFragmentManager().getBackStackEntryCount() <= 0))
                getFragmentManager().popBackStack();
            ftCarList = getFragmentManager().beginTransaction();
            ftCarList.replace(R.id.content, carList).addToBackStack("").commit();
            navigation.setVisibility(View.GONE);

        }catch (IllegalStateException e)
        {

        }
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() <= 0){
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void fragmentMe() {
        Fragment fragment = new DriverMe(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment).commit();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReciever.isConnected();
        showSnack(isConnected);

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
    public void progressBar(ProgressDialog progress)
    {
       // progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
