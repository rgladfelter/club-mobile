package com.radford.clubmobile.networking;

import com.radford.clubmobile.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubServiceProvider {
    private static ClubService service;

    public static ClubService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(ClubService.class);
        }

        return service;
    }

}
