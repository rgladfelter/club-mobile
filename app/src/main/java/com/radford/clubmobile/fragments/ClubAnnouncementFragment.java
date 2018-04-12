package com.radford.clubmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.radford.clubmobile.ClubActivity;
import com.radford.clubmobile.CreateAnnouncementActivity;
import com.radford.clubmobile.R;
import com.radford.clubmobile.adapters.AnnouncementAdapter;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Announcement;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ClubAnnouncementFragment extends Fragment implements Callback<List<Announcement>>, View.OnClickListener {
    private static final int CREATE_REQUEST_CODE = 12;
    private AnnouncementAdapter announcementAdapter;
    private RecyclerView announcementRecyclerView;
    private Club club;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_club_announcement , container, false);
        club = ((ClubActivity) getActivity()).club;

        announcementAdapter = new AnnouncementAdapter(getContext());
        announcementRecyclerView = v.findViewById(R.id.announcements);

        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        announcementRecyclerView.setAdapter(announcementAdapter);

        ClubService service = ClubServiceProvider.getService();
        service.getAnnouncementsByClub(UserManager.getSessionId(), club.getId()).enqueue(this);


        FloatingActionButton fab = v.findViewById(R.id.createAnnouncementFab);
        fab.setOnClickListener(this);
        return v;
    }

    @Override
    public void onResponse(Call<List<Announcement>> call, Response<List<Announcement>> response) {
        if(response.isSuccessful() && response.body() != null) {
            announcementAdapter.load(response.body());
        } else {
            AlertHelper.makeErrorDialog(getContext(), "Failed to get announcements").show();
        }
    }

    @Override
    public void onFailure(Call<List<Announcement>> call, Throwable t) {
        AlertHelper.makeErrorDialog(getContext(), "Failed to get announcements").show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), CreateAnnouncementActivity.class);
        intent.putExtra("club", club);
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CREATE_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Announcement announcement = (Announcement) data.getSerializableExtra("announcement");
                announcementAdapter.add(announcement);
                announcementRecyclerView.scrollToPosition(0);
            }
        }
    }
}