package com.hypers.search.utils.date;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.MILLISECOND;


/**
 * @author long
 * @since 2017/9/15
 */
public class TimeTransferTest {

  private static Date testDate = new Date(SolrDateUtilTest.dateMilli);
  private static Calendar testCld = Calendar.getInstance();

  static {
    testCld.setTime(testDate);
  }

  @Test
  public void testNewDateByOffset() {
    Date newDate = TimeTransfer.newDateByOffset(testDate, "year", -1);
    Calendar newCld = Calendar.getInstance();
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(YEAR) + 1, testCld.get(YEAR));

    newDate = TimeTransfer.newDateByOffset(testDate, "month", 2);
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(MONTH), testCld.get(MONTH) + 2);

    newDate = TimeTransfer.newDateByOffset(testDate, "day", 2);
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(DAY_OF_MONTH), testCld.get(DAY_OF_MONTH) + 2);

    newDate = TimeTransfer.newDateByOffset(testDate, "hour", 2);
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(HOUR_OF_DAY), testCld.get(HOUR_OF_DAY) + 2);

    newDate = TimeTransfer.newDateByOffset(testDate, "min", 2);
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(MINUTE), testCld.get(MINUTE) + 2);

    newDate = TimeTransfer.newDateByOffset(testDate, "week", 2);
    newCld.setTime(newDate);
    Assert.assertEquals(newCld.get(WEEK_OF_YEAR), testCld.get(WEEK_OF_YEAR) + 2);
  }

  public void assertDateEquals(Calendar newCld) {
    Assert.assertEquals(newCld.get(MONTH), testCld.get(MONTH));
    Assert.assertEquals(newCld.get(DAY_OF_MONTH), testCld.get(DAY_OF_MONTH));
    Assert.assertEquals(newCld.get(HOUR_OF_DAY), testCld.get(HOUR_OF_DAY));
    Assert.assertEquals(newCld.get(MINUTE), testCld.get(MINUTE));
    Assert.assertEquals(newCld.get(SECOND), testCld.get(SECOND));
    Assert.assertEquals(newCld.get(MILLISECOND), testCld.get(MILLISECOND));
  }

  @Test
  public void testParseDateString() throws ParseException{
    String dateString = "2017-05-04 12:00:00.000";
    Date date = TimeTransfer.parseDateString(dateString);
    String expected = "Thu May 04 12:00:00 CST 2017";

    System.out.println(date);
    System.out.println(expected);
    Assert.assertEquals(date.toString(), expected);

  }
}
