package com.radford.clubmobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.List;

import com.radford.clubmobile.adapters.ClubAdapter;
import com.radford.clubmobile.delegates.ItemSelector;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindClubActivity extends NavigationDrawerActivity implements SearchView.OnQueryTextListener, Callback<List<Club>>,ItemSelector<Club> {
    private RecyclerView clubRecyclerView;
    private ClubAdapter clubAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ClubService clubService;
    private Context context;
    private DrawerLayout layout;

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
        context = this;
        layout = findViewById(R.id.drawer_layout);
        clubRecyclerView = findViewById(R.id.find_club_recycler_view);

        clubService = ClubServiceProvider.getService();
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
            clubAdapter = new ClubAdapter(response.body(), this);
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

    @Override
    public void itemSelected(final Club item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to join " + item.getName() + "?")
                .setCancelable(false)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clubService.joinClub(UserManager.getSessionId(), item.getId()).enqueue(new JoinCallback());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private class JoinCallback implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.isSuccessful()) {
                Snackbar.make(layout, "Joined club", Snackbar.LENGTH_LONG).show();
            } else {
                AlertHelper.makeErrorDialog(context, "Failed to join club").show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            AlertHelper.makeErrorDialog(context, "Failed to join club").show();
        }
    }
}
