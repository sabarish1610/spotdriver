package com.example.suriya.spotdrivers.retrofit.support;

import java.util.List;

/**
 * Created by Suriya on 19-07-2017.
 */

public class ServerResponse {
    private String message;
    private String result;
    private String name;
    private String path;
    private DriverDetails driver;
    private List<DriverDetails> driverList;
    private List<CarDetails> carList;
    private User user;
    private String id;
    private int withCar;
    private CarDetails carDetails;
    private UserDetails userDetails;

    public int getWithCar() {
        return withCar;
    }

    public CarDetails getCarDetails() {
        return carDetails;
    }

    public List<CarDetails> getCarList() {
        return carList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public DriverDetails getDriver() {
        return driver;
    }

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }

    public User getUser() {
        return user;
    }

    public List<DriverDetails> getDriverList() {
        return driverList;
    }
}
