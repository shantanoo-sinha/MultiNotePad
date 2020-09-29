package com.shantanoo.multi_notepad.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shantanoo on 9/27/2020.
 */
public class Util {

    private static final String TAG = "Util";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm aaa", Locale.US);

    public static String formatDateAsString(Date timestamp) {
        if(timestamp == null)
            return null;
        return dateFormat.format(timestamp);
    }

    public static Date formatStringAsDate(String input){
        if (TextUtils.isEmpty(input))
            return null;
        try {
            return dateFormat.parse(input.trim());
        } catch (ParseException e) {
            Log.e(TAG, "formatStringAsDate: Exception", e);
        }
        return null;
    }
}
