package com.radford.clubmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.radford.clubmobile.adapters.ClubAdapter;
import com.radford.clubmobile.delegates.ItemSelector;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.models.User;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends NavigationDrawerActivity implements Callback<List<Club>>,ItemSelector<Club> {
    private RecyclerView clubRecyclerView;
    private RecyclerView.Adapter clubAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ClubService clubService;
    private int clubIdToOpen = -1;

    @Override
    public int layoutId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public String title() {
        return "My Clubs";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clubService = ClubServiceProvider.getService();
        clubRecyclerView = findViewById(R.id.club_recycler_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pushToken = sharedPreferences.getString("PUSH_TOKEN", "");
        String userPushToken = UserManager.getUser().getPushToken();
        if(!pushToken.equals("") && (userPushToken == null || !userPushToken.equals(pushToken))) {
            User updateUser = new User();
            updateUser.setPushToken(pushToken);
            clubService.updateUser(UserManager.getSessionId(), updateUser).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

        if(sharedPreferences.getBoolean("OPENING_PUSH", false)) {
            clubIdToOpen =  sharedPreferences.getInt("CLUB_ID", -1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        clubService.getClubs(UserManager.getSessionId()).enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
        if(response.isSuccessful() && response.body() != null) {
            clubAdapter = new ClubAdapter(response.body(), this, this);
            layoutManager = new LinearLayoutManager(this);

            clubRecyclerView.setLayoutManager(layoutManager);
            clubRecyclerView.setAdapter(clubAdapter);

            for (Club club : response.body()) {
                if(club.getId() == clubIdToOpen) {
                    itemSelected(club);
                    clubIdToOpen = -1;
                    break;
                }
            }
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to get clubs").show();
        }
    }

    @Override
    public void onFailure(Call<List<Club>> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to get clubs").show();
    }

    @Override
    public void itemSelected(Club item) {
        Intent intent = new Intent(this, ClubActivity.class);
        intent.putExtra("club", item);

        startActivity(intent);
    }
}
