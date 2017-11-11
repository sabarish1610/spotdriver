package com.example.suriya.spotdrivers.fragment.car;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.activity.needdriver.DriverCompleteProfileActivity;
import com.example.suriya.spotdrivers.adapter.RecyclerItemClickListener;
import com.example.suriya.spotdrivers.cars.fragment.activity.CarSupportActivity;
import com.example.suriya.spotdrivers.cars.fragment.model.LocationModel;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 13-10-2017.
 */

public class CarListsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RetInterface retInterface;
    private LinearLayoutManager linearLayoutManager;
    private CarListAdapter adapter;
    private String status, mobileNumber;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private LocationModel mLocationModel;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.car_list_recycler_view);
        progressDialog = Singleton.getInstance().getProgressDialog(getActivity());
        toolbar = (Toolbar) view.findViewById(R.id.car_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mLocationModel = Singleton.getInstance().myLocationModel();
        recyclerView.setHasFixedSize(true);
        Bundle bundle = getArguments();
        try {
            status = bundle.getString("FromDriver");
        }catch (NullPointerException e)
        {
         status = "";
        }
        try {
            mobileNumber = bundle.getString("GetDriverCarsList");
        }catch (NullPointerException e)
        {
            mobileNumber = "";
        }

        if (status.equals("FromDriver"))
            toolbar.setTitle("Your Cars");
        else
            toolbar.setTitle("Cars");
        getCarDetailsList();
        //init(view);
        return view;
    }


    private void getCarDetailsList() {
        Singleton.getInstance().progressBar(progressDialog);
        retInterface = Singleton.getInstance().myRetInterface();
        Call<ServerResponse> call = null;
        if (status.equals("FromDriver"))
            call = retInterface.getDriverCarList(mobileNumber);
        else
            call = retInterface.getCarListes(mLocationModel.getLocality(), mLocationModel.getSeatingCapacity());

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                final ServerResponse resp = response.body();
                if (resp.getResult().equals(SupportConstant.SUCCESS))
                {
                    adapter = new CarListAdapter(getActivity(),resp.getCarList());
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (status.equals("FromDriver"))
                                ((IAmDriverActivity)getActivity()).driverCarDetails(resp.getCarList().get(position).getVechileRegisterNumber());
                                else
                            ((CarSupportActivity)getActivity()).moveToEntireDetails(resp.getCarList().get(position).getVechileRegisterNumber());
                        }
                    }));
                }else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

}
