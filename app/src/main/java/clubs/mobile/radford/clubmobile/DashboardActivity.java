package clubs.mobile.radford.clubmobile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import clubs.mobile.radford.clubmobile.adapters.ClubAdapter;
import clubs.mobile.radford.clubmobile.managers.UserManager;
import clubs.mobile.radford.clubmobile.models.Club;
import clubs.mobile.radford.clubmobile.networking.ClubService;
import clubs.mobile.radford.clubmobile.networking.ClubServiceProvider;
import clubs.mobile.radford.clubmobile.utils.AlertHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends NavigationDrawerActivity implements Callback<List<Club>> {
    private RecyclerView clubRecyclerView;
    private RecyclerView.Adapter clubAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        clubRecyclerView = findViewById(R.id.club_recycler_view);

        ClubService clubService = ClubServiceProvider.getService();

        clubService.getClubs(UserManager.getSessionId()).enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
        if(response.isSuccessful()) {
            clubAdapter = new ClubAdapter(response.body());
            layoutManager = new LinearLayoutManager(this);

            clubRecyclerView.setLayoutManager(layoutManager);
            clubRecyclerView.setAdapter(clubAdapter);
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to get clubs").show();
        }
    }

    @Override
    public void onFailure(Call<List<Club>> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to get clubs").show();
    }
}