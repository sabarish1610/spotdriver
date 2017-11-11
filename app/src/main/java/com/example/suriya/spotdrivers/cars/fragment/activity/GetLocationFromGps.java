package com.example.suriya.spotdrivers.cars.fragment.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.CreateDriverProfileAcitivity;
import com.example.suriya.spotdrivers.activity.needdriver.DriverCompleteProfileActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.cars.fragment.gpsadapter.PlaceArrayAdapter;
import com.example.suriya.spotdrivers.cars.fragment.model.LocationModel;
import com.example.suriya.spotdrivers.gpssupport.Jobbo;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Suriya on 23-09-2017.
 */

public class GetLocationFromGps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    // LogCat tag
    private int gpsStatusChange;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String TAG = GetLocationFromGps.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private Menu menu;
    private ImageView clearText;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Location mLastLocation;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String subLocality, locality, getAdminArea, countryName;
    private String apisubLocality, apilocality, apigetAdminArea, apicountryName;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private Intent intent;
    private LocationModel mLocationModel;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    // UI elements
    private Button btnShowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_get_car_location_in_gps);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        btnShowLocation = (Button) findViewById(R.id.get_current_location_in_gps);
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clearText = (ImageView) findViewById(R.id.clear_place);
        clearText.setOnClickListener(this);
        /**
         *
         */
        mLocationModel = Singleton.getInstance().myLocationModel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>0)
                    clearText.setVisibility(View.VISIBLE);
                else
                    clearText.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS).setCountry("IN").build();
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filter);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        //permissiom
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // First we need to check availability of play services

        // Show location button click listener
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkPlayServices()) {

                    // Building the GoogleApi client
                    Log.d("gpsStatusbefore", String.valueOf(gpsStatusChange));
                    gpsStatusChange = 1;
                    Log.d("gpsStatusafter", String.valueOf(gpsStatusChange));
                    buildGoogleApiClient();
                    // for getting address from GPS
                }
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                }
                try {
                    displayLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() throws IOException {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            addresses = geocoder.getFromLocation(latitude,longitude,1);
             subLocality = addresses.get(0).getSubLocality();
             locality = addresses.get(0).getLocality();
             getAdminArea = addresses.get(0).getAdminArea();
             countryName = addresses.get(0).getCountryName();
            mLocationModel.setLocality(subLocality);
            mLocationModel.setCity(locality);
            mLocationModel.setState(getAdminArea);
            if (!(subLocality == null) && !(locality == null)&& !(getAdminArea == null) && !(countryName == null)) {
                //Toast.makeText(getApplicationContext(), jobbo.getLocality()+"\n"+jobbo.getMoreDetailsAboutLocation(), Toast.LENGTH_SHORT).show();
                    Intent intentForFrom = new Intent(getApplicationContext(), NeedDriverActivity.class);
                    intentForFrom.putExtra(SupportConstant.LOCALITY, subLocality);
                    intentForFrom.putExtra(SupportConstant.GET_DETAIL_FOR_FROM_STRING, SupportConstant.GET_DETAIL_FOR_FROM);
                    intentForFrom.putExtra(SupportConstant.COMPLETE_DETAILS_FROM_GPS, locality + ", " + getAdminArea + ", " + countryName);
                    startActivity(intentForFrom);
            }else {
                Toast.makeText(getApplicationContext(), "Difacult to fetch the data", Toast.LENGTH_SHORT).show();
            }

        } else {
            /**
             *
             */


        }
    }
    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this).build();
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        Log.e(TAG, "Google Places API connection failed with error code: "
                + result.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        result.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        if (gpsStatusChange == 2)
        {
            mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
            Log.i(TAG, "Google Places API connected.");
        }else if (gpsStatusChange == 1)
        {
            try {
                displayLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            //displayLocation();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItem item1 = menu.findItem(R.id.app_bar_search);
        // Handle upload_car_image selection
        switch (item.getItemId()) {
            case android.R.id.home:
                mAutocompleteTextView.setVisibility(View.GONE);
                mAutocompleteTextView.setText("");
                item1.setVisible(true);

                //onBackPressed();
                return true;
            case R.id.app_bar_search:
                if (checkPlayServices())
                    // Building the GoogleApi client
                    Log.d("gpsStatusbeforeAuto", String.valueOf(gpsStatusChange));
                gpsStatusChange = 2;//for getting from auto complete box
                Log.d("gpsStatusafterAuto", String.valueOf(gpsStatusChange));
                    buildGoogleApiClient();
                mAutocompleteTextView.setVisibility(View.VISIBLE);

                item1.setVisible(false);
                //newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.clear_place:
                mAutocompleteTextView.setText("");
                gpsStatusChange = 0;
                break;
        }
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Toast.makeText(GetLocationFromGps.this, "hiiii", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                   .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
            mAutocompleteTextView.setText("");
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            //CharSequence attributions = places.getAttributions();
            Log.d("placeSubLocality", String.valueOf(place.getAddress()));
             filteringMethod(String.valueOf(place.getAddress()));

        }
    };

    private void filteringMethod(String sortingPlaces) {
        String[] s = sortingPlaces.split(",");
        // List<String> address = Arrays.asList(s);
        if (s.length > 2) {
            for (int i = s.length - 1; i >= 0; i--) {
                if (s.length - 1 == i) {
                    apicountryName = s[i].replaceAll("[0-9]", "");
                    // Log.d("GetLocation country", apiCountry);
                }
                if (s.length - 2 == i) {
                    apigetAdminArea = s[i].replaceAll("[0-9]", "");
                    mLocationModel.setState(apigetAdminArea);
                    //  Log.d("GetLocation state", apiAdminArea);
                }
                if (s.length - 3 == i) {
                    apilocality = s[i].replaceAll("[0-9]", "");
                    mLocationModel.setCity(apilocality);
                    //    Log.d("GetLocation city", apiLocality);
                }
                if (0 == i) {
                    apisubLocality = s[i].replaceAll("[0-9]", "");
                    mLocationModel.setLocality(apisubLocality);
                    //Log.d("GetLocation local", apiSublocality);
                }
                if(intent.getStringExtra(SupportConstant.WHERE_I_AM_FROM) == SupportConstant.I_AM_FROM_DRIVER_REGISTERATION)
                {
                    Intent fromGoogleApi = new Intent(getApplicationContext(), CreateDriverProfileAcitivity.class);
                    fromGoogleApi.putExtra(SupportConstant.GET_DETAIL_FOR_FROM_STRING, SupportConstant.GET_DETAIL_FOR_FROM);
                    fromGoogleApi.putExtra(SupportConstant.SUB_LOCALITY, apisubLocality);
                    fromGoogleApi.putExtra(SupportConstant.LOCALITY, apilocality);
                    fromGoogleApi.putExtra(SupportConstant.FROM_GOOGLE_API, true);
                    fromGoogleApi.putExtra(SupportConstant.GET_ADMIN_AREA, apigetAdminArea);
                    fromGoogleApi.putExtra(SupportConstant.COUNTRY, apicountryName);
                    startActivity(fromGoogleApi);
                }else{
                    Intent fromGoogleApi = new Intent(getApplicationContext(), NeedDriverActivity.class);
                    fromGoogleApi.putExtra(SupportConstant.GET_DETAIL_FOR_FROM_STRING, SupportConstant.GET_DETAIL_FOR_FROM);
                    fromGoogleApi.putExtra(SupportConstant.SUB_LOCALITY, apisubLocality);
                    fromGoogleApi.putExtra(SupportConstant.LOCALITY, apilocality);
                    fromGoogleApi.putExtra(SupportConstant.FROM_GOOGLE_API, true);
                    fromGoogleApi.putExtra(SupportConstant.GET_ADMIN_AREA, apigetAdminArea);
                    fromGoogleApi.putExtra(SupportConstant.COUNTRY, apicountryName);
                    startActivity(fromGoogleApi);

                }
            }

        }else{
            Toast.makeText(getApplicationContext(), "Please select correct location", Toast.LENGTH_SHORT).show();
        }
    }

}