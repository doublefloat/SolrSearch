package com.hypers.search.utils.date;

import com.hypers.common.exception.FormatErrorException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-11-14
 */
public class DateGapMapperTest {

  @Test
  public void testCalculateGap() throws FormatErrorException{
    String granularity = "11 min";
    String expected = "+11MINUTES";
    Assert.assertEquals(DateGapMapper.toGapOfSolrDate(granularity), expected);
    Assert.assertEquals(DateGapMapper.toGapOfSolrDate("11 second"), "+11SECONDS");
    Assert.assertEquals(DateGapMapper.toGapOfSolrDate("11 hour"), "+11HOURS");
    Assert.assertEquals(DateGapMapper.toGapOfSolrDate("11 day"), "+11DAYS");
    Assert.assertEquals(DateGapMapper.toGapOfSolrDate("11 year"), "+11YEARS");
    try {
      DateGapMapper.toGapOfSolrDate("11 error");
    } catch (FormatErrorException e) {
      Assert.assertEquals(e.getMessage(), "can't find date unit 'error'");
    }
  }

  @Test
  public void testMappingToGranularity(){
    System.out.println(DateGapMapper.toGranularity(60*1000L));
    System.out.println(DateGapMapper.toGranularity(60*60*1000L));
    System.out.println(DateGapMapper.toGranularity(24*60*60*1000L));
    System.out.println(DateGapMapper.toGranularity(365*24*60*60*1000L));

    System.out.println(DateGapMapper.toGranularity(8*365*24*60*60*1000L));
  }
}
