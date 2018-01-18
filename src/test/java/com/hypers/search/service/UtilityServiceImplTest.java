package com.hypers.search.service;

import com.hypers.common.wrapper.ResultWrapper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-11-22
 */
public class UtilityServiceImplTest {
  private static UtilityService utilityService=new UtilityServiceImpl();

  @Test
  public void testCaculateGap() {
    String start = "2017-05-04 12:23:00";
    String end = "2018-12-02 23:00:32";
    ResultWrapper resultWrapper = (ResultWrapper) utilityService.calculateGap(start, end);
    Assert.assertNotNull(resultWrapper.getResult().getItems());
  }

}
