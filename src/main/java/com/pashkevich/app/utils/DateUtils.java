package com.pashkevich.app.utils;

import java.util.Calendar;
import java.util.Date;

import static com.pashkevich.app.constants.Constants.Common.ONE;

/**
 * Created by Vlad on 22.03.17.
 */
public class DateUtils {

    private static Calendar calendar = Calendar.getInstance();

    public static Date getNextDayDate() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, ONE);
        return calendar.getTime();
    }

}
