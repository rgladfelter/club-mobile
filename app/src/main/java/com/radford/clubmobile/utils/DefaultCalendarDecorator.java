package com.radford.clubmobile.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

public class DefaultCalendarDecorator implements DayViewDecorator {
    private final List<CalendarDay> events;
    private final Drawable drawable;

    public DefaultCalendarDecorator(List<CalendarDay> events, Drawable drawable) {
        this.events = events;
        this.drawable = drawable;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return !events.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.setSelectionDrawable(drawable);
    }
}
