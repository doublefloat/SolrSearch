package com.hypers.search.model.response;

import com.hypers.search.enums.SolrFieldCategory;

/**
 * @author long
 * @since 17-11-1
 */
public class CategorizedField {
  private String name;
  private SolrFieldCategory category;

  public void setCategory(SolrFieldCategory category) {
    this.category = category;
  }

  public SolrFieldCategory getCategory() {
    return category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
