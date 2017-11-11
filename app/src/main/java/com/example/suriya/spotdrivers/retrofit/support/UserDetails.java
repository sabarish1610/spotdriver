package com.example.suriya.spotdrivers.retrofit.support;

/**
 * Created by Suriya on 30-07-2017.
 */

public class UserDetails {
    private String name;
    private String last_name;
    private String age;
    private String gender;
    private String user_mobile;
    private String emergency_contact_1;
    private String emergency_contact_2;
    private String image_path;

    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAge() {
        return age;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public String getEmergency_contact_1() {
        return emergency_contact_1;
    }

    public String getEmergency_contact_2() {
        return emergency_contact_2;
    }

    public String getImage_path() {
        return image_path;
    }
}
