package com.radford.clubmobile.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radford.clubmobile.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubServiceProvider {
    private static ClubService service;

    public static ClubService getService() {
        if (service == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service = retrofit.create(ClubService.class);
        }

        return service;
    }

}
