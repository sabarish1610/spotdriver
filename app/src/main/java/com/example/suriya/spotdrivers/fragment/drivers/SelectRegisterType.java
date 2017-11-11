package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.support.SupportConstant;

/**
 * Created by Suriya on 21-10-2017.
 */

public class SelectRegisterType extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_driver_registeration_type, container, false);
       // initi(view);
        view.findViewById(R.id.driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateDriverProfileAcitivity)getActivity()).moveDriverRegisteration(SupportConstant.DRIVER_PROFILE);
            }
        });
        view.findViewById(R.id.driverwithcar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((CreateDriverProfileAcitivity)getActivity()).moveDriverRegisteration(SupportConstant.CAR_PROFILE);
               // ((CreateDriverProfileAcitivity)getActivity()).moveToSelectVechileType();
            }
        });
        return view;
    }
}
