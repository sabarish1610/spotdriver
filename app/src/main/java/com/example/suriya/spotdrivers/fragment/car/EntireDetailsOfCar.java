package com.example.suriya.spotdrivers.fragment.car;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.google.android.gms.vision.text.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 13-10-2017.
 */

public class EntireDetailsOfCar extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    private RetInterface ret;
    private SliderLayout sliderLayout;
    private HashMap<String,String> Hash_file_maps ;
    private int textView[] = new int[26];;
    private TextView textList[];
    private TextView isAllowance, isHaltingCharges;
    private View view;
    private LinearLayout linearLayout;
    private CircleImageView circleImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String carImageFront, carImageBack, carInnerView, carLeftSide, carRightSide, fromDriver;
    private ProgressDialog progressDialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String carNumber = bundle.getString("carNumber","");
        try {
            fromDriver = bundle.getString("FromDriver");
        }catch (NullPointerException e)
        {
            fromDriver = "";
        }
        Singleton.getInstance().myTost(getActivity(), carNumber);
        progressDialog = Singleton.getInstance().getProgressDialog(getActivity());
        Hash_file_maps = new HashMap<String, String>();
         view = inflater.inflate(R.layout.fragment_car_entire_details, container, false);
        circleImageView = (CircleImageView)view.findViewById(R.id.car_driver_profile_pi);
        isHaltingCharges = (TextView) view.findViewById(R.id.is_halting_charges_applicable);
        isAllowance = (TextView) view.findViewById(R.id.is_hills_allowance_applicable);
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.car_details_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.car_image);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        init(view);
        getData(carNumber);
        return view;
    }

    private void init(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.lin);
        if (fromDriver != null)
        if (fromDriver.equals("FromDriver"))
            linearLayout.setVisibility(view.GONE);
        /**
         * textView int
         */
        textList = new TextView[26];
       // TextView text = new TextView(getActivity());
        textView[0] = R.id.car_driver_name;
        textView[1] = R.id.car_driver_detail_age;
        textView[2] = R.id.car_driver_exper;
        textView[3] = R.id.car_brand_name;
        textView[4] = R.id.car_catagory_name;
        textView[5] = R.id.car_register_number;
        textView[6] = R.id.car_travel_type;
        textView[7] = R.id.car_fuel_Type;
        textView[8] = R.id.car_man_model;
        textView[9] = R.id.car_seating;
        textView[10] = R.id.car_locality;
       // textView[11] = R.id.long_trip_catalogue_details;
        textView[12] = R.id.car_limited_km;
        textView[13] = R.id.car_limited_km_pric;
        textView[14] = R.id.car_time_limited;
        textView[15] = R.id.car_exceed_price;
        textView[16] = R.id.local_waiting_charge;
        textView[17] = R.id.car_local_driver_beta;
        textView[18] = R.id.local_trip_catalogue;
        textView[19] = R.id.car_long_trip_per_km;
        textView[20] = R.id.car_long_driver_beta;
        textView[21] = R.id.car_long_waiting_charges;
        textView[22] = R.id.is_halting_charges_applicable;
        textView[23] = R.id.is_halting_charges_price;
        textView[24] = R.id.is_hills_allowance_applicable;
        textView[25] = R.id.hills_allowance_price;
       // textView[2] = R.id.car_driver_exper;
        for(int i=0; i<textList.length; i++)
        {
            textList[i] = (TextView) view.findViewById(textView[i]);
        }


    }

    private void getData(String carNumber) {
        ret = Singleton.getInstance().myRetInterface();
        Call<ServerResponse> cal = ret.getCarCompleteDetails(carNumber);
        cal.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Singleton.getInstance().progressBar(progressDialog);
                ServerResponse res = response.body();
                if (res.getResult().equals(SupportConstant.SUCCESS))
                {
                    progressDialog.dismiss();
                    Glide.with(getActivity()).load(res.getCarDetails().getDriverProfilePic()).fitCenter().listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(circleImageView);
                    collapsingToolbarLayout.setTitle("Mr."+res.getCarDetails().getDriverName()+" "+res.getCarDetails().getDriverLastName());
                    carImageFront = res.getCarDetails().getCarFrontView();
                    carImageBack = res.getCarDetails().getCarBackView();
                    carInnerView = res.getCarDetails().getCarInnerView();
                    carLeftSide = res.getCarDetails().getCarLeftSide();
                    carRightSide = res.getCarDetails().getCarRightSide();
                    //textList[0].setText("Mr. "+res.getCarDetails().getDriverName()+". "+res.getCarDetails().getDriverLastName());
                    textList[1].setText("Age :"+res.getCarDetails().getDriverAge());
                    textList[2].setText("Exp : "+res.getCarDetails().getDriverExp()+" yrs");
                    textList[3].setText("Brand : "+res.getCarDetails().getVechileCatagory());
                    textList[4].setText("Catagory : "+res.getCarDetails().getVechileCatagoryModel());
                    textList[5].setText("Reg.No : "+res.getCarDetails().getVechileRegisterNumber());
                    textList[6].setText("Travel with : "+res.getCarDetails().getCarLuxuryType());

                    textList[7].setText("Fuel : "+res.getCarDetails().getCarFuelType());
                    textList[8].setText("Model :"+res.getCarDetails().getCarModel());
                    textList[9].setText("Seats : "+res.getCarDetails().getVechileSeat());
                    textList[10].setText("Locality : "+res.getCarDetails().getPrefferedLocality()+","+res.getCarDetails().getPrefferedCity());
                    //textList[11].setText("Catagory : "+res.getCarDetails().getVechileCatagoryModel());
                    if(res.getCarDetails().getVechileLocalTrip() == 1) {
                        view.findViewById(R.id.long_trip_catalogue_detail).setVisibility(View.VISIBLE);
                        textList[12].setText("Limited Km per Day : " + res.getCarDetails().getVechileLocalPerDayLimitedKm());
                        textList[13].setText("Limited Km Price Per Day : " + res.getCarDetails().getVechileLocalLimitedKmPrice());
                        textList[14].setText("Limited Time Period " + res.getCarDetails().getVehileLocalLimitedTimePeriodRange());
                        textList[15].setText("Exceed Price :" + res.getCarDetails().getVechileLocalExceedPerKmPrice());
                        textList[16].setText("Waiting Charges : " + res.getCarDetails().getVechileLocalWaitingCharges());
                        textList[17].setText("Driver Beta : " + res.getCarDetails().getVechileLocalDriverBeta());
                    }
                    //textList[18].setText("Catagory : "+res.getCarDetails().getVechileCatagoryModel());
                    if(res.getCarDetails().getVechileLongTravelTrip() == 1) {
                        view.findViewById(R.id.long_trip_details).setVisibility(View.VISIBLE);
                        textList[19].setText("Price per Km : " + res.getCarDetails().getVechile_long_per_km_price());
                        textList[20].setText("Driver Beta : " + res.getCarDetails().getVechileLongDriverBeta());
                        textList[21].setText("Waiting Charges : " + res.getCarDetails().getVechileLongWaitingChargesPerDay());
                        //textList[22].setText("Travel with : "+res.getCarDetails().getVechileLongDriverBeta());
                        if (res.getCarDetails().getVechileHaltingChargesAviable() == 1) {
                            isHaltingCharges.setText("Is halting Charges applicable : Yes");
                            Toast.makeText(getActivity(), "Halting", Toast.LENGTH_SHORT).show();
                            view.findViewById(R.id.is_halting_charges_price).setVisibility(View.VISIBLE);
                            textList[23].setText("Halting Charges : " + res.getCarDetails().getVechileHaltingChargesPrice()+"/night");
                        }
                        // textList[24].setText("Travel with : "+res.getCarDetails().getVechileLongDriverBeta());
                        if (res.getCarDetails().getVechileHillsAllowanceAvailable() == 1) {
                            isAllowance.setText("Is Hills Allowance applicable : Yes");
                            Toast.makeText(getActivity(), "Halting", Toast.LENGTH_SHORT).show();
                            view.findViewById(R.id.hills_allowance_price).setVisibility(View.VISIBLE);
                            textList[25].setText("Hills Allowance : " + res.getCarDetails().getVechileHillsAllowancePrice()+"/Km");
                        }
                    }
                    slideImage(carImageFront, carImageBack, carInnerView, carLeftSide, carRightSide);
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

    private void slideImage(String carImageFront, String carImageBack, String carInnerView,
                            String carLeftSide, String carRightSide) {
        Hash_file_maps.put("Android CupCake", carImageFront);
        Hash_file_maps.put("Android Donut", carImageBack);
        Hash_file_maps.put("Android Eclair", carInnerView);
        Hash_file_maps.put("Android Froyo", carLeftSide);
        Hash_file_maps.put("Android GingerBread", carRightSide);

        for(String name : Hash_file_maps.keySet()){

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }
}
