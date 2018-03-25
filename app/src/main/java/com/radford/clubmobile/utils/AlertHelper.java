package com.radford.clubmobile.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class AlertHelper {
    public static AlertDialog makeErrorDialog(Context context, String error) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setMessage(error);
        return alert;
    }
}