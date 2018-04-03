package com.radford.clubmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radford.clubmobile.ClubActivity;
import com.radford.clubmobile.R;
import com.radford.clubmobile.models.Club;

public class ClubDetailFragment extends Fragment {

    public ClubDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_club_detail, container, false);
        Club club = ((ClubActivity) getActivity()).club;

        TextView clubDescription = v.findViewById(R.id.club_description);
        clubDescription.setText(club.getDescription());

        return v;
    }

}