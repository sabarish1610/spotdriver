package com.miziontrix.chatapp.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sabarish on 20/6/17.
 */

public interface RetInterface {
    @FormUrlEncoded
    @POST("insert_user.php")
    Call<ServerResponse> postId(@Field("user_name") String userName, @Field("user_email") String userEmail,
                                @Field("mobile_id") String mobileId);
    @GET("user_retrieve.php")
    Call<List<Users>> getRegisteredUser(@Query("email_id") String emailId);
    @FormUrlEncoded
    @POST("login.php")
    Call<ServerResponse> login(@Field("user_name") String userName, @Field("user_email") String userEmail);
    @FormUrlEncoded
    @POST("message.php")
    Call<ServerResponse> sendMessage(@Field("send_to_user_id") String sendToUserId, @Field("user_id") String userId, @Field("message") String textMessage,
                                     @Field("chat_room_id") String chatRoomId);
    @GET("message_retrieve.php")
    Call<List<Message>> getMessage(@Query("chat_room_id") String chatRoomId);
    @GET("getChatRoomDetails.php")
    Call<List<ModelChatRoom>> getRoomDetails(@Query("user_id") String userId);
    @FormUrlEncoded
    @POST("create_chat_room.php")
    Call<ServerResponse> createChatRoom(@Field("user_id") String userId, @Field("send_to_user_id") String sendTiioUserId);

}
