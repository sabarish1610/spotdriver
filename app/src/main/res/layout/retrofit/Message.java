package com.miziontrix.chatapp.retrofit;

/**
 * Created by Suriya on 27-06-2017.
 */

public class Message {
    private String user_id;
    private String message_id;
    private String message;
    private String time;

    public String getUserId() {
        return user_id;
    }

    public String getMessageId() {
        return message_id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return time;
    }
}
