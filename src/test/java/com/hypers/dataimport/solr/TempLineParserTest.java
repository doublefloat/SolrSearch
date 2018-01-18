package com.hypers.dataimport.solr;

import java.text.ParseException;
import org.junit.Test;

/**
 * @author long
 * @since 17-10-12
 */
public class TempLineParserTest {

  private LineParser parser = DataProvider.lineParser();

//  @Test
  public void testParseLine() throws ParseException {
    String line = "192.168.125.1_20160601_00:00:06.467 成功处理 IP:10.0.30.20 MAC:00247E0F4D35 线程:00000E7C 通道ID:56374 事务ID:2501173 请求:(0-100)客户校验(*) 营业部:(0654)上海中兴路营业部 耗时A:5 耗时B:0 排队:2\u00010|654,65400053,陆静辉,|1|||\u000165400053|陆静辉|WSWT|654|0|1|||||1,2,|0|||光大证券 Mar 17 2016 18:40:15|";
    System.out.println(parser.parseLine(line));
  }
}
