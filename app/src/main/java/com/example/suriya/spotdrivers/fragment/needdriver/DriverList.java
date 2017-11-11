package com.example.suriya.spotdrivers.fragment.needdriver;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.Notification.Notification;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.needdriver.DriverCompleteProfileActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.adapter.RecyclerAdapter;
import com.example.suriya.spotdrivers.adapter.RecyclerItemClickListener;
import com.example.suriya.spotdrivers.fragment.FilterLocation;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.services.MyFireBaseMessageService;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.DriverDetails;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 17-07-2017.
 */

public class DriverList extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ServerResponse serverResponse;
    private RetInterface retInterface;
    private BroadcastReceiver broadcastReceiver;
    //private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_list_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.driver_list_recycler_view);
        //progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.drivers_list_toolbar);
        toolbar.setTitle("Drivers");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        progressDialog = Singleton.getInstance().getProgressDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getDriverList();
        broadcastReceiverFunction();
        setHasOptionsMenu(true);
        return view;
    }

    private void broadcastReceiverFunction() {
        final Notification notification = new Notification(getActivity());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("push"))
                {
                    notification.pushNotification(MyFireBaseMessageService.title,MyFireBaseMessageService.message);
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter("push"));

    }

        @Override
        public void onPause() {
            super.onPause();
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        }


    private void getDriverList() {
        //progressBar.setVisibility(View.VISIBLE);
        Singleton.getInstance().progressBar(progressDialog);
        //((NeedDriverActivity)getActivity()).progressBar(progressDialog);
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
       // SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        Call<ServerResponse> call = null;
        if (call == null)
            call = retInterface.getDriverDetailsFromServer();

      /*  final String sFilteredCity = sharedPreferences.getString(SupportConstant.FILTERED_CITY, null);
        final String sFilteresLocality = sharedPreferences.getString(SupportConstant.FILTERED_LOCALITY, null);
        if(!sharedPreferences.getBoolean(SupportConstant.FILTER_APPLIED_STATUS,false))
        {
            call = retInterface.getDriverDetailsFromServer();
        }else {
           call = retInterface.filterDrivers(sFilteredCity, sFilteresLocality,"localityBase");
        }*/
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                serverResponse = response.body();
                if (serverResponse.getResult().contentEquals(SupportConstant.SUCCESS)) {
                    Log.d("driverList", String.valueOf(serverResponse));
                   // progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                    adapter = new RecyclerAdapter(getActivity(), serverResponse.getDriverList());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                       /* SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mobile_number", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mobile",driverDetailsList.get(position).getMobile_number());
                        editor.commit();
                        getActivity().startActivity(new Intent(getActivity(), DriverCompleteProfileActivity.class));*/
                            //Toast.makeText(getActivity(), driverDetailsList.get(position).getMobile_number(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), DriverCompleteProfileActivity.class);
                            intent.putExtra(SupportConstant.MOBILE_NUMBER, serverResponse.getDriverList().get(position).getMobile_number());
                            Toast.makeText(getActivity(), serverResponse.getDriverList().get(position).getMobile_number(), Toast.LENGTH_SHORT).show();
                            getActivity().startActivity(intent);

                        }
                    }));
                }else if (serverResponse.getResult().contentEquals(SupportConstant.FAILURE))
                {
                    progressDialog.dismiss();
                 //   call = retInterface.filterDrivers(sFilteredCity, sFilteresLocality, "city");
                   // diloge(call);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //progressBar.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();

            }
        });
    }

    private void diloge(final Call<ServerResponse> call) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Uncomment the below code to Set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage(getString(R.string.locality_oops));
        builder.setCancelable(false);
        builder.setPositiveButton("Filter by City", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        serverResponse = response.body();
                        if (serverResponse.getResult().contentEquals(SupportConstant.SUCCESS)) {
                           // progressBar.setVisibility(View.INVISIBLE);
                            adapter = new RecyclerAdapter(getActivity(), serverResponse.getDriverList());
                            recyclerView.setAdapter(adapter);
                            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), DriverCompleteProfileActivity.class);
                                    intent.putExtra(SupportConstant.MOBILE_NUMBER, serverResponse.getDriverList().get(position).getMobile_number());
                                    Toast.makeText(getActivity(), serverResponse.getDriverList().get(position).getMobile_number(), Toast.LENGTH_SHORT).show();
                                    getActivity().startActivity(intent);

                                }
                            }));
                        }else if(serverResponse.getResult().contentEquals(SupportConstant.FAILURE)){
                            dilogFinal();
                        }

                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                    }
                });
            }
        });
        builder.setNegativeButton("Search by another location", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                ((NeedDriverActivity)getActivity()).fragmentFilter();
            }
        });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!!!");
        alert.show();

    }

    private void dilogFinal() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle("Alert Dialog");

        // Setting Dialog Message
        alertDialog.setMessage("No data Available");

        // Setting Icon to Dialog
       // alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                ((NeedDriverActivity)getActivity()).fragmentFilter();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.location_filter,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {

            case R.id.location:
                ((NeedDriverActivity)getActivity()).fragmentFilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
