package ru.artyrian.hibersearch.search.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PC on 02.05.14.
 */
public class DateConverter {
    private static final Log log = new Log4JLogger();


    public static final String DATE_FORMAT_D_M_Y = "dd.MM.yyyy";
    private static int MIN_YEAR_RANGE = 2010;
    private static int MAX_YEAR_RANGE = 2100;

    private final static DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_FORMAT_D_M_Y);

    public static Date convertToDate(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.toDate();
    }

    public static String convertToString(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(DATE_FORMAT_D_M_Y).format(date);
    }

    public static String convertToString(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dtf.print(dateTime);
    }

    public static Date convertToDate(String dateString) throws ParseException {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_D_M_Y);
        simpleDateFormat.setLenient(false);

        return simpleDateFormat.parse(dateString);
    }

    public static DateTime convertToDateTime(String dateString) throws ParseException {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_D_M_Y);
        simpleDateFormat.setLenient(false);

        return new DateTime(simpleDateFormat.parse(dateString));
    }

    public static Date newCopy(final Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }


    public static boolean isInRange(String date) {
        String datePattern = "\\d{1,2}.\\d{1,2}.\\d{4}";
        boolean isInRange = false;

        date = (date != null) ? date : "";
        if (date.matches(datePattern)) {
            try {
                Date parsedDate = convertToDate(date);
                int year = parsedDate.getYear() + 1900;
                if (MIN_YEAR_RANGE <= year && year <= MAX_YEAR_RANGE) {
                    isInRange = true;
                }
            } catch (ParseException e) {
                log.error("Date " + date + " must be converted.");
            }
        }

        return isInRange;
    }

    public static boolean isInRange(Date date) {
        return DateConverter.isInRange(DateConverter.convertToString(date));
    }
}
