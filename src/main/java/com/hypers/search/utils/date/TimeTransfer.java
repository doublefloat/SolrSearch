package com.hypers.search.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author long
 * @since 2017/9/15
 */
public class TimeTransfer {

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private static DateFormat format = new SimpleDateFormat(DATE_FORMAT);

  public static final String SECOND = "second";
  public static final String MINUTE = "min";
  public static final String HOUR = "hour";
  public static final String DAY = "day";
  public static final String WEEK = "week";
  public static final String MONTH = "month";
  public static final String YEAR = "year";

  public static final long SECOND_MILI = 1000;
  public static final long MINUTE_MILI = 60 * SECOND_MILI;
  public static final long HOUR_MILI = 60 * MINUTE_MILI;
  public static final long DAY_MILI = 24 * HOUR_MILI;
  public static final long WEEK_MILI = 7 * DAY_MILI;
  public static final long MONTH_MILI_30 = 30 * DAY_MILI; // TODO different month
  public static final long YEAR_MILI_365 = 365 * DAY_MILI; // TODO year has different day

  public static final String YESTERDAY = "yesterday";
  public static final String DAYBEFORE = "daybefore";
  public static final String LASTWEEK = "lastweek";
  public static final String LASTMONTH = "lastmonth";
  public static final String LASTYEAR = "lastyear";
  public static final String TODAY = "today";
  public static final String THISWEEK = "thisweek";
  public static final String THISMONTH = "thismonth";
  public static final String THISYEAR = "thisyear";

  public static final int MILLISECOND_ERROR_DISTANCE = 1;

  public static Date roundDate(Date date, String granularity) {
    Calendar oldCld = Calendar.getInstance(); // default timezone and locale is used
    oldCld.setTime(date);
    Calendar newCld = Calendar.getInstance();
    newCld.setTime(date);

    // break isn't need
    switch (granularity) {
      case YEAR:
        newCld.set(Calendar.MONTH, 0);
      case MONTH:
        newCld.set(Calendar.DAY_OF_MONTH, 0);
      case DAY:
        newCld.set(Calendar.HOUR_OF_DAY, 0);
      case HOUR:
        newCld.set(Calendar.MINUTE, 0);
      case MINUTE:
        newCld.set(Calendar.SECOND, 0);
      case SECOND:
        newCld.set(Calendar.MILLISECOND, MILLISECOND_ERROR_DISTANCE);
        break;
      case WEEK:
        newCld.set(Calendar.DAY_OF_WEEK, 1);
        newCld.set(Calendar.HOUR_OF_DAY, 0);
        newCld.set(Calendar.MINUTE, 0);
        newCld.set(Calendar.SECOND, 0);
        newCld.set(Calendar.MILLISECOND, MILLISECOND_ERROR_DISTANCE);
        break;
    }

    return newCld.getTime();
  }

  public static Date newDateByOffset(Date origin, String granularity, int offset) {
    Calendar originCld = Calendar.getInstance();
    originCld.setTime(origin);
    Calendar newCld = Calendar.getInstance();
    newCld.setTime(origin);

    switch (granularity) {
      case YEAR:
        newCld.set(Calendar.YEAR, originCld.get(Calendar.YEAR) + offset);
        break;
      case MONTH:
        newCld.set(Calendar.MONTH, originCld.get(Calendar.MONTH) + offset);
        break;
      case WEEK:
        newCld.set(Calendar.WEEK_OF_YEAR, originCld.get(Calendar.WEEK_OF_YEAR) + offset);
        break;
      case DAY:
        newCld.set(Calendar.DAY_OF_MONTH, originCld.get(Calendar.DAY_OF_MONTH) + offset);
        break;
      case HOUR:
        newCld.set(Calendar.HOUR_OF_DAY, originCld.get(Calendar.HOUR_OF_DAY) + offset);
        break;
      case MINUTE:
        newCld.set(Calendar.MINUTE, originCld.get(Calendar.MINUTE) + offset);
        break;
    }

    return newCld.getTime();
  }

  public static TimeRange generateTimeRange(Date currentDate, String timeString) {
    if (timeString == null || timeString.isEmpty())
      return null;
    TimeRange timeRange = new TimeRange(null, null);
    switch (timeString) {
      case YESTERDAY:
        timeRange.setStart(roundDate(newDateByOffset(currentDate, DAY, -1), DAY));
        timeRange.setEnd(roundDate(currentDate, DAY));
        break;
      case DAYBEFORE:
        timeRange.setStart(roundDate(newDateByOffset(currentDate, DAY, -2), DAY));
        timeRange.setEnd(roundDate(newDateByOffset(currentDate, DAY, -1), DAY));
        break;
      case LASTWEEK:
        timeRange.setStart(roundDate(newDateByOffset(currentDate, WEEK, -1), WEEK));
        timeRange.setEnd(roundDate(currentDate, WEEK));
        break;
      case LASTMONTH:
        timeRange.setStart(roundDate(newDateByOffset(currentDate, MONTH, -1), MONTH));
        timeRange.setEnd(roundDate(currentDate, MONTH));
        break;
      case LASTYEAR:
        timeRange.setStart(roundDate(newDateByOffset(currentDate, YEAR, -1), YEAR));
        timeRange.setEnd(roundDate(currentDate, YEAR));
        break;
      case TODAY:
        timeRange.setStart(roundDate(currentDate, DAY));
        timeRange.setEnd(currentDate);
        break;
      case THISWEEK:
        timeRange.setStart(roundDate(currentDate, WEEK));
        timeRange.setEnd(currentDate);
        break;
      case THISMONTH:
        timeRange.setStart(roundDate(currentDate, MONTH));
        timeRange.setEnd(currentDate);
        break;
      case THISYEAR:
        timeRange.setStart(roundDate(currentDate, YEAR));
        timeRange.setEnd(currentDate);
        break;
      default:
        return null;
    }
    return timeRange;
  }

  public static TimeRange generateTimeRange(Date date, int offset, String granularity) {
    Date start = newDateByOffset(date, granularity, offset);
    return new TimeRange(start, date);
  }

  public static Date parseDateString(String dateStr) throws ParseException {
    return format.parse(dateStr);
  }

}
