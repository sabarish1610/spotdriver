package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 17-07-2017.
 */

public class CompleteProfile extends Fragment implements View.OnClickListener {
    private String sDriverProfile, sDriverFirstName, sDriverLastName, sDriverGender, sDriverMobileNumber, sDriverEMail, sDriverConfirmPassword, sDOB,
    sState, sCity, sCityLocality, sAadharNumber, sDriverLicenceNumber, sLicenceType, sDriverExp, sDriverCost, sPaymentType, sLicenceFrontpic,
    sLicenceBackPic, sIdFrontPic, sIdBackPic, sRegType;
    private EditText eDriverLicenceNumber, eDriverExp, eCost, eAadharNumber;
    private TextView errorLicenceNumber, tDriverLicenceFront, tDriverLicenceBack, tIdProofFront, tIdProofBack;
    private RetInterface retInterface;
    private RadioGroup driverDutyType;
    private RadioButton jobButton;
    private ProgressDialog progress;
    private Spinner licenceTypeSpinner, costTypeSpinner;
    private HintSpinner<String> hLicenceTypeSpinner, hCostTypeSpinner;
    private List licenceTypeList, costTypeList;
    private Button driverSignUp;
    private static final char space = ' ';
    private int withCar;

    //private ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complete_profile,container,false);
        init(view);
        Bundle bundle = getArguments();
        sDriverProfile = bundle.getString(SupportConstant.PROFILE_PIC, null);
        sDriverFirstName = bundle.getString(SupportConstant.FIRST_NAME, null);
        sDriverLastName = bundle.getString(SupportConstant.LAST_NAME, null);
        sDriverGender = bundle.getString(SupportConstant.GENDER, null);
        sDriverMobileNumber = bundle.getString(SupportConstant.MOBILE_NUMBER, null);
        sDriverEMail = bundle.getString(SupportConstant.EMAIL, null);
        sDriverConfirmPassword = bundle.getString(SupportConstant.PASSWORD, null);
        sDOB = bundle.getString(SupportConstant.AGE, null);
        sState = bundle.getString(SupportConstant.GET_ADMIN_AREA, null);
        sCity = bundle.getString(SupportConstant.LOCALITY, null);
        sCityLocality = bundle.getString(SupportConstant.SUB_LOCALITY);
        sRegType = bundle.getString(SupportConstant.REG_TYPE);
        withCar = 0;
        Log.d("typeOfReggss", sRegType);
        if (sRegType.equals(SupportConstant.CAR_PROFILE))
        {
            withCar = 1;
            eCost.setVisibility(View.GONE);
            costTypeSpinner.setVisibility(View.GONE);
            driverDutyType.setVisibility(View.GONE);
        }
      /*  Log.d("DriverProfile Pic", sDriverProfile);
        Log.d("Driver First Name", sDriverFirstName);
        Log.d("Driver First Name", sDriverLastName);
        Log.d("Driver First Name", sDriverGender);
        Log.d("Driver First Name", sDriverMobileNumber);
        Log.d("Driver First Name", sDriverEMail);
        Log.d("Driver First Name", sDriverConfirmPassword);
        Log.d("Driver First Name", sDOB);
        Log.d("stateee",sState+", "+sCity+", "+sCityLocality);*/

        //getFcmIdFromDevice();
        progressBar();
        return view;
    }

    private void init(View view) {
        eDriverLicenceNumber = (EditText) view.findViewById(R.id.licene_number);
        eDriverExp = (EditText) view.findViewById(R.id.driver_exp);
        eCost = (EditText) view.findViewById(R.id.driver_cost);
        eAadharNumber = (EditText) view.findViewById(R.id.aadhar_number);
        tDriverLicenceFront = (TextView) view.findViewById(R.id.driving_licence_front_pic);
        tDriverLicenceBack = (TextView) view.findViewById(R.id.driving_licence_back_pic);
        tIdProofFront = (TextView) view.findViewById(R.id.driver_id_front_pic);
        tIdProofBack = (TextView) view.findViewById(R.id.driver_id_back_pic);
        errorLicenceNumber = (TextView) view.findViewById(R.id.driver_licence_error);
        tDriverLicenceFront.setOnClickListener(this);
        tDriverLicenceBack.setOnClickListener(this);
        tIdProofFront.setOnClickListener(this);
        tIdProofBack.setOnClickListener(this);
        /*
        for Spinner
         */
        licenceTypeList = Arrays.asList(getResources().getStringArray(R.array.licence_list));
        costTypeList = Arrays.asList(getResources().getStringArray(R.array.salary_type));
        licenceTypeSpinner = (Spinner) view.findViewById(R.id.licence_type_spinner);
        costTypeSpinner = (Spinner) view.findViewById(R.id.cost_type_spinner);
        hLicenceTypeSpinner = new HintSpinner<>(licenceTypeSpinner, new HintAdapter<String>(getActivity(), "L-Type", licenceTypeList), new HintSpinner.Callback<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                sLicenceType = itemAtPosition.toString();
            }
        });
        hLicenceTypeSpinner.init();
        hCostTypeSpinner = new HintSpinner<>(costTypeSpinner, new HintAdapter<String>(getActivity(), "S-Mode", costTypeList), new HintSpinner.Callback<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                sPaymentType = itemAtPosition.toString();
            }
        });
        hCostTypeSpinner.init();
        /*
        Driver Duty Type
         */
        driverDutyType = (RadioGroup)view.findViewById(R.id.duty_type);
        /*
        signUpButton
         */
        driverSignUp = (Button) view.findViewById(R.id.driver_signUp);
        driverSignUp.setOnClickListener(this);
      /*  password = (EditText)view.findViewById(R.id.driver_password);
        confirmPassword = (EditText)view.findViewById(R.id.driver_confirm_password);
        licenceNumber = (EditText)view.findViewById(R.id.driver_licence_number);
        jobCata = (RadioGroup)view.findViewById(R.id.job_catagory);
        errorPassword = (TextView)view.findViewById(R.id.password_error);
        confirmErrorPassword = (TextView)view.findViewById(R.id.confirm_password_error);
        errorLicenceNumber = (TextView)view.findViewById(R.id.licence_error);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.rel);*/
        //progressBar = (ProgressBar)view.findViewById(R.id.driver_profile_complete_progress);
