package com.hypers.search.utils;

import com.hypers.common.exception.ParameterErrorException;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-11-6
 */
public class FilterResolverTest {

  private static FilterResolver resolver = new FilterResolver();

  @Test
  public void testResolve() throws ParameterErrorException {
    String filters = "f1:(v2)";
    String expected = "f1:(\"(v2)\")";
    List<String> expectedList = new ArrayList<>();
    expectedList.add(expected);
    Assert.assertEquals(resolver.resolve(filters), expectedList);
  }

  @Test
  public void testResolve2() throws ParameterErrorException {
    String filters = "f1:(v2) ,:hello;f2:\"hello\"";
    List expectedList = new ArrayList<>();
    expectedList.add("f1:(\\(v2\\)\\  OR \\:hello)");
    expectedList.add("f2:(\"hello\")");
    Assert.assertEquals(resolver.resolve(filters), expectedList);
  }


  @Test
  public void testResolve3() throws ParameterErrorException{
    String str="type:系统信息 (jsdgp1.dll)\"刷新退市股票信息\"线程成功退出!";
    List<String> expected=new ArrayList<>();
    expected.add("type:(系统信息\\ \\(jsdgp1.dll\\)\\\"刷新退市股票信息\\\"线程成功退出!)");
    Assert.assertEquals(resolver.resolve(str), expected);
  }
}
