package com.radford.clubmobile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.models.Event;
import com.radford.clubmobile.utils.DateHelper;

public class EventActivity extends ToolbarActivity{
    public Club club;
    public Event event;
    @Override
    public int layoutId() {
        return R.layout.activity_event;
    }

    @Override
    public String title() {
        return ((Event) getIntent().getSerializableExtra("event")).getName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        club = (Club) getIntent().getSerializableExtra("club");
        event = (Event) getIntent().getSerializableExtra("event");

        ((TextView) findViewById(R.id.eventDate)).setText(DateHelper.toDate(event.getStartTime()));
        ((TextView) findViewById(R.id.startTimeText)).setText(DateHelper.toTime(event.getStartTime()));
        ((TextView) findViewById(R.id.endTimeText)).setText(DateHelper.toTime(event.getEndTime()));
        ((TextView) findViewById(R.id.eventDescription)).setText(event.getDescription());
        ((TextView) findViewById(R.id.eventLocation)).setText(event.getLocation());
        ((TextView) findViewById(R.id.clubName)).setText(club.getName());

        ImageView clubImage = findViewById(R.id.clubImage);
        Glide.with(this)
                .load(Constants.BaseImageUrl + club.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(clubImage);

    }
}
