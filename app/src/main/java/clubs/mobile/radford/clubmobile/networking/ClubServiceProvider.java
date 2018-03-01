package clubs.mobile.radford.clubmobile.networking;

import clubs.mobile.radford.clubmobile.networking.ClubService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubServiceProvider {
    private static ClubService service;

    public static ClubService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://radford-clubs.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(ClubService.class);
        }

        return service;
    }

}
