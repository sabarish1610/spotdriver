package com.example.suriya.spotdrivers.fragment.car;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.google.android.gms.vision.text.Text;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

/**
 * Created by Suriya on 11-10-2017.
 */

public class CarImages extends Fragment {
    ArrayList<Uri> path = new ArrayList<>();
    private ImageView selectImage;
    private TextView signUp;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageAdapter imageAdapter;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_photo_uploads, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //mainController = new ImageController(this, imageView);
        imageAdapter = new ImageAdapter(getActivity(), //mainController,
                path);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageAdapter);
        init(view);
        return view;
    }

    private void init(View view) {
        //Bitmap img;
        selectImage = (ImageView) view.findViewById(R.id.selectImage);
        signUp = (TextView) view.findViewById(R.id.sign_up);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/blackjack.otf");
        signUp.setTypeface(tf);
        // imageView = (ImageView) findViewById(R.id.imageImg);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateDriverProfileAcitivity)getActivity()).getImageFromFisherMan("carImage");
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateDriverProfileAcitivity)getActivity()).sendCarDetailsToServer(getActivity());
            }
        });
    }
public void imagePath(ArrayList<Uri> path)
{
    this.path = path;
    imageAdapter.changePath(this.path);
}
}
