package com.hypers.search.utils.date;

import com.hypers.common.exception.FormatErrorException;

/**
 * @author long
 * @since 17-11-14
 */
public class DateGapMapper {

  public static final int NUM_OF_SEGMENT = 10;

  public static String toGranularity(long timeInMilli) {
    float width = timeInMilli / NUM_OF_SEGMENT;
    String unit;
    int coefficient;

    if (width >= TimeTransfer.YEAR_MILI_365) {
      unit = TimeTransfer.YEAR;
      coefficient = new Float(Math.ceil(width / TimeTransfer.YEAR_MILI_365)).intValue();
    } else if (width >= TimeTransfer.MONTH_MILI_30) {
      unit = TimeTransfer.MONTH;
      coefficient = new Float(Math.ceil(width / TimeTransfer.MONTH_MILI_30)).intValue();
    } else if (width >= TimeTransfer.DAY_MILI) {
      unit = TimeTransfer.DAY;
      coefficient = new Float(Math.ceil(width / TimeTransfer.DAY_MILI)).intValue();
    } else if (width >= TimeTransfer.HOUR_MILI) {
      unit = TimeTransfer.HOUR;
      coefficient = new Float(Math.ceil(width / TimeTransfer.HOUR_MILI)).intValue();
    } else if (width >= TimeTransfer.MINUTE_MILI) {
      unit = TimeTransfer.MINUTE;
      coefficient = new Float(Math.ceil(width / TimeTransfer.MINUTE_MILI)).intValue();
    } else {
      unit = TimeTransfer.SECOND;
      coefficient = new Float(Math.ceil(width / TimeTransfer.SECOND_MILI)).intValue();
    }

    return coefficient + " " + unit;
  }

  public static long toMilliSeconds(String granularity) throws FormatErrorException {
    String unit;
    int coefficient;
    long gap;
    String[] pieces = granularity.split(" ");
    try {
      coefficient = Integer.valueOf(pieces[0]);
      unit=pieces[1];
    } catch (NumberFormatException e) {
      throw new FormatErrorException(e.getMessage(), e);
    }

    if (unit.equals(TimeTransfer.WEEK)) {
      coefficient *= 7;
      unit = TimeTransfer.DAY;
    }

    unit = pieces[1];
    switch (unit) {
      case TimeTransfer.SECOND:
        gap = TimeTransfer.SECOND_MILI * coefficient;
        break;
      case TimeTransfer.MINUTE:
        gap = TimeTransfer.MINUTE_MILI * coefficient;
        break;
      case TimeTransfer.HOUR:
        gap = TimeTransfer.HOUR_MILI * coefficient;
        break;
      case TimeTransfer.DAY:
        gap = TimeTransfer.DAY_MILI * coefficient;
        break;
      case TimeTransfer.MONTH:
        gap = TimeTransfer.MONTH_MILI_30 * coefficient;
        break;
      case TimeTransfer.YEAR:
        gap = TimeTransfer.YEAR_MILI_365 * coefficient;
        break;
      default:
        throw new FormatErrorException("unit is wrong");
    }
    return gap;
  }

  public static String toGapOfSolrDate(String granularity) throws FormatErrorException {
    int pivot = granularity.indexOf(' ');
    int coefficient;
    String unit;

    try {
      coefficient = Integer.valueOf(granularity.substring(0, pivot));
      unit = granularity.substring(pivot + 1, granularity.length());
    } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
      throw new FormatErrorException("can't parse '" + granularity + "'");
    }

    if (unit.equals(TimeTransfer.WEEK)) {
      coefficient *= 7;
      unit = TimeTransfer.DAY;
    }

    StringBuilder gap = new StringBuilder();
    gap.append("+");
    gap.append(coefficient);
    switch (unit) {
      case TimeTransfer.SECOND:
        gap.append(SolrDateUnit.SECONDS);
        break;
      case TimeTransfer.MINUTE:
        gap.append(SolrDateUnit.MINUTES);
        break;
      case TimeTransfer.HOUR:
        gap.append(SolrDateUnit.HOURS);
        break;
      case TimeTransfer.DAY:
        gap.append(SolrDateUnit.DAYS);
        break;
      case TimeTransfer.MONTH:
        gap.append(SolrDateUnit.MONTHS);
        break;
      case TimeTransfer.YEAR:
        gap.append(SolrDateUnit.YEARS);
        break;
      default:
        throw new FormatErrorException("can't find date unit '" + unit + "'");
    }
    return gap.toString();
  }

  /**
   * @param userTimeString 15min: 3 min, 30min: 5 min, 1h: 10 min, 12h: 1 h, 24h: 1 h, 1 week: 1 day
   * 1 month: 5 days, 1 year: 1 month
   */
  public static String mappingToGranularity(String userTimeString) {
    if (userTimeString == null) {
      return "3 min";
    }

    switch (userTimeString) {
      case TimeTransfer.YESTERDAY:
      case TimeTransfer.DAYBEFORE:
      case TimeTransfer.TODAY:
        return "1 hour";
      case TimeTransfer.LASTWEEK:
      case TimeTransfer.THISWEEK:
        return "1 day";
      case TimeTransfer.LASTMONTH:
      case TimeTransfer.THISMONTH:
        return "5 day";
      case TimeTransfer.THISYEAR:
      case TimeTransfer.LASTYEAR:
        return "1 month";
      default:
        return null;
    }
  }
}
