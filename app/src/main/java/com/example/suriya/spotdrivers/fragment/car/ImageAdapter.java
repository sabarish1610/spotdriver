package com.example.suriya.spotdrivers.fragment.car;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangc on 2015-11-06.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    String Tag = ImageAdapter.class.getSimpleName().toString();
    Context context;
    ArrayList<Uri> imagePaths;
    //ImageController imageController;

    public ImageAdapter(Context context, //ImageController imageController,
                        ArrayList<Uri> imagePaths) {
        this.context = context;
       // this.imageController = imageController;
        this.imagePaths = imagePaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upload_car_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Uri imagePath = imagePaths.get(position);
            Picasso.with(context)
                    .load(imagePath)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
          /*  holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // imageController.setImgMain(imagePath);
                }
            });*/

    }

    public void changePath(ArrayList<Uri> imagePaths) {
        this.imagePaths = imagePaths;
        //imageController.setImgMain(imagePaths.get(0));
        //Log.d("listType", String.valueOf(this.imagePaths));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);

        }
    }
}