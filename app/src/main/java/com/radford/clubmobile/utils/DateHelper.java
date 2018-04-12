package com.radford.clubmobile.utils;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {
    public static String toTime(Date date) {
        return new SimpleDateFormat("h:mm a").format(date);
    }

    public static String toDate(Date date) {
        return new SimpleDateFormat("EEEE, MMMM d").format(date);
    }

    public static String timeAgo(Date date) {
        int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        return TimeAgo.using(date.getTime() + offset);
    }
}
