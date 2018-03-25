package clubs.mobile.radford.clubmobile.networking;

import java.util.List;

import clubs.mobile.radford.clubmobile.models.Club;
import clubs.mobile.radford.clubmobile.models.LoginRequest;
import clubs.mobile.radford.clubmobile.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClubService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("clubs")
    Call<List<Club>> getClubs(@Header("x-session-id") String sessionId);

    @GET("clubs/all")
    Call<List<Club>> getAllClubs(@Header("x-session-id") String sessionId);

    @PUT("clubs/join/{id}")
    Call<Void> joinClub(@Header("x-session-id") String sessionId, @Path("id") int clubId);

    @DELETE("clubs/leave/{id}")
    Call<Void> leaveClub(@Header("x-session-id") String sessionId, @Path("id") int clubId);
}