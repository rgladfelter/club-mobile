package com.radford.clubmobile.utils;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.List;

public class EventCalendarDecorator implements DayViewDecorator {
    private final List<CalendarDay> events;
    private final int color;

    public EventCalendarDecorator(List<CalendarDay> events, int color) {
        this.events = events;
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return events.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, color));
    }
}
