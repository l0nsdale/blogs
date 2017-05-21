package com.pashkevich.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.pashkevich.app.constants.Constants.Common.ONE;

/**
 * Created by Vlad on 22.03.17.
 */
public class DateUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
    private static final Calendar calendar = Calendar.getInstance();

    public static Date getNextDayDate() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, ONE);
        return calendar.getTime();
    }

    public static String getDateString(Date date) {
        return dateFormat.format(date);
    }

}