/*
error
 */
        eDriverLicenceNumber.addTextChangedListener(new MyTextWatcher(eDriverLicenceNumber));
        //eDriverExp.addTextChangedListener(new MyTextWatcher(eDriverExp));
        //eCost.addTextChangedListener(new MyTextWatcher(eCost));
        eAadharNumber.addTextChangedListener(new MyTextWatcher(eAadharNumber));
      /*  password.addTextChangedListener(new CompleteProfile.MyTextWatcher(password));
        confirmPassword.addTextChangedListener(new CompleteProfile.MyTextWatcher(confirmPassword));
        licenceNumber.addTextChangedListener(new CompleteProfile.MyTextWatcher(licenceNumber));*/
    }

    private void progressBar()
    {
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Data's Uploading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    /*private void getFcmIdFromDevice() {
        SharedPreferences shar = getActivity().getSharedPreferences(SupportConstant.FCM_SHARE,Context.MODE_PRIVATE);
        fcmId = shar.getString(SupportConstant.FCM_ID,null);
    }*/

    private void getAllDetails()
    {
        sDriverLicenceNumber = eDriverLicenceNumber.getText().toString().trim();
        sDriverExp = eDriverExp.getText().toString().trim();
        sDriverCost = eCost.getText().toString().trim();
        sAadharNumber = eAadharNumber.getText().toString().trim();
                //sendDatasToServer(jobCatagory);
    }
    private void signUp() {
        getAllDetails();
       if(sAadharNumber.length() == 0)
       {
           Toast.makeText(getActivity(), "Please Enter Aadhar Number", Toast.LENGTH_SHORT).show();
           return;
       }
        if (!(validateLienceNumber())) {
            return;
        }
        if (sDriverExp.length() == 0)
        {
            Toast.makeText(getActivity(), "Please Mention Experience", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sDriverCost.length() == 0 && !(sRegType.equals(SupportConstant.CAR_PROFILE)))
        {
            Toast.makeText(getActivity(), "Please Enter intrested price", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sLicenceType == null)
        {
            Toast.makeText(getActivity(), "Please Enter Licence Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sPaymentType == null && !(sRegType.equals(SupportConstant.CAR_PROFILE)))
        {
            Toast.makeText(getActivity(), "Please Enter PaymentMode", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sLicenceFrontpic == null)
        {
            Toast.makeText(getActivity(), "Please upload front licence photo", Toast.LENGTH_SHORT).show();
            return;
        }else if (sLicenceBackPic == null)
        {
            Toast.makeText(getActivity(), "Please upload back licence photo", Toast.LENGTH_SHORT).show();
            return;
        }else if (sIdFrontPic == null)
        {
            Toast.makeText(getActivity(), "Please upload front Id proof photo", Toast.LENGTH_SHORT).show();
            return;
        }else if(sIdBackPic == null)
        {
            Toast.makeText(getActivity(), "Please upload back Id proof photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (driverDutyType.getCheckedRadioButtonId() == SupportConstant.FLAG_RADIO_GROUP && !(sRegType.equals(SupportConstant.CAR_PROFILE)))
        {
            progress.dismiss();
            Toast.makeText(getActivity(), "Please select Job Catagory", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedItemId = driverDutyType.getCheckedRadioButtonId();
        jobButton = (RadioButton)getView().findViewById(selectedItemId);
        String jobCatagory;
        try {
             jobCatagory = jobButton.getText().toString();
        }catch (Exception e)
        {
            jobCatagory = "";
        }
        if (sRegType.equals(SupportConstant.CAR_PROFILE))
        {
            sDriverCost = "";
            sPaymentType = "";
        }
        //Toast.makeText(getActivity(), jobCatagory, Toast.LENGTH_SHORT).show();
        sendDatasToServer(jobCatagory);
    }
    private void sendDatasToServer( String sJobCatagory) {
        progress.show();
   retInterface = Singleton.getInstance().myRetInterface();
        Call<ServerResponse> call = retInterface.insertDriverDetails(sDriverFirstName, sDriverLastName, sDriverMobileNumber, sDriverGender,
                sLicenceType, sDriverProfile, sDriverExp, sDriverCost, sPaymentType, sDOB, sDriverEMail, sJobCatagory, sDriverConfirmPassword,
                sDriverLicenceNumber, sLicenceFrontpic, sLicenceBackPic, sIdFrontPic, sIdBackPic, withCar, sState, sCity, sCityLocality);
        call.enqueue(new Callback<ServerResponse>() {
    @Override
    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
        ServerResponse ser = response.body();
        if (ser.getResult().contentEquals(SupportConstant.SUCCESS)) {
            progress.dismiss();
            if(sRegType.equals(SupportConstant.CAR_PROFILE))
            {
                ((CreateDriverProfileAcitivity)getActivity()).moveToSelectVechileType();
            }else {
                //getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
        progress.dismiss();
        Toast.makeText(getActivity(), ser.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<ServerResponse> call, Throwable t) {
        try {
            Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e)
        {
            Singleton.getInstance().myTost(getActivity(), e.toString());
        }
        progress.dismiss();
    }
});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.driving_licence_front_pic:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_LICENCE_FRONT);
                break;
            case R.id.driving_licence_back_pic:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_LICENCE_BACK);
                break;
            case R.id.driver_id_front_pic:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_OTHER_PROOF_FRONT);
                break;
            case R.id.driver_id_back_pic:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_OTHER_PROOF_BACK);
                break;
            case R.id.driver_signUp:
                signUp();
                break;
        }

    }
    public void lincenceFront(Bitmap bitmap, String encryptedImage)
    {
        this.sLicenceFrontpic = encryptedImage;
        if(this.sLicenceFrontpic != null)
        {
            tDriverLicenceFront.setText(getResources().getString(R.string.image_selected));
            tDriverLicenceFront.setTextColor(getResources().getColor(R.color.lime_color));
            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }
        //licenceFront.setImageBitmap(bitmap);
        Log.d("linc_front",encryptedImage);
    }
    public void lincenceBack(Bitmap bitmap, String encryptedImage)
    {
        this.sLicenceBackPic = encryptedImage;
        if(sLicenceBackPic != null)
        {
            tDriverLicenceBack.setText(getResources().getString(R.string.image_selected));
            tDriverLicenceBack.setTextColor(getResources().getColor(R.color.lime_color));
        }
        //licenceBack.setImageBitmap(bitmap);
        Log.d("linc_back",encryptedImage);
    }
    public void setOtherProofFront(Bitmap bitmap, String encryptedImage)
    {
        this.sIdFrontPic = encryptedImage;
        if(sIdFrontPic != null)
        {

            tIdProofFront.setText(getResources().getString(R.string.image_selected));
            tIdProofFront.setTextColor(getResources().getColor(R.color.lime_color));
        }
        //otherProofFront.setImageBitmap(bitmap);
        Log.d("linc_oth_front",encryptedImage);
    }
    public void setOtherProofBack(Bitmap bitmap, String encryptedImage)
    {
        this.sIdBackPic = encryptedImage;
        if(sIdBackPic != null)
        {
            tIdProofBack.setText(getResources().getString(R.string.image_selected));
            tIdProofBack.setTextColor(getResources().getColor(R.color.lime_color));
        }
        //otherProofBack.setImageBitmap(bitmap);
        Log.d("linc_oth_back",encryptedImage);
    }


    private class MyTextWatcher implements TextWatcher {
        View view;
        private MyTextWatcher (View view)
        {
            this.view=view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (view.getId()) {
                case R.id.licene_number:
                    validateLienceNumber();
                    break;
                case R.id.aadhar_number:
                    aadharAddSpace(s);
                    //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }

        }
    }

    private void aadharAddSpace(CharSequence s) {

       // StringBuilder stringBuilder = new StringBuilder(s);

    }

    private boolean validateLienceNumber() {
        sDriverLicenceNumber = eDriverLicenceNumber.getText().toString();
        if (sDriverLicenceNumber.length()==0)
        {
            errorLicenceNumber.setText("please enter Licence Number");
            return false;
        }
        else
        {
            errorLicenceNumber.setText("");
        }
        return true;
    }

}
