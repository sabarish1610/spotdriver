package com.example.suriya.spotdrivers.fragment.car;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.Arrays;
import java.util.List;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
/**
 * Created by Suriya on 06-10-2017.
 */

public class AboutCar extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Spinner selectSpinnerCategory, categorySpinnerModel;
    private HintSpinner<String> carCategory, categoryModel;
    private EditText eotherCategory, eotherCategoryModel, ecarNumber, evechileModel, evechileSeatType;
    private Button completeDriverDetails;
    private List cat;
    private RadioButton acNacButton, fuelTypeButton;
    private RadioGroup acNac, fuelType, regCarSeatingCapacity, regVanSeatingCapacity, regBusSeatingCapacity;
    private Model carModel;
    private static int VISIBILITY;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_car, container, false);
        initi(view);
        return view;
    }

    private void initi(View view) {
        /*
        getting objects
         */
        carModel = Singleton.getInstance().myCarModel();
        cat = Arrays.asList(getResources().getStringArray(R.array.brand_category));
        /*
        spinner
         */
        selectSpinnerCategory = (Spinner)view.findViewById(R.id.carCategory);
        categorySpinnerModel = (Spinner) view.findViewById(R.id.category_model);
        /*
        EditText
         */
        eotherCategory = (EditText) view.findViewById(R.id.otherCategory);
        eotherCategoryModel = (EditText) view.findViewById(R.id.otherCategoryModel);
        ecarNumber = (EditText) view.findViewById(R.id.car_number);
        evechileModel = (EditText) view.findViewById(R.id.car_model);
       // evechileSeatType = (EditText) view.findViewById(R.id.car_seating_capacity);
        /*
        Button
         */
        completeDriverDetails = (Button) view.findViewById(R.id.car_details);
        completeDriverDetails.setOnClickListener(this);
        /*
        radio group
         */
        acNac = (RadioGroup) view.findViewById(R.id.travel_with);
        fuelType = (RadioGroup) view.findViewById(R.id.fuel_type);
        regCarSeatingCapacity = (RadioGroup) view.findViewById(R.id.reg_car_seating_capacity);
        regVanSeatingCapacity = (RadioGroup) view.findViewById(R.id.reg_select_van_capcity);
        regBusSeatingCapacity = (RadioGroup) view.findViewById(R.id.reg_select_bus_capcity);
        regCarSeatingCapacity.setOnCheckedChangeListener(this);
        regVanSeatingCapacity.setOnCheckedChangeListener(this);
        regBusSeatingCapacity.setOnCheckedChangeListener(this);
        if (carModel.getVechileType().equals(SupportConstant.CAR))
        regCarSeatingCapacity.setVisibility(View.VISIBLE);
        else if (carModel.getVechileType().equals(SupportConstant.VAN))
        regVanSeatingCapacity.setVisibility(View.VISIBLE);
        else if (carModel.getVechileType().equals(SupportConstant.BUS))
            regBusSeatingCapacity.setVisibility(View.VISIBLE);

        //model = Singleton.getInstance().myModel();
        carCategory = new HintSpinner<>(selectSpinnerCategory, new HintAdapter(getActivity(), "Please Select Category", cat), new HintSpinner.Callback() {
            @Override
            public void onItemSelected(int position, Object itemAtPosition) {
                carModel.setsCatagoryofCar(itemAtPosition.toString());
                changeCategoryModel(itemAtPosition.toString());
            }
        });
        carCategory.init();
    }
    private void reload()
    {
        if (VISIBILITY == 1)
        {
            eotherCategory.setVisibility(View.GONE);
            categorySpinnerModel.setVisibility(View.VISIBLE);
            eotherCategoryModel.setVisibility(View.INVISIBLE);
            VISIBILITY = 0;
        }
    }
    private void changeCategoryModel(String categoryModel) {
        List<String> catModel;
        switch (categoryModel)
        {
            case "Tata":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Tata));
                changeSpinnerModel(catModel);
                //  if ()
                reload();
                break;
            case "Maruti Suzuki":
                catModel = Arrays.asList(getResources().getStringArray(R.array.maruti_suzuki));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Toyota":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Toyoto));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Hyundai":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Hyundai));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Mahindra":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Mahindra));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Honda":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Honda));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Chevrolet":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Chevrolet));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Ford":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Ford));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Sukoda":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Skoda));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Volkswagen":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Volkswagen));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Fiat":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Fiat));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Nissan":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Nissan));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Renault":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Renault));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Mitsubishi":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Mitsubishi));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Mercedes-Benz":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Mercedes_Benz));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Audi":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Audi));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Mahindra Renault":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Mahindra_Renault));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "BMW":
              /*  catModel = Arrays.asList(getResources().getStringArray(R.array.));
                changeSpinnerModel(catModel);*/
                break;
            case "Hindustan Motors":
                catModel = Arrays.asList(getResources().getStringArray(R.array.Hindustan_Motors));
                changeSpinnerModel(catModel);
                reload();
                break;
            case "Other Brands":
                VISIBILITY = 1;
                eotherCategory.setVisibility(View.VISIBLE);
                categorySpinnerModel.setVisibility(View.GONE);
                eotherCategoryModel.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void changeSpinnerModel(final List catModelList)
    {
        categoryModel = new HintSpinner<>(categorySpinnerModel, new HintAdapter<String>(getActivity(), "Please Select Model", catModelList), new HintSpinner.Callback<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                carModel.setsModelofCar(itemAtPosition.toString());
            }
        });
        categoryModel.init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.car_details:
                completeCarDetails();
                break;
        }
    }

    private void completeCarDetails() {
        getDetailfromEditText();
        if(carModel.getsCatagoryofCar() == null)
        {
            Singleton.getInstance().myTost(getActivity(), "Please Select Car Catagory");
            return;
        }
        if (carModel.getsModelofCar() == null)
        {
            Singleton.getInstance().myTost(getActivity(), "Please Select Car Model");
            return;
        }
        if (carModel.getsCarRegisterNumberofCar().equals(""))
        {
            Singleton.getInstance().myTost(getActivity(), "Please Enter car Register Number");
            return;
        }
        if (acNac.getCheckedRadioButtonId() == -1)
        {
            Singleton.getInstance().myTost(getActivity(), "Please Select Ac or Non Ac");
            return;
        }
        if (fuelType.getCheckedRadioButtonId() == -1)
        {
            Singleton.getInstance().myTost(getActivity(), "Please Select Fuel Type");
            return;
        }if (carModel.getsCarManufacturingofCar().equals(""))
        {
            Singleton.getInstance().myTost(getActivity(), "Please Enter Car Model");
            return;
        }if (carModel.getsSeatingCapacityofCar().equals(""))
        {
            Singleton.getInstance().myTost(getActivity(), "Please Enter Seating capacity of Car");
            return;
        }
        Log.d("singleton", carModel.getsCatagoryofCar());
        int travelWithSelectedItem, petrolTypeItem;
        travelWithSelectedItem = acNac.getCheckedRadioButtonId();
        petrolTypeItem = fuelType.getCheckedRadioButtonId();
        acNacButton = (RadioButton) getView().findViewById(travelWithSelectedItem);
        fuelTypeButton = (RadioButton) getActivity().findViewById(petrolTypeItem);
       // carModel.setsMobileNumber("8904087257");
        carModel.setsTravelWithType(acNacButton.getText().toString());
        carModel.setsFuelTypeofCar(fuelTypeButton.getText().toString());
        ((CreateDriverProfileAcitivity)getActivity()).moveToAboutPrice();

    }

    private void getDetailfromEditText() {
        carModel.setsCarRegisterNumberofCar(ecarNumber.getText().toString());
        carModel.setsCarManufacturingofCar(evechileModel.getText().toString());
        if (carModel.getsCatagoryofCar() == getResources().getString(R.string.other_brand))
        {
            carModel.setsCatagoryofCar(eotherCategory.getText().toString());
            carModel.setsModelofCar(eotherCategoryModel.getText().toString());
        }else if(carModel.getsModelofCar() == getResources().getString(R.string.other)){
            carModel.setsModelofCar(eotherCategoryModel.getText().toString());
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rb = (RadioButton) getView().findViewById(checkedId);
        carModel.setsSeatingCapacityofCar(rb.getText().toString());

    }
}
