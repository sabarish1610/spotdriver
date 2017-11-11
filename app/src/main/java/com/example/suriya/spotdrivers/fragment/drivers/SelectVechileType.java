package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.fragment.car.Model;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

/**
 * Created by Suriya on 27-10-2017.
 */

public class SelectVechileType extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup selectVechileType;
    private Model model;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_vechile_type_fragment, container, false);
        selectVechileType = (RadioGroup) view.findViewById(R.id.reg_select_vechile_type);
        model = Singleton.getInstance().myCarModel();
        selectVechileType.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rb=(RadioButton)getView().findViewById(checkedId);
        model.setVechileType(rb.getText().toString());
     //   Toast.makeText(getActivity(), model.getVechileType(), Toast.LENGTH_SHORT).show();
       // ((CreateDriverProfileAcitivity)getActivity()).moveDriverRegisteration(SupportConstant.CAR_PROFILE);
        ((CreateDriverProfileAcitivity)getActivity()).moveToCarDetails();
    }
}
