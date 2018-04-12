package com.radford.clubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Announcement;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAnnouncementActivity extends ToolbarActivity implements Callback<Announcement>, View.OnClickListener {
    public Club club;
    public TextView name, description;
    @Override
    public int layoutId() {
        return R.layout.activity_create_announcement;
    }

    @Override
    public String title() {
        return "Create announcement";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        club = (Club) getIntent().getSerializableExtra("club");

        name = findViewById(R.id.announcement_name);
        description = findViewById(R.id.announcement_description);

        Button createButton = findViewById(R.id.sendAnnouncementButton);
        createButton.setOnClickListener(this);
    }

    @Override
    public void onResponse(Call<Announcement> call, Response<Announcement> response) {
        if(response.isSuccessful()) {
            Intent intent = new Intent().putExtra("announcement", response.body());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to create announcement").show();
        }
    }

    @Override
    public void onFailure(Call<Announcement> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to create announcement").show();
    }

    @Override
    public void onClick(View v) {
        Announcement announcement = new Announcement();
        announcement.setClubId(club.getId());
        announcement.setName(name.getText().toString());
        announcement.setDescription(description.getText().toString());
        ClubServiceProvider.getService().createAnnouncement(UserManager.getSessionId(), announcement).enqueue(this);
    }
}
