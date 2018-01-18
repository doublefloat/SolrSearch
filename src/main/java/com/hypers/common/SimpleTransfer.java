package com.hypers.common;

import com.alibaba.fastjson.JSON;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author long
 * @since 17-9-1
 */
public class SimpleTransfer {

  public static <T> Map<String, Object> toMap(T t) {
    return JSON.toJavaObject((JSON) JSON.toJSON(t), Map.class);
  }

  public static Map<String, Object> toMap(Object key, Object value, Object... paras) {
    assert paras.length % 2 == 0;
    assert key instanceof String;

    Map<String, Object> map = new LinkedHashMap<>();
    map.put((String) key, value);

    for (int i = 1; i < paras.length; i++)
      map.put(String.valueOf(paras[i - 1]), paras[i]);

    return map;
  }

}