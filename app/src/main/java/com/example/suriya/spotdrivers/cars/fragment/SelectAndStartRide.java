package com.example.suriya.spotdrivers.cars.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.cars.fragment.activity.CarSupportActivity;
import com.example.suriya.spotdrivers.cars.fragment.activity.GetLocationFromGps;
import com.example.suriya.spotdrivers.cars.fragment.model.LocationModel;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

/**
 * Created by Suriya on 22-09-2017.
 */

public class SelectAndStartRide extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private TextView  enterFromLocation, pickUpLocationFrom, titleText;
    private RelativeLayout fromLayout;
    private LocationModel mLocationModel;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private int resId;
    private RadioGroup vechileTYpe, carseatingCapacity, vanSeatingCapacity, busSeatingCapacity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_fragment_get_location_ride,container,false);
        init(view);
        return view;
    }
    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerVerticalFilter);
        vechileTYpe = (RadioGroup) view.findViewById(R.id.vechile_type_radio_type);
        carseatingCapacity = (RadioGroup) view.findViewById(R.id.car_select_seating) ;
        vanSeatingCapacity = (RadioGroup) view.findViewById(R.id.van_select_seating) ;
        busSeatingCapacity = (RadioGroup) view.findViewById(R.id.bus_select_seating);
        titleText = (TextView) view.findViewById(R.id.title_text);
        carseatingCapacity.setOnCheckedChangeListener(this);
        vechileTYpe.setOnCheckedChangeListener(this);
        vanSeatingCapacity.setOnCheckedChangeListener(this);
        busSeatingCapacity.setOnCheckedChangeListener(this);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);


        fromLayout = (RelativeLayout) view.findViewById(R.id.layout_from);
        enterFromLocation = (TextView) view.findViewById(R.id.enter_pick_up_location);
        pickUpLocationFrom = (TextView) view.findViewById(R.id.pick_location);


        mLocationModel = Singleton.getInstance().myLocationModel();
        Intent intent = getActivity().getIntent();
        if(SupportConstant.GET_DETAIL_FOR_FROM == intent.getIntExtra(SupportConstant.GET_DETAIL_FOR_FROM_STRING,0)) {
            viewPager.setCurrentItem(3);
            if(intent.getBooleanExtra(SupportConstant.FROM_GOOGLE_API, false))
            {
                if(intent.getStringExtra(SupportConstant.SUB_LOCALITY) == null)
                {
                    enterFromLocation.setText(intent.getStringExtra(SupportConstant.LOCALITY));
                    pickUpLocationFrom.setText(intent.getStringExtra(SupportConstant.GET_ADMIN_AREA)+", "+intent.getStringExtra(SupportConstant.COUNTRY));

                }else {

                    enterFromLocation.setText(intent.getStringExtra(SupportConstant.SUB_LOCALITY));
                    pickUpLocationFrom.setText(intent.getStringExtra(SupportConstant.LOCALITY)+", "+intent.getStringExtra(SupportConstant.GET_ADMIN_AREA)+", "+intent.getStringExtra(SupportConstant.COUNTRY));
                }

            }else {
                enterFromLocation.setText(intent.getStringExtra(SupportConstant.LOCALITY));
                pickUpLocationFrom.setText(intent.getStringExtra(SupportConstant.COMPLETE_DETAILS_FROM_GPS));
            }
        }

        fromLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), GetLocationFromGps.class));
            }
        });

        view.findViewById(R.id.getloaction_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mLocationModel.getLocality() == null && mLocationModel.getCity() == null))
                    ((NeedDriverActivity)getActivity()).carSupportActivity(getActivity());
                else
                    Singleton.getInstance().myTost(getActivity(),"Please Select Location");
                // Toast.makeText(getActivity(), mLocationModel.getCity()+" "+mLocationModel.getLocality(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rb=(RadioButton)getView().findViewById(checkedId);
        if (R.id.vechile_type_radio_type == group.getId()) {
            if(rb.getText().toString().equals(SupportConstant.CAR)) {
                viewPager.setCurrentItem(1);
                titleText.setText("Required seats");
            }
            else if (rb.getText().toString().equals(SupportConstant.VAN)) {
                viewPager.setCurrentItem(2);
                titleText.setText("Required seats");
            }else if (rb.getText().toString().equals(SupportConstant.BUS))
            {
                viewPager.setCurrentItem(4);
                titleText.setText("Required seats");
            }
        }else
        {
            RadioButton rb1 = (RadioButton) getView().findViewById(checkedId);

            String sSeatingCapacity = rb1.getText().toString();
            moveToLocation(sSeatingCapacity);
        }
    }

    private void moveToLocation(String sSeatingCapacity) {
        viewPager.setCurrentItem(3);
        titleText.setText("");

        int seatingCapacity;
        try {
            seatingCapacity = Integer.parseInt(sSeatingCapacity);
        }catch (Exception e)
        {
            seatingCapacity = 8;
        }
        mLocationModel.setSeatingCapacity(seatingCapacity);
        Toast.makeText(getActivity(), sSeatingCapacity, Toast.LENGTH_SHORT).show();
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

             resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.vechile_type;
                    break;
                case 1:
                    resId = R.id.carseatingCapatiy;
                    break;
                case 2:
                   resId = R.id.vanseatingCapatiy;
                    break;
                case 3:
                    resId = R.id.getLocation;
                    break;
                case 4:
                    resId = R.id.busseatingCapatiy;
                    break;
            }
            return getView().findViewById(resId);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
