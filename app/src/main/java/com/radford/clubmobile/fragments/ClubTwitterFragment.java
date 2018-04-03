package com.radford.clubmobile.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.radford.clubmobile.ClubActivity;
import com.radford.clubmobile.R;
import com.radford.clubmobile.adapters.TwitterAdapter;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubTwitterFragment extends Fragment implements Callback<Club> {
    private TwitterAdapter twitterAdapter;
    private RecyclerView twitterRecyclerView;

    public ClubTwitterFragment() {
        twitterAdapter = new TwitterAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_club_twitter, container, false);
        Club club = ((ClubActivity) getActivity()).club;

        twitterRecyclerView = v.findViewById(R.id.twitter_recycler_view);

        twitterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        twitterRecyclerView.setAdapter(twitterAdapter);

        ClubService service = ClubServiceProvider.getService();
        service.getClubDetails(UserManager.getSessionId(), club.getId()).enqueue(this);

        return v;
    }

    @Override
    public void onResponse(Call<Club> call, Response<Club> response) {
        if(response.isSuccessful() && response.body() != null) {
            if(response.body().getTweets() != null) {
                twitterAdapter.load(response.body().getTweets());
            }
        } else {
            AlertHelper.makeErrorDialog(getContext(), "Failed to get tweets").show();
        }
    }

    @Override
    public void onFailure(Call<Club> call, Throwable t) {
        AlertHelper.makeErrorDialog(getContext(), "Failed to get tweets").show();
    }
}

