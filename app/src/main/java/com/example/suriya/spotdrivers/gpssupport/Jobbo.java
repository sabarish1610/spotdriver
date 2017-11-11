package com.example.suriya.spotdrivers.gpssupport;

/**
 * Created by Suriya on 25-09-2017.
 */

public class Jobbo {
    public static final int GET_INTO_CAR_LOCATION_PAGE = 01;
    public static String locality;
    public static String moreDetailsAboutLocation;
    public static int getIntoCarLocationPage;

    public Jobbo(String locality, String moreDetailsAboutLocation, int getIntoCarLocationPage) {
        this.locality = locality;
        this.moreDetailsAboutLocation = moreDetailsAboutLocation;
        this.getIntoCarLocationPage = getIntoCarLocationPage;
    }

    public int getGetIntoCarLocationPage() {
        return getIntoCarLocationPage;
    }
    public void setGetIntoCarLocationPage(int getIntoCarLocationPage) {
        this.getIntoCarLocationPage = getIntoCarLocationPage;
    }
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMoreDetailsAboutLocation() {
        return moreDetailsAboutLocation;
    }

    public void setMoreDetailsAboutLocation(String moreDetailsAboutLocation) {
        this.moreDetailsAboutLocation = moreDetailsAboutLocation;
    }
}
