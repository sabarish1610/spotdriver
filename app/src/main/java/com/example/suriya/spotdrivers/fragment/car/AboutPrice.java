package com.example.suriya.spotdrivers.fragment.car;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.support.Singleton;

/**
 * Created by Suriya on 06-10-2017.
 */

public class AboutPrice extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private CheckBox localTripDetails, longTripDetails, cHaltingCharges, cHillsAllowance;
    private LinearLayout localLayout, longLayout;
    private TextView tLimitedTime;
    private Model carModel;
    private int localCount=0, longCount = 0, isHaltingCharges = 0, isHillsAllowance = 0;
    private EditText eLimitedKmPerDay, ePerDayKmPrice, eExceedPerKmPrice, eLocalDriverBeta, eLocalWaitingCharges,
    eLongperKmPrice, eLongDriverBeta, eLongWaitingCharges, eHaltingCharges, eHillsAllowance;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_price_details, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        /**
         * Get Object from Singleton Class
         */
        carModel = Singleton.getInstance().myCarModel();
        /**
         * Check Box Details
         */
        localTripDetails = (CheckBox) view.findViewById(R.id.local_trip_quation);
        localTripDetails.setOnCheckedChangeListener(this);
        longTripDetails = (CheckBox) view.findViewById(R.id.long_trip_quation);
        longTripDetails.setOnCheckedChangeListener(this);
        cHaltingCharges = (CheckBox) view.findViewById(R.id.is_halring_charges);
        cHaltingCharges.setOnCheckedChangeListener(this);
        cHillsAllowance = (CheckBox) view.findViewById(R.id.is_long_allowance);
        cHillsAllowance.setOnCheckedChangeListener(this);
        /**
         * Linear Layouts
         */
        localLayout = (LinearLayout) view.findViewById(R.id.local_trip_details);
        longLayout = (LinearLayout) view.findViewById(R.id.long_trip_details);
        /**
         * EditText Details
         */
        eLimitedKmPerDay = (EditText) view.findViewById(R.id.local_limited_km);
        ePerDayKmPrice = (EditText) view.findViewById(R.id.local_total_cost);
        eExceedPerKmPrice = (EditText) view.findViewById(R.id.local_exceed_km_price);
        eLocalDriverBeta = (EditText) view.findViewById(R.id.local_driver_beta);
        eLocalWaitingCharges = (EditText) view.findViewById(R.id.local_waiting_charges);
        eLongperKmPrice = (EditText) view.findViewById(R.id.long_per_km_price);
        eLongDriverBeta = (EditText) view.findViewById(R.id.long_driver_beta);
        eLongWaitingCharges = (EditText) view.findViewById(R.id.long_waiting_charges);
        eHaltingCharges = (EditText) view.findViewById(R.id.long_halting_charges);
        eHillsAllowance = (EditText) view.findViewById(R.id.long_hills_allowance);
        /**
         * Text View
         */
        tLimitedTime = (TextView) view.findViewById(R.id.textMin);
        /**
         * Button
         */

        view.findViewById(R.id.next_of_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        /**
         * Hour Seekbar
         */
        seekBar(view);

    }

    /**
     *
     */
    private void next() {
        getDatas();

    }
    /**
     * Getting Data's from Fragments
     */
    public void getDatas()
    {
        /**
         * Local Trip Data's
         */
        carModel.setsKMLimitedPerDay(eLimitedKmPerDay.getText().toString().trim());
        carModel.setsPriceLimitedPerDayCost(ePerDayKmPrice.getText().toString().trim());
        carModel.setsExcceedperKMPrice(eExceedPerKmPrice.getText().toString().trim());
        carModel.setsLocalDRiverBeta(eLocalDriverBeta.getText().toString().trim());
        carModel.setsLocalLimitedTimePerDay(tLimitedTime.getText().toString().trim());
        carModel.setsLocalTripWaitingCharges(eLocalWaitingCharges.getText().toString().trim());
        /**
         * Long Trip Data's
         */
        carModel.setsLongTripPerKmPrice(eLongperKmPrice.getText().toString().trim());
        carModel.setsLongDRiverBeta(eLongDriverBeta.getText().toString().trim());
        carModel.setsLongTripWaitingCharges(eLongWaitingCharges.getText().toString().trim());
        if(localTripDetails.isChecked() || longTripDetails.isChecked())
        {
            if (localTripDetails.isChecked())
            {
                if(carModel.getsKMLimitedPerDay().isEmpty())
                {
                    toastMessage("Perday Km");
                    return;

                }
                if(carModel.getsPriceLimitedPerDayCost().isEmpty())
                  {
                   toastMessage("Cost");
                      return;
                  }
                  if (carModel.getsExcceedperKMPrice().isEmpty())
                  {
                      toastMessage("Exceed per km price");
                      return;
                  }
                  if (carModel.getsLocalDRiverBeta().isEmpty())
                  {
                      toastMessage("Enter Driver Beta");
                      return;
                  }
                  if (carModel.getsLocalTripWaitingCharges().isEmpty())
                  {
                      toastMessage("Enter Waiting Charges");
                      return;
                  }
                  if (carModel.getsLocalLimitedTimePerDay().equals("0")) {
                      toastMessage("Please Set Limited TIme");
                      return;
                  }
            }
            if(longTripDetails.isChecked()){
                if (carModel.getsLongTripPerKmPrice().isEmpty())
                {
                    toastLongTrip("Please Enter Charges for Per Km");
                    return;
                }
                if (carModel.getsLongDRiverBeta().isEmpty())
                {
                    toastLongTrip("Please Enter Driver Beta");
                    return;
                }
                if (carModel.getsLongTripWaitingCharges().isEmpty())
                {
                    toastLongTrip("Please Enter Waiting Charges");
                    return;
                }
                if (cHaltingCharges.isChecked())
                {
                    carModel.setsLongHaltingChargesPrice(eHaltingCharges.getText().toString().trim());

                    if (carModel.getsLongHaltingChargesPrice().isEmpty())
                    {
                        toastLongTrip("Please Enter Halting Charges");
                        return;
                    }
                }
                if (cHillsAllowance.isChecked())
                {
                    carModel.setsHillsAllowanceprice(eHillsAllowance.getText().toString().trim());
                    if (carModel.getsHillsAllowanceprice().isEmpty())
                    {
                        toastLongTrip("Please Enter Hills Allowance");
                        return;
                    }
                }
            }
        }else {
            Singleton.getInstance().myTost(getActivity(), "Please Select any Trip Type Please");
            return;
        }
        ((CreateDriverProfileAcitivity)getActivity()).moveToImagePage();
    }
    /**
     *
     * @param view
     */
    private void seekBar(View view) {
            // get seekbar from view
            final BubbleThumbSeekbar rangeSeekbar = (BubbleThumbSeekbar) view.findViewById(R.id.rangeSeekbar3);

            // get min and max text view
            final TextView tvMin = (TextView) view.findViewById(R.id.textMin);
            final TextView tvMax = (TextView) view.findViewById(R.id.textMax3);
            rangeSeekbar.setMinValue(0).setMaxValue(24).setSteps(1).apply();
            // set listener
            rangeSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
                @Override
                public void valueChanged(Number minValue) {
                    tvMin.setText(String.valueOf(minValue));
                }
            });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.local_trip_quation:
                if(isChecked)
                {
                    carModel.setCarTripType(localCount = localCount+1);
                    localLayout.setVisibility(View.VISIBLE);
                }else
                {
                    carModel.setCarTripType(localCount = localCount-1);
                    localLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.long_trip_quation:
                if(isChecked)
                {
                    carModel.setCarLongTrip(longCount = longCount+1);
                    longLayout.setVisibility(View.VISIBLE);
                }else
                {
                    carModel.setCarLongTrip(longCount = longCount-1);
                    longLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.is_halring_charges:
                carModel.setsLongHaltingCharges(isHaltingCharges = isHaltingCharges + 1);
                break;
            case R.id.is_long_allowance:
                carModel.setsHillsAllowance(isHillsAllowance = isHillsAllowance+1);
                break;
        }

    }
    private void toastMessage(String message)
    {
        Singleton.getInstance().myTost(getActivity(),"You have Selecte Local trip please enter "+ message);

    }
    private void toastLongTrip(String message)
    {
        Singleton.getInstance().myTost(getActivity(),"You have Selecte Long trip please enter "+ message);

    }
}
