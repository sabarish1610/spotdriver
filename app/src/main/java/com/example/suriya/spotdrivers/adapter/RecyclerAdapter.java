package com.example.suriya.spotdrivers.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.retrofit.support.DriverDetails;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sabarish on 22/5/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Context context;
    List<DriverDetails> driverDetailses;
    private RecyclerViewHolder holder;
    private SharedPreferences sharedPreferences;
    public RecyclerAdapter(Context context, List<DriverDetails> driverDetailses ) {
        this.context = context;
        this.driverDetailses = driverDetailses;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_driver_list,parent,false);
        holder = new RecyclerViewHolder(view);
        return holder;
    }
/*
set the text
 */
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        holder.driverName.setText(driverDetailses.get(position).getName()+" "+driverDetailses.get(position).getLast_name());
        holder.driverExperience.setText(driverDetailses.get(position).getExperience());
        holder.driverCharges.setText("₹ : "+driverDetailses.get(position).getCharges());
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(driverDetailses.get(position).getProfile_image_path()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return driverDetailses.size() ;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImageView;
        TextView driverName, driverExperience, driverCharges;
        ProgressBar progressBar;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.driver_profile_pic);
            driverName = (TextView) itemView.findViewById(R.id.driver_name);
            driverExperience = (TextView) itemView.findViewById(R.id.experience_num);
            driverCharges = (TextView) itemView.findViewById(R.id.show_driver_charges);
            progressBar = (ProgressBar) itemView.findViewById(R.id.driver_list_profile);
        }
    }
}
