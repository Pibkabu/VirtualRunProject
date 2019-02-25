package com.example.quynh.virtualrunproject.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by quynh on 2/11/2019.
 */

public class DateFormatHandler {
    public static Date stringToDate(String pattern, String dateString){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = format.parse(dateString);
        }catch (Exception ex){
            Log.e("Format Date", "stringToDate: ", ex);
        }
        return date;
    }

    public static String dateToString(String pattern, Date date){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        return format.format(date);
    }
}
