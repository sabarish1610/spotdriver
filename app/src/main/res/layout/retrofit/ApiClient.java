package com.miziontrix.chatapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sabarish on 20/6/17.
 */

public class ApiClient {
    public static final String URL = "http://10.0.2.2/miziontrix_chat/";//http://10.0.2.2/miziontrix_chat/http://scholl-android.miziontrix.com/
    public static Retrofit retrofit;
    public static Retrofit getApiClient()
    {
        if(retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
            return retrofit;

    }
}
