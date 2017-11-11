package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.adapter.CustomArrayAdapter;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suriya on 17-07-2017.
 */

public class DrivingLicenceTypeAndLocation {
    //private ListView mListView;
    /*private RadioGroup driverLicenceTypeRadio;
    private RadioButton driverLicenceTypeRadioButton;
    private Spinner state, city, locality1, locality2;
    private Toolbar toolbar;
   // private String sState, sCity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driving_licence_type,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.driver_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
       init(view);
       // populateListView();
        return view;
    }

    private void init(View view) {
       // mListView = (ListView)view.findViewById(R.id.known_check_list);
        driverLicenceTypeRadio = (RadioGroup)view.findViewById(R.id.radio_driving_licence_type);
        state = (Spinner) view.findViewById(R.id.state_spinner);
        city = (Spinner) view.findViewById(R.id.city_spinner);
        locality1 =(Spinner) view.findViewById(R.id.preferred_locality_1);
        locality2 = (Spinner) view.findViewById(R.id.preferred_locality_2);
        state.setOnItemSelectedListener(this);
        city.setOnItemSelectedListener(this);
        locality1.setOnItemSelectedListener(this);
        locality2.setOnItemSelectedListener(this);
    }

   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void populateListView() {
        String[] list = {"LMV", "LMV-NT", "LMV-TR", "HMV", "HPMW","HTV", "TRAILOR"};
        ArrayAdapter<String> adpter = new CustomArrayAdapter(getActivity(),list);
        mListView.setAdapter(adpter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem upload_car_image) {
        switch (upload_car_image.getItemId())
        {
            case R.id.next:
               submit();
                return true;
        }
        return super.onOptionsItemSelected(upload_car_image);
    }
    private void getLicenceType()
    {
        String sState = state.getSelectedItem().toString();
        String sCity = city.getSelectedItem().toString();
        String sLocality1 = locality1.getSelectedItem().toString();
        String sLocality2 = locality2.getSelectedItem().toString();
        Log.d("state", sState);
        Log.d("city", sCity);
        Log.d("locality1", sLocality1);
        Log.d("locality2", sLocality2);
        int selectedId = driverLicenceTypeRadio.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        driverLicenceTypeRadioButton = (RadioButton) getView().findViewById(selectedId);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.PROFILE_SHARING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SupportConstant.DRIVING_LICENCE_TYPE,driverLicenceTypeRadioButton.getText().toString());
        editor.putString(SupportConstant.STATE, sState);
        editor.putString(SupportConstant.CITY, sCity);
        editor.putString(SupportConstant.LOCALITY_1, sLocality1);
        editor.putString(SupportConstant.LOCALITY_2, sLocality2);
        editor.commit();

    }
    private void submit() {
        if(driverLicenceTypeRadio.getCheckedRadioButtonId() == SupportConstant.FLAG_RADIO_GROUP)
        {Toast.makeText(getActivity(), "Please Select Licence type of Yours", Toast.LENGTH_SHORT).show();return;}
        if (city.getSelectedItemPosition()<0)
        {
            Toast.makeText(getActivity(), "Please Select City", Toast.LENGTH_SHORT).show();
            return;
        }
        if(CustomArrayAdapter.checkAccumulator == 0)
        {
            Toast.makeText(getActivity(), "please Select", Toast.LENGTH_SHORT).show();
            return;
        }
        getLicenceType();
       // ((CreateDriverProfileAcitivity)getActivity()).driverProofFragment();
    }*/


  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       String State = parent.getItemAtPosition(position).toString();
        if (State.contentEquals("Tamil Nadu"))
        {

            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),
                    R.array.city_spinner, android.R.layout.simple_spinner_item);
            adapter2.setNotifyOnChange(true);
            city.setAdapter(adapter2);

        }
        String City = parent.getItemAtPosition(position).toString();
        showCity(City);
    }

    private void showCity(String selectedCity) {
        switch (selectedCity)
        {
            case "Chennai":
                ArrayAdapter chennai = ArrayAdapter.createFromResource(getActivity(),
                        R.array.chennai_spinner, android.R.layout.simple_spinner_item);
                chennai.setNotifyOnChange(true);
                locality1.setAdapter(chennai);
                locality2.setAdapter(chennai);
                break;
            case "Coimbatore":
                ArrayAdapter kovi = ArrayAdapter.createFromResource(getActivity(),
                        R.array.kovi_spinner, android.R.layout.simple_spinner_item);
                kovi.setNotifyOnChange(true);
                locality1.setAdapter(kovi);
                locality2.setAdapter(kovi);
                break;
            case "Madurai":

                ArrayAdapter madurai = ArrayAdapter.createFromResource(getActivity(),
                        R.array.madurai_spinner, android.R.layout.simple_spinner_item);
                madurai.setNotifyOnChange(true);
                locality1.setAdapter(madurai);
                locality2.setAdapter(madurai);
                break;
            case "Tiruchirappalli":

                ArrayAdapter Tiruchirappalli = ArrayAdapter.createFromResource(getActivity(),
                        R.array.tiruchirappalli_spinner, android.R.layout.simple_spinner_item);
                Tiruchirappalli.setNotifyOnChange(true);
                locality1.setAdapter(Tiruchirappalli);
                locality2.setAdapter(Tiruchirappalli);
                break;
            case "Salem":

                ArrayAdapter Salem = ArrayAdapter.createFromResource(getActivity(),
                        R.array.salem_spinner, android.R.layout.simple_spinner_item);
                Salem.setNotifyOnChange(true);
                locality1.setAdapter(Salem);
                locality2.setAdapter(Salem);
                break;
            case "Erode":

                ArrayAdapter Erode = ArrayAdapter.createFromResource(getActivity(),
                        R.array.erode_spinner, android.R.layout.simple_spinner_item);
                Erode.setNotifyOnChange(true);
                locality1.setAdapter(Erode);
                locality2.setAdapter(Erode);
                break;
            case "Tuticorin":

                ArrayAdapter Tuticorin = ArrayAdapter.createFromResource(getActivity(),
                        R.array.tuticorin_spinner, android.R.layout.simple_spinner_item);
                Tuticorin.setNotifyOnChange(true);
                locality1.setAdapter(Tuticorin);
                locality2.setAdapter(Tuticorin);
                break;
            case "Krishnagiri":

                ArrayAdapter Krishnagiri = ArrayAdapter.createFromResource(getActivity(),
                        R.array.krishnagiri_spinner, android.R.layout.simple_spinner_item);
                Krishnagiri.setNotifyOnChange(true);
                locality1.setAdapter(Krishnagiri);
                locality2.setAdapter(Krishnagiri);
                break;
            case "Vellore":

                ArrayAdapter Vellore = ArrayAdapter.createFromResource(getActivity(),
                        R.array.vellore_spinner, android.R.layout.simple_spinner_item);
                Vellore.setNotifyOnChange(true);
                locality1.setAdapter(Vellore);
                locality2.setAdapter(Vellore);
                break;
            case "Dindigul":

                ArrayAdapter Dindigul = ArrayAdapter.createFromResource(getActivity(),
                        R.array.dindigul_spinner, android.R.layout.simple_spinner_item);
                Dindigul.setNotifyOnChange(true);
                locality1.setAdapter(Dindigul);
                locality2.setAdapter(Dindigul);
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
