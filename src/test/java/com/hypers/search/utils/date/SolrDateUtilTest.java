package com.hypers.search.utils.date;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 2017/9/15
 */
public class SolrDateUtilTest {

  static final long MINUTE = 60 * 1000;
  static final long HOUR = MINUTE * 60;
  public static long dateMilli = 1505444828875L;
  private String localDateString = "2017-09-15T11:07:08.875Z";
  private String utcDateString = "2017-09-15T03:07:08.875Z";
  private Locale locale = Locale.getDefault();
  private TimeZone zone = TimeZone.getDefault();

  @Test
  public void testGetCurrentTime() {
    Date localDate = new Date(dateMilli);
    Date utcDate = SolrDateUtil
        .currentUTCTime(TimeZone.getDefault(), Locale.getDefault(), localDate);
//    System.out.println(localDate.getTime());
//    System.out.println(utcDate.getTime());
    System.out.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tLZ\n", localDate);
    System.out.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tLZ\n", utcDate);
    Assert.assertTrue(utcDate.getTime() + HOUR * 8 == localDate.getTime());
  }

  @Test
  public void testFormatAsUTC() {
    Date localDate = new Date(dateMilli);
    String utcDateString = SolrDateUtil.formatAsUTC(null, null, localDate);
    System.out.println(localDate);
    System.out.println(utcDateString);
    Assert.assertEquals(utcDateString, this.utcDateString);
  }

  @Test
  public void testCurrentLocaleTime() {

    Date date = SolrDateUtil.currentLocalTime(zone, locale,
        SolrDateUtil.currentUTCTime(zone, locale, new Date(dateMilli)));
    System.out.println(date);
    Assert.assertEquals(date.getTime(), dateMilli);
  }

  @Test
  public void testParseAsLocalDate() throws ParseException {
    Date localData = SolrDateUtil.parseAsLocalDate(zone, locale, this.utcDateString);
    Assert.assertEquals(localData, new Date(dateMilli));
  }

  @Test
  public void testEscapeColons() {
    String plain = "name:long&hello:every";
    String expected = "name\\:long&hello\\:every";
    Assert.assertEquals(expected, SolrDateUtil.escapeColons(plain));
  }
}