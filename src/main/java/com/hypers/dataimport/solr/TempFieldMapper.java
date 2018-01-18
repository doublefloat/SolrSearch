package com.hypers.dataimport.solr;

import java.util.HashMap;
import java.util.Map;

/**
 * @author long
 * @since 17-10-11
 */
public class TempFieldMapper implements FieldMapper {

  private static Map<String, String> pairs = new HashMap<>();

  static {
    pairs = new HashMap<>();
    pairs.put("IP", "IP");
    pairs.put("MAC", "MAC");
    pairs.put("线程", "thread");
    pairs.put("通道ID", "channelId");
    pairs.put("事务ID", "transactionId");
    pairs.put("请求", "request");
    pairs.put("营业部", "department");
    pairs.put("耗时A", "costTimeA");
    pairs.put("耗时B", "costTimeB");
    pairs.put("排队", "queue");
    pairs.put("_id","_id");
    pairs.put("message", "_message");
    pairs.put("log_dt", "log_dt");
    pairs.put("type", "type");
  }

  public String mapping(String in) throws NoSuchFieldException {
    String out = pairs.get(in);
    if (out == null) {
      throw new NoSuchFieldException("Undefined field: " + in);
    }
    return out;
  }
}
