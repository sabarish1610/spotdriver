package com.example.suriya.spotdrivers.support;

/**
 * Created by Suriya on 29-07-2017.
 */

public class LoggedInDetails {
    private boolean logginStatus;
    private String logginedInType;
    private String userLogedInId;

    public LoggedInDetails(boolean logginStatus, String logginedInType, String userLogedInId) {
        this.logginStatus = logginStatus;
        this.logginedInType = logginedInType;
        this.userLogedInId = userLogedInId;
    }

    public boolean isLogginStatus() {
        return logginStatus;
    }

    public String getLogginedInType() {
        return logginedInType;
    }

    public String getUserLogedInId() {
        return userLogedInId;
    }
}
