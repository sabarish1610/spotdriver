package com.miziontrix.chatapp.retrofit;

/**
 * Created by sabarish on 21/6/17.
 */

public class ServerResponse {
    private String result;
    private String message;
    private com.miziontrix.chatapp.retrofit.Users users;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public com.miziontrix.chatapp.retrofit.Users getUsers() {
        return users;
    }
}
