package com.hypers.search.utils;

import com.hypers.search.model.Count;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author long
 * @since 17-11-7
 */
public class CountListMapper implements Mapper<Map<String, Long>, List<Count>> {

  @Override
  public List<Count> mapping(Map<String, Long> pairs) {
    List<Count> countList = new ArrayList<>(pairs.size());
    for (String key : pairs.keySet()) {
      countList.add(new Count(key, pairs.get(key)));
    }
    return countList;
  }
}
