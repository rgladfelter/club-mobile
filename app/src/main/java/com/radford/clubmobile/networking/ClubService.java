package com.radford.clubmobile.networking;

import java.util.List;
import java.util.Map;

import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.models.LoginRequest;
import com.radford.clubmobile.models.LoginResponse;
import com.radford.clubmobile.models.RegistrationRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ClubService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Multipart
    @POST("register")
    Call<Void> register(@PartMap() Map<String, RequestBody> partMap,
                         @Part MultipartBody.Part file);

    @GET("clubs")
    Call<List<Club>> getClubs(@Header("x-session-id") String sessionId);

    @GET("clubs/{id}")
    Call<Club> getClubDetails(@Header("x-session-id") String sessionId, @Path("id") int clubId);

    @GET("clubs/all")
    Call<List<Club>> getAllClubs(@Header("x-session-id") String sessionId);

    @PUT("clubs/join/{id}")
    Call<Void> joinClub(@Header("x-session-id") String sessionId, @Path("id") int clubId);

    @DELETE("clubs/leave/{id}")
    Call<Void> leaveClub(@Header("x-session-id") String sessionId, @Path("id") int clubId);

    @POST("register/resend")
    Call<Void> resendEmail(@Body LoginRequest egistrationRequest);
}