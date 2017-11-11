package com.example.suriya.spotdrivers.retrofit.support;

import android.content.SharedPreferences;

/**
 * Created by Suriya on 24-07-2017.
 */

public class User {
    private String mobile;
    private String message;
    private String chatRoomId;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMessage() {
        return message;
    }
}
