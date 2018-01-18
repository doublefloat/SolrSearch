package com.hypers.search.utils.date;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author long
 * @since 2017/9/14
 */
public class SolrDateUtil {

  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final DateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
  public static final int DATE_STRING_MAX_LENGTH = 24;
  public static final int DATE_RANGE_STRING_MAX_LENGTH=54;

  public static Date currentUTCTime(TimeZone zone, Locale locale, Date date) {
    assert zone != null;
    assert locale != null;

    Calendar calendar = Calendar.getInstance(zone, locale);
    calendar.setTime(date);
    int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
    int dstOffset = calendar.get(Calendar.DST_OFFSET);
    calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

    return calendar.getTime();
  }

  public static String formatAsUTC(TimeZone zone, Locale locale, Date date) {
    if (zone == null) {
      zone = TimeZone.getDefault();
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }
    Date utcDate = currentUTCTime(zone, locale, date);

    StringBuffer sb = new StringBuffer(DATE_STRING_MAX_LENGTH);
    FORMATTER.format(utcDate, sb, new FieldPosition(0));

    return sb.toString();
  }

  public static String formatAsUTC(Date date) {
    return formatAsUTC(null, null, date);
  }

  public static Date currentLocalTime(TimeZone zone, Locale locale, Date utcDate) {
    assert zone != null;
    assert locale != null;

    Calendar calendar = Calendar.getInstance(zone, locale);
    calendar.setTime(utcDate);
    int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
    int dstOffset = calendar.get(Calendar.DST_OFFSET);
    calendar.add(Calendar.MILLISECOND, zoneOffset + dstOffset);

    return calendar.getTime();
  }

  public static Date parseAsLocalDate(TimeZone zone, Locale locale, String utcString)
      throws ParseException {
    if (zone == null) {
      zone = TimeZone.getDefault();
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }

    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

    return currentLocalTime(zone, locale, format.parse(utcString));
  }

  public static String escapeColons(CharSequence plain) {
    StringBuilder sb;
    if (plain instanceof StringBuilder) {
      sb = (StringBuilder) plain;
    } else {
      sb = new StringBuilder(plain);
    }

    for (int i = 0; i < sb.length(); i++) {
      if (sb.charAt(i) == ':') {
        sb.insert(i, '\\');
        i++;
      }
    }
    return sb.toString();
  }

  public static String solrTimeRangeString(TimeRange timeRange) {
    StringBuilder sb = new StringBuilder(DATE_RANGE_STRING_MAX_LENGTH);
    sb.append("[");
    sb.append(formatAsUTC(timeRange.getStart()));
    sb.append(" TO ");
    sb.append(formatAsUTC(timeRange.getEnd()));
    sb.append("]");
    return sb.toString();
  }
}

