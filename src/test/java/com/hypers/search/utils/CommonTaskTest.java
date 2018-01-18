package com.hypers.search.utils;

import com.hypers.search.utils.CommonTask;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-10-24
 */
public class CommonTaskTest {
  @Test
  public void testEscape() {
    StringBuilder str = new StringBuilder("f1:v2:hello:test");
    String expected= CommonTask.escape(str, ':', 4, str.length());
    Assert.assertEquals("f1:v2\\:hello\\:test",expected);
  }

  @Test
  public void testEscape2() {
    StringBuilder str = new StringBuilder("f1\\:v2\\:hello:test");
    CommonTask.escape(str, ':', 1, str.length());
    System.out.println(str);
    Assert.assertEquals(str.toString(),"f1\\:v2\\:hello\\:test");
  }

}
