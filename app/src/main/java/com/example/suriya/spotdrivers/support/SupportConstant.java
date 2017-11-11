package com.example.suriya.spotdrivers.support;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Suriya on 18-07-2017.
 */

public class SupportConstant {
    public static final String PREF_NAME = "preference";


    public static final int FLAG_PROFILE_PIC = 0x01;
    public static final int FLAG_LICENCE_FRONT = 0x02;
    public static final int FLAG_LICENCE_BACK = 0x03;
    public static final int FLAG_OTHER_PROOF_FRONT = 0x04;
    public static final int FLAG_OTHER_PROOF_BACK = 0x05;
    //for Radio Group
    public static final int FLAG_RADIO_GROUP = -1;
    //Shared preference
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EXPERIENCE_IN_YEAR = "expYear";
    public static final String PASSWORD = "password";
    public static final String PREFFERED_LOCATION = "prefferedLocation";
    public static final String GENDER = "gender";
    public static final String CHARGES = "charges";
    public static final String AGE = "age";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String EMERGENCY_CONTACT_1 = "emergency1";
    public static final String EMERGENCY_CONTACT_2 = "emergency2";
    public static final String EMAIL = "email";
    public static final String DRIVING_LICENCE_TYPE = "drivingLicenceType";
    public static final String PROFILE_PIC = "profile";
    public static final String LICENCE_FRONT = "licenceFront";
    public static final String LICENCE_BACK = "licenceBack";
    public static final String OTHER_FRONT = "otherFront";
    public static final String OTHER_BACK = "otherBack";
    public static final String PROFILE_SHARING = "profileSharing";
    public static final String LOGGED_IN = "login";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String LOCALITY_1 = "locality1";
    public static final String LOCALITY_2 = "locality2";
    public static final String IMAGE_PATH = "imagePath";
    //login
    public static final String LOGGEDIN_TYPE = "loggrdintype";
    public static final String LOGGED_IN_STATUS ="loggedinststatus";
    public static final String LOGGEDIN_USERID = "loggedinusedid";
    //message
    public static final String MESSAGELIST = "messageList";
    public static final String SEND_TO_USER_ID = "sendToUserID";
    public static final String CHAT_ROOM = "chat_room";
    public static final String CHAT_ROOM_ID = "chatroomid";
    //server Response
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    //user type
    public static final String USER_TYPE = "userType";
    public static final String DRIVER = "driver";
    public static final String USER = "user";
    //FCM
    public static final String FCM_SHARE = "fcmshare";
    public static final String FCM_ID = "fcmId";
    //OTP
    public static final String SMS_ORIGIN = "MIZION";
    public static final String OTP_DELIMITER = ":";
    public static final String WAITING_FOR_SMS = "waitingforSMS";
    public static final String NEWLY_REGISTERING_MOBILE_NUMBER = "newregisteringmobilenumber";
    public static final String NEW_USER = "newUser";
    public static final String FORGET_PASSWORD = "passwordChange";
    public static final String FORGET_PASSWORD_MOBILE_NUMBER = "forgetMobileNumber";
    public static final String OTP_TYPE = "otpType";
    //filter
    public static final String FILTER_APPLIED_STATUS = "filterAppliedStatus";
    public static final String FILTERED_CITY = "filteredCity";
    public static final String FILTERED_LOCALITY = "filteredLocality";
    //Notifi
    public static final String MESSAGE_RECIEVED = "messageRecieved";
    //Location
    public static final String LOCALITY = "locality";
    public static  final String COMPLETE_DETAILS_FROM_GPS = "completeDetailsFromGps";
    public  static  final int GET_DETAIL_FOR_FROM = 1;
    public static  final String GET_DETAIL_FOR_FROM_STRING = "fromDetails";
    public static final String SUB_LOCALITY = "subLocality";
    public static final String GET_ADMIN_AREA = "getAdminArea";
    public static final String COUNTRY = "country";
    public static final String FROM_GOOGLE_API = "fromGoogleApi";
    public static final String WHERE_I_AM_FROM = "whereIamFrom";
    public static final String I_AM_FROM_DRIVER_REGISTERATION = "iAmFromDriverRedisteration";

    public static final String DRIVER_PROFILE = "driverProfile";
    public static final String CAR_PROFILE = "carProfile";
    public static final String REG_TYPE = "regType";


    public static final String CAR = "CAR";
    public static final String VAN = "VAN";
    public static final String BUS = "BUS";
}
