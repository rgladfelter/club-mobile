package com.radford.clubmobile.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.radford.clubmobile.ClubActivity;
import com.radford.clubmobile.EventActivity;
import com.radford.clubmobile.R;
import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.Club;
import com.radford.clubmobile.models.Event;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;
import com.radford.clubmobile.utils.DefaultCalendarDecorator;
import com.radford.clubmobile.utils.EventCalendarDecorator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubCalendarFragment extends Fragment implements Callback<List<Event>> {
    private MaterialCalendarView mcv;
    private Club club;
    public ClubCalendarFragment() {
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
        View v = inflater.inflate(R.layout.fragment_club_calendar, container, false);
        club = ((ClubActivity) getActivity()).club;

        mcv = v.findViewById(R.id.calendarView);

        ClubServiceProvider.getService().getEventsByClub(UserManager.getSessionId() ,club.getId()).enqueue(this);
        return v;
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
        if(response.isSuccessful() && response.body() != null) {
            final List<Event> events = response.body();
            final List<CalendarDay> days = getCalendarDays(events);
            mcv.addDecorator(new DefaultCalendarDecorator(days, getResources().getDrawable(R.drawable.transparent)));
            mcv.addDecorator(new EventCalendarDecorator(days, Color.RED));
            mcv.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    if(days.contains(date)) {
                        final List<Event> eventsForDay = getEventsForDay(events, date);
                        List<String> eventNames = getEventNames(eventsForDay);

                        new MaterialDialog.Builder(getContext())
                                .title("Events")
                                .items(eventNames)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        Intent intent = new Intent(getContext(), EventActivity.class);
                                        intent.putExtra("event", eventsForDay.get(position));
                                        intent.putExtra("club", club);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                }
            });
        } else {
            AlertHelper.makeErrorDialog(getContext(), "Failed to get events").show();
        }
    }

    private List<Event> getEventsForDay(List<Event> events, CalendarDay date) {
        List<Event> eventsForDay = new ArrayList<>();
        for (Event event: events) {
            if(CalendarDay.from(event.getStartTime()).equals(date))
                eventsForDay.add(event);
        }
        return eventsForDay;
    }

    private List<String> getEventNames(List<Event> events) {
        List<String> eventNames = new ArrayList<>();
        for (Event event: events) {
            eventNames.add(event.getName());
        }
        return eventNames;
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        AlertHelper.makeErrorDialog(getContext(), "Failed to get events").show();
    }

    private List<CalendarDay> getCalendarDays(List<Event> events) {
        List<CalendarDay> days = new ArrayList<>();
        for (Event event : events) {
            days.add(CalendarDay.from(event.getStartTime()));
        }

        return days;
    }
}