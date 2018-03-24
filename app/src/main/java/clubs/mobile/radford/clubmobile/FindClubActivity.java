package clubs.mobile.radford.clubmobile;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

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

public class FindClubActivity extends NavigationDrawerActivity implements SearchView.OnQueryTextListener, Callback<List<Club>> {
    private RecyclerView clubRecyclerView;
    private ClubAdapter clubAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public int layoutId() {
        return R.layout.activity_find_club;
    }

    @Override
    public String title() {
        return "Find clubs";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clubRecyclerView = findViewById(R.id.find_club_recycler_view);

        ClubService clubService = ClubServiceProvider.getService();

        clubService.getAllClubs(UserManager.getSessionId()).enqueue(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        clubAdapter.getFilter().filter(newText);
        return true;
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
