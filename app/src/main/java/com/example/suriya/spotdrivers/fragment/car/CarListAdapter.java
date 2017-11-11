package com.example.suriya.spotdrivers.fragment.car;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.retrofit.support.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suriya on 13-10-2017.
 */

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.RecyclerViewHolder> {
    Context context;
    List<CarDetails> carDetailses;
   private CarListAdapter.RecyclerViewHolder holder;
    private SharedPreferences sharedPreferences;
    public CarListAdapter(Context context, List<CarDetails> driverDetailses ) {
        this.context = context;
        this.carDetailses = driverDetailses;

    }

    @Override
    public CarListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_car_details,parent,false);
       holder = new CarListAdapter.RecyclerViewHolder(view);
        return holder;
    }
    /*
    set the text
     */
    @Override
    public void onBindViewHolder(final CarListAdapter.RecyclerViewHolder holder, final int position) {

        holder.carCatagoryModel.setText(carDetailses.get(position).getVechileCatagory().toString()+"-"+carDetailses.get(position).getVechileCatagoryModel().toString());
        holder.driverName.setText(carDetailses.get(position).getDriverName().toString()+"."+carDetailses.get(position).getDriverLastName().toUpperCase().toString());
        holder.carLuxuryType.setText(carDetailses.get(position).getCarLuxuryType().toString());
        holder.carNumber.setText(carDetailses.get(position).getVechileRegisterNumber().toString());
        holder.carFuelType.setText(carDetailses.get(position).getCarFuelType().toString());
        holder.carLocality.setText(carDetailses.get(position).getPrefferedLocality().toString()+" "+carDetailses.get(position).getPrefferedCity().toString());
        holder.carDriverExp.setText(carDetailses.get(position).getDriverExp().toString()+" yrs Exp");
        holder.carDriverAge.setText("Age "+carDetailses.get(position).getDriverAge().toString());
       // holder.carModel.setText("Model "+carDetailses.get(position).getCarModel().toString());
        holder.carSeatingCapacity.setText("Seating "+carDetailses.get(position).getVechileSeat().toString());
        holder.supportCarImagesList.add(carDetailses.get(position).getCarFrontView());
        holder.supportCarImagesList.add(carDetailses.get(position).getCarBackView());
        holder.supportCarImagesList.add(carDetailses.get(position).getCarInnerView());
        holder.supportCarImagesList.add(carDetailses.get(position).getCarLeftSide());
        holder.supportCarImagesList.add(carDetailses.get(position).getCarRightSide());
      /*  holder.imageAdapter = new ImageAdapter(context, holder.supportCarImagesList,1);
        holder.recyclerView.setAdapter(holder.imageAdapter);*/
      for(int i =0; i<holder.carImage.length;i++)
      {
          Glide.with(context).load(holder.supportCarImagesList.get(i)).fitCenter().listener(new RequestListener<String, GlideDrawable>() {
              @Override
              public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                  // holder.progressBar.setVisibility(View.INVISIBLE);
                  return false;
              }

              @Override
              public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                  //holder.progressBar.setVisibility(View.INVISIBLE);
                  return false;
              }
          }).into(holder.carImage[i]);
      }

    }

    @Override
    public int getItemCount() {
        return carDetailses.size() ;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        List<String> supportCarImagesList = new ArrayList<String>();
        TextView carCatagoryModel, driverName, carLuxuryType, carNumber, carFuelType, carLocality,
        carDriverExp, carDriverAge, carModel, carSeatingCapacity;
       ImageView[] carImage = new ImageView[5];
     /*   RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        ImageAdapter imageAdapter;*/
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            carCatagoryModel = (TextView) itemView.findViewById(R.id.get_catagory_models);
            driverName = (TextView) itemView.findViewById(R.id.car_list_driver_name);
            carLuxuryType = (TextView) itemView.findViewById(R.id.car_luxury_type);
            carNumber = (TextView) itemView.findViewById(R.id.list_car_register_number);
            carFuelType = (TextView) itemView.findViewById(R.id.list_fuel_type);
            carLocality = (TextView) itemView.findViewById(R.id.list_locality);
            carDriverExp = (TextView) itemView.findViewById(R.id.car_driver_exp);
            carDriverAge = (TextView) itemView.findViewById(R.id.car_driver_age);
            carModel = (TextView) itemView.findViewById(R.id.car_model);
            carSeatingCapacity = (TextView) itemView.findViewById(R.id.carSeatingCapacity);
            carImage[0] = (ImageView) itemView.findViewById(R.id.car_front_view);
            carImage[1] = (ImageView) itemView.findViewById(R.id.car_back_view);
            carImage[2] = (ImageView) itemView.findViewById(R.id.car_inner_view);
            carImage[3] = (ImageView) itemView.findViewById(R.id.side_view_1);
            carImage[4] = (ImageView) itemView.findViewById(R.id.side_view_2);
            /**
             *
             */
           /* recyclerView = (RecyclerView) itemView.findViewById(R.id.list_car_list);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);*/
        }
    }
}
