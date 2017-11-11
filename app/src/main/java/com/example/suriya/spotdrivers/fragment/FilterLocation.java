package com.example.suriya.spotdrivers.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.fragment.needdriver.DriverList;
import com.example.suriya.spotdrivers.support.SupportConstant;

/**
 * Created by Suriya on 04-08-2017.
 */

public class FilterLocation extends Fragment implements AdapterView.OnItemSelectedListener{
    private Spinner filterCity, filterLocality;
    private Button applyFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment,container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        filterCity = (Spinner) view.findViewById(R.id.filter_city);
        filterLocality = (Spinner) view.findViewById(R.id.filter_locality);
        applyFilter = (Button) view.findViewById(R.id.apply_filter);


        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCity = filterCity.getSelectedItem().toString();
                String sLocality = filterLocality.getSelectedItem().toString();
                Log.d("filter_city", sCity);
                Log.d("filter_locality", sLocality);
                SharedPreferences sharedPreference = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putBoolean(SupportConstant.FILTER_APPLIED_STATUS, true);
                editor.putString(SupportConstant.FILTERED_CITY, sCity);
                editor.putString(SupportConstant.FILTERED_LOCALITY, sLocality);
                editor.commit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.need_driver_content,new DriverList()).commit();
            }
        });
        filterCity.setOnItemSelectedListener(this);
        filterLocality.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String city = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), city, Toast.LENGTH_SHORT).show();
        showCity(city);
    }

    private void showCity(String city) {
      /*  switch (city)
        {
            case "Chennai":
                ArrayAdapter chennai = ArrayAdapter.createFromResource(getActivity(),
                        R.array.chennai_spinner, android.R.layout.simple_spinner_item);
                chennai.setNotifyOnChange(true);
                filterLocality.setAdapter(chennai);
                break;
            case "Coimbatore":
                ArrayAdapter kovi = ArrayAdapter.createFromResource(getActivity(),
                        R.array.kovi_spinner, android.R.layout.simple_spinner_item);
                kovi.setNotifyOnChange(true);
                filterLocality.setAdapter(kovi);
                break;
            case "Madurai":

                ArrayAdapter madurai = ArrayAdapter.createFromResource(getActivity(),
                        R.array.madurai_spinner, android.R.layout.simple_spinner_item);
                madurai.setNotifyOnChange(true);
                filterLocality.setAdapter(madurai);
                break;
            case "Tiruchirappalli":

                ArrayAdapter Tiruchirappalli = ArrayAdapter.createFromResource(getActivity(),
                        R.array.tiruchirappalli_spinner, android.R.layout.simple_spinner_item);
                Tiruchirappalli.setNotifyOnChange(true);
                filterLocality.setAdapter(Tiruchirappalli);
                break;
            case "Salem":

                ArrayAdapter Salem = ArrayAdapter.createFromResource(getActivity(),
                        R.array.salem_spinner, android.R.layout.simple_spinner_item);
                Salem.setNotifyOnChange(true);
                filterLocality.setAdapter(Salem);
                break;
            case "Erode":

                ArrayAdapter Erode = ArrayAdapter.createFromResource(getActivity(),
                        R.array.erode_spinner, android.R.layout.simple_spinner_item);
                Erode.setNotifyOnChange(true);
                filterLocality.setAdapter(Erode);
                break;
            case "Tuticorin":

                ArrayAdapter Tuticorin = ArrayAdapter.createFromResource(getActivity(),
                        R.array.tuticorin_spinner, android.R.layout.simple_spinner_item);
                Tuticorin.setNotifyOnChange(true);
                filterLocality.setAdapter(Tuticorin);
                break;
            case "Krishnagiri":

                ArrayAdapter Krishnagiri = ArrayAdapter.createFromResource(getActivity(),
                        R.array.krishnagiri_spinner, android.R.layout.simple_spinner_item);
                Krishnagiri.setNotifyOnChange(true);
                filterLocality.setAdapter(Krishnagiri);
                break;
            case "Vellore":

                ArrayAdapter Vellore = ArrayAdapter.createFromResource(getActivity(),
                        R.array.vellore_spinner, android.R.layout.simple_spinner_item);
                Vellore.setNotifyOnChange(true);
                filterLocality.setAdapter(Vellore);
                break;
            case "Dindigul":

                ArrayAdapter Dindigul = ArrayAdapter.createFromResource(getActivity(),
                        R.array.dindigul_spinner, android.R.layout.simple_spinner_item);
                Dindigul.setNotifyOnChange(true);
                filterLocality.setAdapter(Dindigul);
                break;

        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
