package com.radford.clubmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.radford.clubmobile.managers.UserManager;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean("OPENING_PUSH", true).putInt("CLUB_ID", Integer.valueOf(intent.getStringExtra("clubId"))).apply();

        Intent intentToStart;
        if(UserManager.getSessionId() != null) {
            intentToStart = new Intent(context, DashboardActivity.class);
        } else {
            intentToStart = new Intent(context, LoginActivity.class);
        }

        intentToStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentToStart);
    }
}
