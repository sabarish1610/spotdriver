package com.example.suriya.spotdrivers.retrofit.support;

/**
 * Created by Suriya on 20-07-2017.
 */

public class DriverDetails {
    private String name;
    private String last_name;
    private String experience;
    private String charges;
    private String age;
    private String gender;
    private String mobile_number;
    private String profile_image_path;
    private String driver_licence_type;
    private String driver_licence_number;
    private String driver_job_catagory;
    private String location_1;
    private String location_2;
    private int withCar;

    public int getWithCar() {
        return withCar;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getExperience() {
        return experience;
    }

    public String getCharges() {
        return charges;
    }

    public String getAge() {
        return age;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getProfile_image_path() {
        return profile_image_path;
    }

    public String getDriver_licence_type() {
        return driver_licence_type;
    }

    public String getDriver_licence_number() {
        return driver_licence_number;
    }

    public String getDriver_job_catagory() {
        return driver_job_catagory;
    }

    public String getLocation_1() {
        return location_1;
    }

    public String getLocation_2() {
        return location_2;
    }
}
