package com.example.suriya.spotdrivers.fragment.needdriver;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.activity.needdriver.CreateNeedDriverProfile;
import com.example.suriya.spotdrivers.fragment.drivers.DriverDetailsOne;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.suriya.spotdrivers.support.Singleton.TAG;

/**
 * Created by Suriya on 18-07-2017.
 */

public class NeedDriverProfileCreate extends Fragment implements View.OnClickListener {
    private EditText userName, userLastName, userMobileNumber,userEmail, userPassword, userConfirmPassword ;
    private String sProfilePic, sUserName, sUserLastName, sUserMobileNumber, sUserEmail, sUserPassword, sUserConfirmPassword;
    private Button signUp;
    private RetInterface reInterface;
    private TextView userNameError, userMobileError, userEmailError, userPasswordError, userConfirmPasswordError;
    private CircleImageView profilePic;
    private ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enable the Option Menu
        setHasOptionsMenu(true);
    }
    private void progressBar()
    {
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Data's Uploading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.need_driver_profile_create,container,false);
        progressBar();
        Intent intent = getActivity().getIntent();
        sUserMobileNumber = intent.getStringExtra(SupportConstant.MOBILE_NUMBER);

       /* toolbar = (Toolbar) view.findViewById(R.id.driver_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);*/
        Log.d(SupportConstant.MOBILE_NUMBER, sUserMobileNumber);
        init(view);
        return view;
    }

    private void init(View view) {
        userName = (EditText)view.findViewById(R.id.user_name);
        userLastName = (EditText)view.findViewById(R.id.user_last_name);
       // userAge = (EditText)view.findViewById(R.id.need_driver_age);
        userMobileNumber = (EditText)view.findViewById(R.id.user_phone_number);
        userMobileNumber.setText(sUserMobileNumber);
        userMobileNumber.setEnabled(false);
        userEmail = (EditText)view.findViewById(R.id.user_email);
        userPassword = (EditText)view.findViewById(R.id.user_password);
        userConfirmPassword = (EditText)view.findViewById(R.id.user_confirm_password);
        signUp = (Button) view.findViewById(R.id.user_sign_up);
        signUp.setOnClickListener(this);
        // readio group
       // userGender = (RadioGroup)view.findViewById(R.id.user_gender);
        //TextView
        userNameError = (TextView)view.findViewById(R.id.user_name_error);
       // userAgeError = (TextView)view.findViewById(R.id.user_age_error);
        userMobileError = (TextView)view.findViewById(R.id.mobile_number_error);
        userEmailError = (TextView)view.findViewById(R.id.user_email_error);
        userPasswordError = (TextView)view.findViewById(R.id.user_password_error);
        userConfirmPasswordError = (TextView)view.findViewById(R.id.user_password_confirm_error);
        //text watcher
        userName.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userName));
        userMobileNumber.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userMobileNumber));
        //userAge.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userAge));
        userEmail.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userEmail));
        userPassword.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userPassword));
        userConfirmPassword.addTextChangedListener(new NeedDriverProfileCreate.MyTextWatcher(userConfirmPassword));
        //imageView
        profilePic = (CircleImageView)view.findViewById(R.id.user_profilr_pic);
        profilePic.setOnClickListener(this);
        getDatasfromEditText();
    }

    private void getDatasfromEditText() {
        sUserName = userName.getText().toString().trim();
        sUserLastName = userLastName.getText().toString().trim();
        sUserEmail = userEmail.getText().toString().trim();
        sUserPassword = userPassword.getText().toString().trim();
        sUserConfirmPassword = userConfirmPassword.getText().toString().trim();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem upload_car_image) {
        switch (upload_car_image.getItemId())
        {
            case R.id.next:
                nextFragment();
                return true;
        }
        return super.onOptionsItemSelected(upload_car_image);
    }*/

    private void signUp() {
        getDatasfromEditText();
        if(!firstNameValidation())
            return;
        if(!mobileNumberValidation())
            return;
        if(sUserEmail.length()>0)
            if (!valideMailId())
                return;
        if (sProfilePic == null)
        {
            Toast.makeText(getActivity(), "Please Keep profile Pic", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadToServer();
    }
    private void uploadToServer() {
        reInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = reInterface.insertUserDetails(sUserName, sUserLastName, sUserMobileNumber, sUserEmail, sUserConfirmPassword, sProfilePic);
        call.enqueue(new Callback<ServerResponse>() {
            ServerResponse resp;
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                resp = response.body();
                if (resp.getResult().contentEquals(SupportConstant.SUCCESS)) {
                    progress.dismiss();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.user_profilr_pic:
                ((CreateNeedDriverProfile)getActivity()).selectImage();
                break;
            case R.id.user_sign_up:
                signUp();
                break;

        }
    }

    /**
     *
     * @param bitmap
     * @param imageEncryption
     */
    public void getBitmapImage(Bitmap bitmap, String imageEncryption)
    {
        this.sProfilePic = imageEncryption;
        Log.d(TAG,imageEncryption);
        profilePic.setImageBitmap(bitmap);
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
                case R.id.user_name:
                    firstNameValidation();
                    break;
                case R.id.user_phone_number:
                    mobileNumberValidation();
                    break;
                case R.id.user_email:
                    valideMailId();
                    break;
                case R.id.user_password:
                    checkingPassword();
                    break;
                case R.id.user_confirm_password:
                    comparePassword();
                    break;
                // case R.id.need_driver_age:
                //   validateAge();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private boolean checkingPassword()
    {
        sUserPassword = userPassword.getText().toString().trim();
        if (!isValidPassword(sUserPassword))
        {
            userPasswordError.setText(getString(R.string.password_error));
            userPasswordError.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }else {
            userPasswordError.setText("");

        }
        return true;
    }
    private boolean comparePassword()
    {
        sUserConfirmPassword = userConfirmPassword.getText().toString().trim();
        if(!sUserPassword.equals(sUserConfirmPassword)){
            userConfirmPasswordError.setText(getString(R.string.confirm_password_error));
            userConfirmPasswordError.setTextColor(Color.rgb(255, 0, 0));
            return false;
        }
        else {
            userConfirmPasswordError.setText(getString(R.string.password_match));
            userConfirmPasswordError.setTextColor(Color.rgb(0,128,0));
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
    /**
     * Validate the name
     * @return
     */
    private boolean firstNameValidation()
    {
        getDatasfromEditText();
        if(sUserName.length() == 0)
        {
            userNameError.setText(getString(R.string.enter_name));
            return false;
        }else if (sUserName.length() < 3)
        {
            userNameError.setText(getString(R.string.enter_validate_name));
            return false;
        }else{
            userNameError.setText("");
        }
        return true;
    }

    /**
     * validate mobile Number
     * @return
     */
    private boolean mobileNumberValidation()
    {
        getDatasfromEditText();
        if(sUserMobileNumber.length() == 0)
        {
            userMobileError.setText(getString(R.string.enter_mobile));
            return false;
        }else if(sUserMobileNumber.length()<10 || sUserMobileNumber.length()>10) {
            userMobileError.setText(getString(R.string.enter_valide_mobile));
            return false;
        }
        else {
            userMobileError.setText("");
        }
        return true;
    }
    /**
     * mail validation
     */
    private boolean valideMailId()
    {
        getDatasfromEditText();

        if(!isValidEmail(sUserEmail))
        {if(sUserEmail.length()>0)
            userEmailError.setText(getString(R.string.enter_valide_mail));
            // errorEmail.setTextColor(Color.rgb(255, 0, 0));
            //requestFocus(emailId);
            return false;
        }
        else
        {
            userEmailError.setText("");
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

    /**
     * age validate
     * @return
     */
  /*  private boolean validateAge() {
        getDatasfromEditText();
        if(sUserAge.length() == 0)
        {
            userAgeError.setText(getString(R.string.enter_age));
            return false;
        }else if (sUserAge.length()>2||sUserAge.length()==1)
        {
            userAgeError.setText(getString(R.string.please_enter_correct_age));
            return false;
        }
        else {
            userAgeError.setText("");
        }

        return true;
    }*/

}
