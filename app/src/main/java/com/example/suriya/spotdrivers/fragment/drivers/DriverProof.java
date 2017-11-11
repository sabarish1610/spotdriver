package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.support.SupportConstant;

/**
 * Created by Suriya on 17-07-2017.
 */

public class DriverProof extends Fragment implements View.OnClickListener {
    private final String TAG = DriverProof.class.getSimpleName();
    private ImageView licenceFront,licenceBack,otherProofFront,otherProofBack;
    private TextView drivingLicenceFront, drivingLicenceBack, otherProofFrontSelect, otherProofBackSelect;
    private String sLicenceFront, sLicenceBack, sOtherFront, sOtherBack;
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_proof_upload,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.driver_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        init(view);
        return view;
    }

    private void init(View view) {

        licenceFront = (ImageView)view.findViewById(R.id.licence_front_image) ;
        licenceBack = (ImageView)view.findViewById(R.id.licence_back_image);
        otherProofFront = (ImageView)view.findViewById(R.id.other_proof_front);
        otherProofBack = (ImageView)view.findViewById(R.id.other_proof_back);
        drivingLicenceFront = (TextView)view.findViewById(R.id.licence_proof_front);
        drivingLicenceBack = (TextView)view.findViewById(R.id.licence_proof_back);
        otherProofFrontSelect = (TextView)view.findViewById(R.id.other_proof_front_text);
        otherProofBackSelect = (TextView)view.findViewById(R.id.other_proof_back_text);
        drivingLicenceFront.setOnClickListener(this);
        drivingLicenceBack.setOnClickListener(this);
        otherProofFrontSelect.setOnClickListener(this);
        otherProofBackSelect.setOnClickListener(this);
        Log.d(TAG,"init ++");
     }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.next:
                next();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void next() {
        if (sLicenceFront == null)
        {
            Toast.makeText(getActivity(), "Please upload front licence photo", Toast.LENGTH_SHORT).show();
            return;
        }else if (sLicenceBack == null)
        {
            Toast.makeText(getActivity(), "Please upload back licence photo", Toast.LENGTH_SHORT).show();
            return;
        }else if (sOtherFront == null)
        {
            Toast.makeText(getActivity(), "Please upload front other proof photo", Toast.LENGTH_SHORT).show();
            return;
        }else if(sOtherBack == null)
        {
            Toast.makeText(getActivity(), "Please upload back other proof photo", Toast.LENGTH_SHORT).show();
            return;
        }
        passingValues();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_profile, new CompleteProfile()).addToBackStack( "tag" ).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.licence_proof_front:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_LICENCE_FRONT);
                break;
            case R.id.licence_proof_back:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_LICENCE_BACK);
                break;
            case R.id.other_proof_front_text:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_OTHER_PROOF_FRONT);
                break;
            case R.id.other_proof_back_text:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_OTHER_PROOF_BACK);
                break;
        }
    }
   public void lincenceFront(Bitmap bitmap, String encryptedImage)
    {
        this.sLicenceFront = encryptedImage;
        licenceFront.setImageBitmap(bitmap);
        Log.d("linc_front",encryptedImage);
    }
    public void lincenceBack(Bitmap bitmap, String encryptedImage)
    {
        this.sLicenceBack = encryptedImage;
        licenceBack.setImageBitmap(bitmap);
        Log.d("linc_back",encryptedImage);
    }
    public void setOtherProofFront(Bitmap bitmap, String encryptedImage)
    {
        this.sOtherFront = encryptedImage;
     otherProofFront.setImageBitmap(bitmap);
        Log.d("linc_oth_front",encryptedImage);
    }
    public void setOtherProofBack(Bitmap bitmap, String encryptedImage)
    {
        this.sOtherBack = encryptedImage;
        otherProofBack.setImageBitmap(bitmap);
        Log.d("linc_oth_back",encryptedImage);
    }
    private void passingValues()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.PROFILE_SHARING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SupportConstant.LICENCE_FRONT,sLicenceFront);
        editor.putString(SupportConstant.LICENCE_BACK,sLicenceBack);
        editor.putString(SupportConstant.OTHER_FRONT,sOtherFront);
        editor.putString(SupportConstant.OTHER_BACK,sOtherBack);
        editor.commit();
    }
}
