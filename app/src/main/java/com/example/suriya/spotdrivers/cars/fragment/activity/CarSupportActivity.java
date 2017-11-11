package com.example.suriya.spotdrivers.cars.fragment.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.fragment.car.CarListsFragment;
import com.example.suriya.spotdrivers.fragment.car.EntireDetailsOfCar;

public class CarSupportActivity extends AppCompatActivity {
    private Fragment carList, entireDetails;
    private FragmentTransaction carListFt, entireDetailsft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_support);
        carListFt = getFragmentManager().beginTransaction();
        carList = new CarListsFragment();
        carListFt.replace(R.id.car_Support_fragment, carList);
        carListFt.commit();

    }
    public void moveToEntireDetails(String carNumber)
    {
        Bundle bundle = new Bundle();
        entireDetailsft = getFragmentManager().beginTransaction();
        entireDetails = new EntireDetailsOfCar();
        bundle.putString("carNumber",carNumber);
        entireDetails.setArguments(bundle);
        entireDetailsft.replace(R.id.car_Support_fragment, entireDetails).addToBackStack("").commit();
    }
}
