package clubs.mobile.radford.clubmobile.networking;

import clubs.mobile.radford.clubmobile.models.LoginRequest;
import clubs.mobile.radford.clubmobile.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClubService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
