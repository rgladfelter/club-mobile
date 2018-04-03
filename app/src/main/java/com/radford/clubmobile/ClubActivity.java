package com.radford.clubmobile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.radford.clubmobile.adapters.ViewPagerAdapter;
import com.radford.clubmobile.fragments.ClubAnnouncementFragment;
import com.radford.clubmobile.fragments.ClubDetailFragment;
import com.radford.clubmobile.fragments.ClubTwitterFragment;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubActivity extends ToolbarActivity implements Callback<Void> {
    public Club club;
    @Override
    public int layoutId() {
        return R.layout.activity_club;
    }

    @Override
    public String title() {
        return ((Club) getIntent().getSerializableExtra("club")).getName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new ClubDetailFragment());
        adapter.addFragment(new ClubAnnouncementFragment());
        adapter.addFragment(new ClubTwitterFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_announcement);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_twitter);

        club = (Club) getIntent().getSerializableExtra("club");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leave_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_leave) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to leave " + club.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ClubService clubService = ClubServiceProvider.getService();
                            clubService.leaveClub(UserManager.getSessionId(), club.getId()).enqueue(ClubActivity.this);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
            finish();
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to leave club").show();
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to leave club").show();
    }
}
