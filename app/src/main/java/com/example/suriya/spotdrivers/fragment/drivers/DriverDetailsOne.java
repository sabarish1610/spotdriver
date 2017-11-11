package com.example.suriya.spotdrivers.fragment.drivers;

import android.app.AlertDialog;
import android.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.cars.fragment.activity.GetLocationFromGps;
import com.example.suriya.spotdrivers.fragment.car.Model;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.example.suriya.spotdrivers.support.calender.SetData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Suriya on 17-07-2017.
 */

public class DriverDetailsOne extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String TAG = DriverDetailsOne.class.getSimpleName();
    private CircleImageView selectProfilePic;
    private RadioGroup gender;
    private RadioButton genderButton;
    private Button next;
    private EditText eDriverFirstName, eDriverLastName, eDriverMobileNumber,eDriverEmail, eDriverPassword, eDriverConfirmPassword,
    eDriverDOB;
    private String sDriverProfilePic, sDriverFirstName, sDriverLastName, sDriverMobileNumber, sDriverEmail, sDriverPassword, sConfirmDriverPassword,
    sDriverDOB, sState, sCity, sCityLocality, sCountry, sGender;
    private Model carModel;
    private TextView nameError,mobileError,emailError, passwordError, confirmPasswordError, dobError, prefferedLocationError;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enable tje Option Menu
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_profile_personal_details,container,false);
        Intent intent = getActivity().getIntent();
        carModel = Singleton.getInstance().myCarModel();
        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                filteringMethod(String.valueOf(place.getAddress()));
                Toast.makeText(getActivity(),place.getAddress(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getActivity(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        sDriverMobileNumber = intent.getStringExtra(SupportConstant.MOBILE_NUMBER);
        carModel.setsMobileNumber(sDriverMobileNumber);
        Fcm();
        init(view);
        return view;
    }

    private void filteringMethod(String sortingPlaces) {
        String[] s = sortingPlaces.split(",");
        // List<String> address = Arrays.asList(s);
        if (s.length > 2) {
            for (int i = s.length - 1; i >= 0; i--) {
                if (s.length -1 == i)
                {
                    sCountry = s[i].replace("[0-9]", "");
                }
                if (s.length - 2 == i) {
                    sState = s[i].replaceAll("[0-9]", "");
                      Log.d("GetLocation state", sState);
                }
                if (s.length - 3 == i) {
                    sCity = s[i].replaceAll("[0-9]", "");
                        Log.d("GetLocation city", sCity);
                }
                if(!(s.length == 3))
                if (0 == i) {
                    sCityLocality = s[i].replaceAll("[0-9]", "");
                    Log.d("GetLocation local", sCityLocality);
                }
            }
        }
    }

    private void Fcm() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SupportConstant.FCM_SHARE, Context.MODE_PRIVATE);
    }


    private void init(View view) {
        selectProfilePic = (CircleImageView) view.findViewById(R.id.driver_profile_pic);
        selectProfilePic.setOnClickListener(this);
        next = (Button) view.findViewById(R.id.driver_next);
        next.setOnClickListener(this);
        /*
        editText
         */
        eDriverFirstName = (EditText)view.findViewById(R.id.driver_first_name);
        eDriverLastName = (EditText)view.findViewById(R.id.driver_last_name);
        /*eExpInYr = (EditText)view.findViewById(R.id.experience_year);
        eCharges = (EditText)view.findViewById(R.id.driver_charges);
        eAge = (EditText)view.findViewById(R.id.driver_age);*/
        eDriverMobileNumber = (EditText)view.findViewById(R.id.driver_phone_number);
        eDriverMobileNumber.setText(sDriverMobileNumber);
        eDriverMobileNumber.setEnabled(false);
        eDriverEmail = (EditText)view.findViewById(R.id.driver_mail);
        eDriverPassword = (EditText) view.findViewById(R.id.driver_password);
        eDriverConfirmPassword = (EditText) view.findViewById(R.id.driver_confirm_password);
        //ePrefferedLocation = (EditText) view.findViewById(R.id.driver_preffered_location);
        //ePrefferedLocation.setOnClickListener(this);
        eDriverDOB = (EditText) view.findViewById(R.id.driver_date_of_birth);
        eDriverDOB.setOnClickListener(this);
        /*
        TextView
         */
        nameError = (TextView)view.findViewById(R.id.driver_name_error);
        mobileError = (TextView)view.findViewById(R.id.driver_mobile_number_error);
        emailError = (TextView)view.findViewById(R.id.driver_email_error);
        passwordError = (TextView)view.findViewById(R.id.driver_password_error);
        confirmPasswordError = (TextView) view.findViewById(R.id.driver_confirm_password_error);
        dobError = (TextView) view.findViewById(R.id.driver_dob_error) ;
        prefferedLocationError = (TextView) view.findViewById(R.id.driver_preffered_location_error);
        /*
        set Error Message
         */
        eDriverFirstName.addTextChangedListener(new MyTextWatcher(eDriverFirstName));
        eDriverMobileNumber.addTextChangedListener(new MyTextWatcher(eDriverMobileNumber));
        eDriverEmail.addTextChangedListener(new MyTextWatcher(eDriverEmail));
        eDriverPassword.addTextChangedListener(new MyTextWatcher(eDriverPassword));
        eDriverConfirmPassword.addTextChangedListener(new MyTextWatcher(eDriverConfirmPassword));
        dobError.addTextChangedListener(new MyTextWatcher(dobError));
//        eDriverPrefferedLocation.addTextChangedListener(new MyTextWatcher(eDriverPrefferedLocation));
        /*
        spinner
         */
       // salaryType = (Spinner)view.findViewById(R.id.price_spinner);
        /*
        radio button
         */
        gender = (RadioGroup)view.findViewById(R.id.gender);

    }
    private void passingValues()
    {
        sDriverFirstName= eDriverFirstName.getText().toString().trim();
        sDriverLastName = eDriverLastName.getText().toString().trim();
        sDriverEmail = eDriverEmail.getText().toString().trim();
        sDriverPassword = eDriverPassword.getText().toString().trim();
        sConfirmDriverPassword = eDriverConfirmPassword.getText().toString().trim();
        sDriverDOB = eDriverDOB.getText().toString().trim();
//        sDriverPrefferedLocation = eDriverPrefferedLocation.getText().toString().trim();
    }
    public void getBitmapImage(Bitmap bitmap, String imageEncryption)
    {
        this.sDriverProfilePic = imageEncryption;
        selectProfilePic.setImageBitmap(bitmap);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.driver_profile_pic:
                ((CreateDriverProfileAcitivity)getActivity()).selectImage(SupportConstant.FLAG_PROFILE_PIC);
                break;
            case R.id.driver_next:
                 next();
                //((CreateDriverProfileAcitivity)getActivity()).driverCompleteFragment();
                break;
            case R.id.driver_date_of_birth:
                SetData setData = new SetData(eDriverDOB, getActivity());
                break;
        }
    }

    /**
     * Validate the name
     * @return
     */
    private boolean firstNameValidation()
    {
        passingValues();
        if(sDriverFirstName.length() == 0)
        {
            nameError.setText(getString(R.string.enter_name));
            return false;
        }else if (sDriverFirstName.length() < 3)
        {
            nameError.setText(getString(R.string.enter_validate_name));
            return false;
        }else{
            nameError.setText("");
        }
        return true;
    }

    /**
     * validate mobile Number
     * @return
     */
    private boolean mobileNumberValidation()
    {
        passingValues();
        if(sDriverMobileNumber.length() == 0)
        {
            mobileError.setText(getString(R.string.enter_mobile));
            return false;
        }else if(sDriverMobileNumber.length()<10 || sDriverMobileNumber.length()>10) {
            mobileError.setText(getString(R.string.enter_valide_mobile));
            return false;
        }
        else {
            mobileError.setText("");
        }
        return true;
    }
    /**
     * mail validation
     */
    private boolean valideMailId()
    {
        passingValues();

        if(!isValidEmail(sDriverEmail))
        {if(sDriverEmail.length()>0)
            emailError.setText(getString(R.string.enter_valide_mail));
            // errorEmail.setTextColor(Color.rgb(255, 0, 0));
            //requestFocus(emailId);
            return false;
        }
        else
        {
            emailError.setText("");
        }
        return true;
    }

    /**
     * validate Email
     * @param email
     * @return
     */
    private boolean isValidEmail(String email)
    {
        return !TextUtils.isEmpty(email)&&android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //  sSalaryType = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * textwater class
     */
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
        switch (view.getId())
        {
            case R.id.driver_first_name:
                firstNameValidation();
                break;
            case R.id.driver_phone_number:
                mobileNumberValidation();
                break;
            case R.id.driver_mail:
                valideMailId();
                break;
            case R.id.driver_password:
                checkingPassword();
                break;
            case R.id.driver_confirm_password:
                comparePassword();
                break;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

    /*private boolean validateExperience() {
        passingValues();
        if(sExpInYr.length()==0)
        {
            experiencrError.setText(getString(R.string.enter_amount));
            return false;
        }
        else {
            experiencrError.setText("");
        }
        return true;
    }*/

    /**
     * age validate
     * @return
     */
  /*  private boolean validateAge() {
        passingValues();
        if(sAge.length() == 0)
        {
            ageError.setText(getString(R.string.enter_age));
            return false;
        }else if (sAge.length()>2||sAge.length()==1)
        {
            ageError.setText(getString(R.string.please_enter_correct_age));
            return false;
        }
        else {
            ageError.setText("");
        }

        return true;
    }*/

    /**
     * validate charges
     * @return
     */
   /* private boolean validateDrivingCharges() {
        passingValues();
        if(sCharges.length()==0)
        {
            chardesError.setText(getString(R.string.enter_amount));
            return false;
        }
        else {
            chardesError.setText("");
        }
        return true;
    }*/
    /**
     * next fragment and Validate all details
     */
    private void next()
    {
        passingValues();
        if(!firstNameValidation())
            return;
        if(!mobileNumberValidation())
            return;
        if(sDriverEmail.length()>0)
            if (!valideMailId())
                return;
        if(!checkingPassword())
        {

            return;
        }
        if(!comparePassword())
        {
            return;
        }
        if (sDriverDOB.length() == 0)
        {
            Toast.makeText(getActivity(), "Please select DoB", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sCountry == null)
        {
            Toast.makeText(getActivity(), "Please select Preffered Location", Toast.LENGTH_SHORT).show();
            return;
        }
       /*if (sDriverPrefferedLocation.length() == 0)
        {
            Toast.makeText(getActivity(), "Please select Preffered Location", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(gender.getCheckedRadioButtonId() == SupportConstant.FLAG_RADIO_GROUP){
            Toast.makeText(getActivity(), "Please SelectGender", Toast.LENGTH_SHORT).show();return;}
        if (sDriverProfilePic == null)
        {
            Toast.makeText(getActivity(), "Please select profile pic", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedItemId = gender.getCheckedRadioButtonId();
        genderButton = (RadioButton) getView().findViewById(selectedItemId);
        sGender = genderButton.getText().toString();
        Toast.makeText(getActivity(), sGender, Toast.LENGTH_SHORT).show();
      /* Fragment fragment = new CompleteProfile();
        Bundle bundle = new Bundle();
        bundle.putString(SupportConstant.PROFILE_PIC, sDriverProfilePic);
        bundle.putString(SupportConstant.FIRST_NAME, sDriverFirstName);
        fragment.setArguments(bundle);*/
        ((CreateDriverProfileAcitivity)getActivity()).driverCompleteFragment(sDriverProfilePic, sDriverFirstName, sDriverLastName,
                sDriverMobileNumber, sDriverEmail, sDriverPassword, sDriverDOB, sGender, sState, sCity, sCityLocality);
    }
    //checking password
    private boolean checkingPassword()
    {
        sDriverPassword = eDriverPassword.getText().toString().trim();
        if (!isValidPassword(sDriverPassword))
        {
            passwordError.setText(getString(R.string.password_error));
            passwordError.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }else {
            passwordError.setText("");

        }
        return true;
    }
    private boolean comparePassword()
    {
        sConfirmDriverPassword = eDriverConfirmPassword.getText().toString().trim();
        if(!sConfirmDriverPassword.equals(sDriverPassword)){
            confirmPasswordError.setText(getString(R.string.confirm_password_error));
            confirmPasswordError.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        else {
            confirmPasswordError.setText(getString(R.string.password_match));
            confirmPasswordError.setTextColor(Color.rgb(0,128,0));
        }

        return true;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
