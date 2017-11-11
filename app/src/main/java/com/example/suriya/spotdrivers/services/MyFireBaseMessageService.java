package com.example.suriya.spotdrivers.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.suriya.spotdrivers.Notification.Notification;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Suriya on 25-07-2017.
 */

public class MyFireBaseMessageService extends FirebaseMessagingService {
    public static String  chatRoomId,message,title;
    private SharedPreferences sharedPreferences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreferences = getSharedPreferences(SupportConstant.LOGGED_IN,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Intent pushNotificatiom = new Intent("push");
        pushNotificatiom.putExtra("message",message);
        pushNotificatiom.putExtra("chat_room_id",chatRoomId);
        JSONObject jsonObject;
        if(remoteMessage.getData().size()>0)
        {
            Log.e(TAG, "Data Payload: " + gson.toJson(remoteMessage.getData()));
            try {
                jsonObject = new JSONObject(gson.toJson(remoteMessage.getData()));
                getPushNotification(jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        Notification notification = new Notification(getApplicationContext());
        if(sharedPreferences.getBoolean(SupportConstant.LOGGED_IN_STATUS,false));
        {
            if (!Notification.isAppIsBackground(getApplicationContext()))
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificatiom);
            else
                notification.pushNotification(title, message);
        }
    }
   private void getPushNotification(JSONObject jsonObject) throws JSONException {
       Log.e(TAG, "Notification JSON " + jsonObject.toString());
        // JSONObject data1 = jsonObject.getJSONObject("data");
        title = jsonObject.getString("title");
        message = jsonObject.getString("message");
        chatRoomId = jsonObject.getString("chatRoomId");
       SharedPreferences sharedPreferences = getSharedPreferences("count", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString(chatRoomId+"msg",message);
       editor.putInt(chatRoomId, sharedPreferences.getInt(chatRoomId,0)+1);
       editor.commit();
       //String mess = sharedPreferences.getString(chatRoomId+"msg", message);
      // Log.i("mess",mess);
        Log.e(TAG,"title = "+ title);
        Log.e(TAG,"message = "+ message);
        Log.e(TAG,"chatRoomId = "+ chatRoomId);

    }
}
