package com.hypers.search.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author long
 * @since 17-12-20
 */
@ConfigurationProperties(prefix = "collection")
public class FieldFilterProperties {

  private Map<String, String> pairs = new HashMap<>();

  public Map<String, String> getFilteredItems() {
    return this.pairs;
  }

  public void setFilteredItems(Map<String, String> filteredFields) {
    this.pairs = pairs;
  }
}
