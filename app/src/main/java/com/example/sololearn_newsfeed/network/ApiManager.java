package com.example.sololearn_newsfeed.network;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {


    private static final String BASE_URL = "https://content.guardianapis.com/";


    private static TheGuardianService mApiClient;

    public static TheGuardianService getApiClient() {
        if (mApiClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .build();


            mApiClient = retrofit.create(TheGuardianService.class);
        }


        return mApiClient;

    }

}
