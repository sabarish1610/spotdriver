package com.example.suriya.spotdrivers.retrofit;

import com.example.suriya.spotdrivers.chat.chatroomfragment.MessageSupport.ModelChatRoom;
import com.example.suriya.spotdrivers.retrofit.support.DriverDetails;
import com.example.suriya.spotdrivers.retrofit.support.Message;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by sabarish on 20/6/17.
 */

public interface RetInterface {
    /**
     *
     * @param name
     * @param lastName
     * @param mobileNumber
     * @param sriverGender
     * @param drivingLicenceType
     * @param profilePic
     * @param experienceYear
     * @param charges
     * @param age
     * @param email
     * @param jobCatagory
     * @param driverPassword
     * @param licenceNumber
     * @param licenceFront
     * @param licenceBack
     * @param otherFront
     * @param otherBack
     * @return
     */
    @FormUrlEncoded
    @POST("save_driver_details.php")
    Call<ServerResponse> insertDriverDetails(@Field("name") String name,
                                             @Field("last_name") String lastName,
                                             @Field("mobile_number") String mobileNumber,
                                             @Field("driver_gender") String sriverGender,
                                             @Field("driving_licence_type") String drivingLicenceType,
                                             @Field("profile_pic") String profilePic,
                                             @Field("experience_year") String experienceYear,
                                             @Field("charges") String charges,
                                             @Field("payment_type") String paymentType,
                                             @Field("age") String age,
                                             @Field("email") String email,
                                             @Field("job_catagory") String jobCatagory,
                                             @Field("driver_password") String driverPassword,
                                             @Field("licence_number") String licenceNumber,
                                             @Field("licence_front") String licenceFront,
                                             @Field("licence_back") String licenceBack,
                                             @Field("other_front") String otherFront,
                                             @Field("other_back") String otherBack,
                                             @Field("with_car") int withCar,
                                            // @Field("driver_fcm_id") String fcmId,
                                             @Field("state") String state,
                                             @Field("city") String city,
                                            // @Field("locality1") String locality1,
                                             @Field("city_locality") String cityLocality);
    @FormUrlEncoded
    @POST("user_register_profile.php")
    Call<ServerResponse> insertUserDetails(@Field("user_name") String userName,
                                           @Field("user_last_name") String userLastName,
                                           @Field("user_mobile") String userMobile,
                                           @Field("user_email") String userEmail,
                                           @Field("user_password") String userPassword,
                                           @Field("user_profile_pic") String userProfilePic);

    /**
     *
     * @return
     */
    @GET("retrieve_driver_details.php")
   // Call<List<DriverDetails>> getDriverDetailsFromServer();
    Call<ServerResponse> getDriverDetailsFromServer();

    /**
     *
     * @param mobileNumber
     * @return
     */
    @GET("driver_complete_details.php")
    Call<ServerResponse> getCompleteDriverProfile(@Query("mobile_number") String mobileNumber);

    /**
     *
     * @param driverMobileNumber
     * @param driverPassword
     * @return
     */
    @FormUrlEncoded
    @POST("driver_login.php")
    Call<ServerResponse> loginForDriver(@Field("driver_mobile") String driverMobileNumber,
                                        @Field("driver_password") String driverPassword,
                                        @Field("fcm_code") String fcmCode);

    /**
     *
     * @param userPhoneNumber
     * @param userPassword
     * @return
     */
    @FormUrlEncoded
    @POST("user_login.php")
    Call<ServerResponse> loginForUser(@Field("user_mobile") String userPhoneNumber,
                                      @Field("user_password") String userPassword,
                                      @Field("fcm_code") String fcmCode);

    /**
     *
     * @param userId
     * @return
     */
    @GET("getChatRoomDetails.php")
    Call<List<ModelChatRoom>> getRoomDetails(@Query("user_id") String userId,
                                             @Query("type_login") String loginType);

    /**
     *
     * @param chatRoomId
     * @return
     */
    @GET("message_retrieve.php")
    Call<List<Message>> getMessage(@Query("chat_room_id") String chatRoomId);

    /**
     *
     * @param userId
     * @param sendToUserId
     * @return
     */
    @FormUrlEncoded
    @POST("create_chat_room.php")
    Call<ServerResponse> createChatRoom(@Field("user_id") String userId,
                                        @Field("send_to_user_id") String sendToUserId);

    /**
     *
     * @param sendToUserId
     * @param userId
     * @param textMessage
     * @param chatRoomId
     * @return
     */
    @FormUrlEncoded
    @POST("message.php")
    Call<ServerResponse> sendMessage(@Field("send_to_user_id") String sendToUserId,
                                     @Field("user_id") String userId,
                                     @Field("message") String textMessage,
                                     @Field("chat_room_id") String chatRoomId,
                                     @Field("user_type") String userType);

