package com.instance.ceg.appFragments;

import android.text.format.DateFormat;

import com.instance.ceg.appData.NewsFeedItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

class SortByDate implements Comparator<NewsFeedItems> {


    @Override
    public int compare(NewsFeedItems t, NewsFeedItems t1) {
        Date date1 = stringToDate(t.getTimeStamp());
        Date date2 = stringToDate(t1.getTimeStamp());



        return 0;
    }

    private static Date stringToDate(long date){
        Date returnDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            returnDate = simpleDateFormat.parse(String.valueOf(date));
        }catch (Exception e) {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                returnDate = simpleDateFormat1.parse(String.valueOf(date));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return returnDate;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(date);
//        String dateFormatted = DateFormat.format("yyyy-MM-dd HH:mm", date).toString();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    }
}
