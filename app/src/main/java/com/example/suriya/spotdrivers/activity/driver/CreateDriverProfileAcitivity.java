package com.example.suriya.spotdrivers.activity.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.suriya.spotdrivers.NetworkConnection.ConnectivityReciever;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.LoginActivity;
import com.example.suriya.spotdrivers.fragment.car.AboutCar;
import com.example.suriya.spotdrivers.fragment.car.AboutPrice;
import com.example.suriya.spotdrivers.fragment.car.CarImages;
import com.example.suriya.spotdrivers.fragment.car.Model;
import com.example.suriya.spotdrivers.fragment.drivers.CompleteProfile;
import com.example.suriya.spotdrivers.fragment.drivers.DriverDetailsOne;
import com.example.suriya.spotdrivers.fragment.drivers.SelectRegisterType;
import com.example.suriya.spotdrivers.fragment.drivers.SelectVechileType;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.example.suriya.spotdrivers.support.Utility;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDriverProfileAcitivity extends AppCompatActivity implements ConnectivityReciever.ConnectivityReceiverListener {
    private int flag;
    private Fragment fragment,driverComplete, aboutCarFragment, carDetailsPrice, carImagesFragment, regSelectFragment,
    selectVechileType;
    private Bitmap bitmap;
    private String typeOfRegisteration ;
    int count;
    private String condition, imagePath;
    ArrayList<Uri> path = new ArrayList<>();
    RetInterface retInterface;
    private Model carModel;
    int kmLimited, priceLimitedPerDay, localLimitedTime, exceedKm, localDriverBeta, localTripWaitingCharges,
            longTripPerKmPrice, longDriverBeta, longTripWaitingCharges, hillsAllowance, haltingCharges;
    private FragmentTransaction ft, ftCarPrice, ftImage, ftDriverRegisteration, ftAboutCar, ftSelectVechileType;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE =1;
    private String userChoosenTask, driverMobileNumber;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver_profile_acitivity);
         ft = getFragmentManager().beginTransaction();
        carModel = Singleton.getInstance().myCarModel();
        driverComplete = new CompleteProfile();
        fragment = new DriverDetailsOne();
        aboutCarFragment = new AboutCar();
        carDetailsPrice = new AboutPrice();
        carImagesFragment = new CarImages();
        regSelectFragment = new SelectRegisterType();
        ft.replace(R.id.fragment_profile, regSelectFragment);//regSelectFragment
        ft.commit();
    }
    public void moveToCarDetails()
    {
        //this.driverMobileNumber = driverMobileNumber;
        ftAboutCar = getFragmentManager().beginTransaction();
        ftAboutCar.replace(R.id.fragment_profile, aboutCarFragment).addToBackStack("");
        ftAboutCar.commit();
    }
    public void moveToSelectVechileType()
    {   selectVechileType = new SelectVechileType();
        ftSelectVechileType = getFragmentManager().beginTransaction();
        ftSelectVechileType.replace(R.id.fragment_profile, selectVechileType).commit();
    }
    public void moveDriverRegisteration(String typeOfRegisteration)
    {
        this.typeOfRegisteration = typeOfRegisteration;
        ftDriverRegisteration = getFragmentManager().beginTransaction();
        ftDriverRegisteration.replace(R.id.fragment_profile, fragment);
        ftDriverRegisteration.commit();
    }
    public void moveToAboutPrice()
    {
        ftCarPrice = getFragmentManager().beginTransaction();
        ftCarPrice.replace(R.id.fragment_profile, carDetailsPrice);
        ftCarPrice.commit();
    }
    public void moveToImagePage()
    {
        ftImage = getFragmentManager().beginTransaction();
        ftImage.replace(R.id.fragment_profile, carImagesFragment);
        ftImage.commit();
    }
   public void driverCompleteFragment(String sDriverProfilePic, String sDriverFirstName, String sDriverLastName, String sDriverMobileNumber, String sDriverEmail,
                                      String sDriverPassword, String sDriverDOB, String sGender, String sState, String sCity,
                                      String sCityLocality)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(SupportConstant.PROFILE_PIC, sDriverProfilePic);
        bundle.putString(SupportConstant.FIRST_NAME, sDriverFirstName);
        bundle.putString(SupportConstant.LAST_NAME, sDriverLastName);
        bundle.putString(SupportConstant.GENDER, sGender);
        bundle.putString(SupportConstant.MOBILE_NUMBER, sDriverMobileNumber);
        bundle.putString(SupportConstant.EMAIL, sDriverEmail);
        bundle.putString(SupportConstant.PASSWORD, sDriverPassword);
        bundle.putString(SupportConstant.AGE, sDriverDOB);
        bundle.putString(SupportConstant.GET_ADMIN_AREA, sState);
        bundle.putString(SupportConstant.LOCALITY, sCity);
        bundle.putString(SupportConstant.SUB_LOCALITY, sCityLocality);
        bundle.putString(SupportConstant.REG_TYPE, typeOfRegisteration);
        Log.d("typeOfReg", typeOfRegisteration);
        driverComplete.setArguments(bundle);
        ft.replace(R.id.fragment_profile,driverComplete).commit();

    }
    public void selectImage(int flag) {
        this.flag = flag;
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateDriverProfileAcitivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(CreateDriverProfileAcitivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        if (condition == "carImage")
        {
            onSelectFromGalleryResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flag == SupportConstant.FLAG_PROFILE_PIC)
            ((DriverDetailsOne)fragment).getBitmapImage(bitmap, imagetoString(bitmap));
        else if (flag == SupportConstant.FLAG_LICENCE_FRONT)
            ((CompleteProfile)driverComplete).lincenceFront(bitmap, imagetoString(bitmap));
        else if (flag == SupportConstant.FLAG_LICENCE_BACK)
            ((CompleteProfile)driverComplete).lincenceBack(bitmap, imagetoString(bitmap));
        else if (flag == SupportConstant.FLAG_OTHER_PROOF_FRONT)
            ((CompleteProfile)driverComplete).setOtherProofFront(bitmap, imagetoString(bitmap));
        else if (flag == SupportConstant.FLAG_OTHER_PROOF_BACK)
            ((CompleteProfile)driverComplete).setOtherProofBack(bitmap, imagetoString(bitmap));

    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap resizeImage = null;
        if (data != null) {
            if (condition == "carImage")
            {
                Log.d("carImage", "welcome");
                path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                ((CarImages)carImagesFragment).imagePath(path);
                return;
                //imageAdapter.changePath(path);
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                resizeImage = getResizedBitmap(bitmap,500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (flag == SupportConstant.FLAG_PROFILE_PIC)
        ((DriverDetailsOne)fragment).getBitmapImage(resizeImage, imagetoString(resizeImage));
        else if (flag == SupportConstant.FLAG_LICENCE_FRONT)
       ((CompleteProfile)driverComplete).lincenceFront(resizeImage, imagetoString(resizeImage));
        else if (flag == SupportConstant.FLAG_LICENCE_BACK)
        ((CompleteProfile)driverComplete).lincenceBack(resizeImage, imagetoString(resizeImage));
        else if (flag == SupportConstant.FLAG_OTHER_PROOF_FRONT)
            ((CompleteProfile)driverComplete).setOtherProofFront(resizeImage, imagetoString(resizeImage));
        else if (flag == SupportConstant.FLAG_OTHER_PROOF_BACK)
            ((CompleteProfile)driverComplete).setOtherProofBack(resizeImage, imagetoString(resizeImage));
    }
    private String imagetoString(Bitmap bitmap)
    {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);


    }
    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void sendCarDetailsToServer(Context context)
    {
        progress=new ProgressDialog(context);
        progress.setMessage("Car Details Uploading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        try {
             kmLimited = Integer.parseInt(carModel.getsKMLimitedPerDay());
        } catch (Exception e)
        {
            kmLimited = 0;
        }
        try{
             priceLimitedPerDay = Integer.parseInt(carModel.getsPriceLimitedPerDayCost());

        }catch (Exception e)
        {
            priceLimitedPerDay = 0;
        }
        try{
             localLimitedTime = Integer.parseInt(carModel.getsLocalLimitedTimePerDay());
        }catch (Exception e)
        {
            localLimitedTime = 0;
        }
        try{
             exceedKm = Integer.parseInt(carModel.getsExcceedperKMPrice());
        }catch (Exception e)
        {
            exceedKm = 0;
        }
        try{
             localDriverBeta = Integer.parseInt(carModel.getsLocalDRiverBeta());
        }catch (Exception e)
        {
            localDriverBeta = 0;
        }
        try{
             localTripWaitingCharges = Integer.parseInt(carModel.getsLocalTripWaitingCharges());
        }catch (Exception e)
        {
            localTripWaitingCharges = 0;
        }
        try{
             longTripPerKmPrice = Integer.parseInt(carModel.getsLongTripPerKmPrice());
        }catch (Exception e)
        {
            longTripPerKmPrice = 0;
        }
        try{
             longDriverBeta = Integer.parseInt(carModel.getsLongDRiverBeta());
        }catch (Exception e)
        {
            longDriverBeta = 0;
        }
        try{
             longTripWaitingCharges =  Integer.parseInt(carModel.getsLongTripWaitingCharges());
        }catch (Exception e)
        {
            longTripWaitingCharges = 0;
        }
        try {
             hillsAllowance = Integer.parseInt(carModel.getsHillsAllowanceprice());
        }catch (Exception e)
        {
            hillsAllowance = 0;
        }
        try{
             haltingCharges = Integer.parseInt(carModel.getsLongHaltingChargesPrice());
        }catch (Exception e)
        {
            haltingCharges = 0;
        }

                retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<ServerResponse> call = retInterface.carDetailsUpload(carModel.getsMobileNumber(), carModel.getsCatagoryofCar(),
                carModel.getsModelofCar(),
                carModel.getsCarRegisterNumberofCar(),
                carModel.getsTravelWithType(),
                carModel.getsFuelTypeofCar(),
                carModel.getsCarManufacturingofCar(),
                carModel.getsSeatingCapacityofCar(),
                carModel.getCarTripType(),
                kmLimited,
                priceLimitedPerDay,
                        localLimitedTime,
                        exceedKm,
                        localDriverBeta,
                        localTripWaitingCharges,
                         carModel.getCarLongTrip(),
                        longTripPerKmPrice,
                        longDriverBeta,
                        longTripWaitingCharges,
                        carModel.getsHillsAllowance(),
                        hillsAllowance,
                        carModel.getsLongHaltingCharges(),
                        haltingCharges);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().equals(SupportConstant.SUCCESS))
                {
                    freqUpload(0);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Singleton.getInstance().myTost(getApplicationContext(),"Failure");

            }
        });
    }
    public void getImageFromFisherMan(String condition)
    {
        this.condition = condition;
        Log.d("CreateDriver", "welcome");
        FishBun.with(CreateDriverProfileAcitivity.this)
                .MultiPageMode()
                .setMaxCount(5)
                .setMinCount(5)
                .setPickerSpanCount(5)
                .setArrayPaths(path)
                .setAlbumSpanCount(1, 3)
                .setActionBarColor(Color.parseColor("#ffd423"), Color.parseColor("#FABE00"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_custom_back_white))
                .setOkButtonDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_custom_ok))
                .setAllViewTitle("All")
                .setActionBarTitle("FishBun Dark")
                .textOnNothingSelected("Please select three or more!")
                .startAlbum();


    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }
    public void freqUpload(int imageId)
    {
        if (path.size() == imageId) {
            progress.dismiss();
           // return;
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return;
        }
            String imagePath = getPath(path.get(imageId));
            upload(imagePath, imageId);
    }
    public  void upload(String singleImagePath, int imageId)
    {
        count = imageId;
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
            File file = new File(singleImagePath);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            Call<ServerResponse> call = retInterface.imageUpload(filePart, carModel.getsMobileNumber(), imageId, carModel.getsCarRegisterNumberofCar());
            Log.d("server i", String.valueOf(count));
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse res = response.body();
                    if(res.getResult().equals("success"))
                    {
                        if (count < path.size())
                        {
                            freqUpload(count+1);
                            progress.setMessage("Uploading "+count+" Image...");
                        }

                        Log.d("server Count", String.valueOf(count));
                        Log.d("serverurl", res.getPath());
                        Log.d("servername", res.getName());
                        Log.d("serverid", res.getId());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                }
            });
            Log.d("listType", imagePath);


    }
    /*public void dialogBox(final Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Uncomment the below code to Set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialogExample");
        alert.show();
    }*/

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}