    /**
     *
     * @param mobileNumber
     * @return
     */
    @FormUrlEncoded
    @POST("get_user_profile.php")
    Call<ServerResponse> getUserProfileCompletly(@Field("mobile_number") String mobileNumber);
    @FormUrlEncoded
    @POST("change_user_password.php")
    Call<ServerResponse> changeUserPassword(@Field("mobile_number") String mobileNumber,
                                            @Field("old_password") String oldPassword,
                                            @Field("new_password") String newPassword);
    @FormUrlEncoded
    @POST("change_driver_password.php")
    Call<ServerResponse> changeDriverPassword(@Field("mobile_number") String mobileNumber,
                                              @Field("old_password") String oldPassword,
                                            @Field("new_password") String newPassword);

    /**
     *
     * @param mobileNumber
     * @param recievedOtp
     * @return
     */
    @FormUrlEncoded
    @POST("verify_otp.php")
    Call<ServerResponse> verifyOtp(@Field("mobile_number") String mobileNumber,
                                   @Field("recieved_otp") String recievedOtp);

    /**
     *
     * @param mobileNumber
     * @param userType
     * @return
     */
    @FormUrlEncoded
    @POST("send_otp.php")
    Call<ServerResponse> sendOtp(@Field("mobile_number") String mobileNumber,
                                 @Field("user_type") String userType);

    /**
     *
     * @param userMobileNumber
     * @param driverMobileNumber
     * @return
     */
    @FormUrlEncoded
    @POST("add_call_details.php")
    Call<ServerResponse> uploadCallDetails(@Field("user_mobile_number") String userMobileNumber,
                                           @Field("driver_mobile_number") String driverMobileNumber);

    /**
     *
     * @param city
     * @param locality
     * @return
     */
    @GET("filter_drivers.php")
    Call<ServerResponse> filterDrivers(@Query("city") String city,
                                       @Query("locality") String locality,
                                       @Query("filter_type") String filterType);

    /**
     *
     * @param mobileNumber
     * @param typeOfUser
     * @return
     */
    @FormUrlEncoded
    @POST("otp_for_forget_password.php")
    Call<ServerResponse> otpForForgetPassword(@Field("mobile_number") String mobileNumber,
                                              @Field("user_type") String typeOfUser);
    @FormUrlEncoded
    @POST("user_forget_password.php")
    Call<ServerResponse> resetDriverPassword(@Field("mobile_number") String mobileNumber,
                                           @Field("password") String password,
                                           @Field("user_type") String userType);
    @FormUrlEncoded
    @POST("user_forget_password.php")
    Call<ServerResponse> resetUserPassword(@Field("mobile_number") String mobileNumber,
                                             @Field("password") String password,
                                             @Field("user_type") String userType);
    @FormUrlEncoded
    @POST("car_details_registeration.php")
    Call<ServerResponse> carDetailsUpload(@Field("mobile_number") String mobileNumber,
                                          @Field("car_catagory") String carCatagory,
                                          @Field("car_catagory_model") String cat_CatagoryModel,
                                          @Field("car_number") String carNumber,
                                          @Field("car_customer_travel_with") String customerTravelWith,
                                          @Field("car_fuel_type") String carFuelType,
                                          @Field("car_manfuctinf_date") String carManfucturing,
                                          @Field("car_seating_capacity") String carSeatingCapacity,
                                          @Field("car_trip_type") int carTripType,
                                          @Field("car_local_limited_km") int carLocalLimiteKm,
                                          @Field("car_local_price") int carlocalPrice,
                                          @Field("car_local_time_limited_period") int timeLimited,
                                          @Field("car_local_exceed_per_km") int exceedPerKm,
                                          @Field("car_local_driver_beta") int localDriverBeta,
                                          @Field("car_local_waiting_charges") int localWaitingCharges,
                                          @Field("car_long_trip") int carLongTrip,
                                          @Field("car_long_per_km_price") int carLongPerKmPrice,
                                          @Field("car_long_driver_beta") int longDriverBeta,
                                          @Field("car_long_waiting_charges") int longWaitingCharges,
                                          @Field("car_long_is_halting_charges") int isHalting,
                                          @Field("car_long_halting_charges") int haltingChargePrice,
                                          @Field("car_is_hills_allowance") int isHills,
                                          @Field("car_hills_allowance") int hillsAllowancePrice);

    /**
     *
     * @param file
     * @param mob
     * @param i
     * @param carNumber
     * @return
     */
    @Multipart
    @POST("upload.php")
    Call<ServerResponse> imageUpload(@Part MultipartBody.Part file,
                                     @Part("name") String mob,
                                     @Part("id") int i,
                                     @Part("car_number") String carNumber);

    /**
     *
     * @return
     */
    @GET("get_cars_details.php")
    Call<ServerResponse> getCarListes(@Query("locality") String locality, @Query("number_of_seating") int seatingCapacity);
    @GET("get_driver_car_list.php")
    Call<ServerResponse> getDriverCarList(@Query("driver_phone_number") String mubileNumber);
    /**
     *
     * @param CarNumber
     * @return
     */
    @GET("complete_details_of_car.php")
    Call<ServerResponse> getCarCompleteDetails(@Query("car_number") String CarNumber);

}
