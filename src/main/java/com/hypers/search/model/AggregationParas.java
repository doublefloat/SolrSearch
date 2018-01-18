package com.hypers.search.model;

import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;

/**
 * @author long
 * @since 2017/9/19
 */
public class AggregationParas {

  private String collection;
  private String query;
  private String filters;
  private String outerStart;
  private String outerEnd;
  private String outerGap;
  private String outerField;
  private SolrFieldCategory outerFieldCategory;
  private AggregationType type;
  private Integer outerLimit;

  public void setOuterLimit(Integer outerLimit) {
    this.outerLimit = outerLimit;
  }

  public Integer getOuterLimit() {
    return outerLimit;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getFilters() {
    return filters;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  public void setOuterField(String outerField) {
    this.outerField = outerField;
  }

  public String getOuterField() {
    return outerField;
  }

  public void setOuterGap(String outerGap) {
    this.outerGap = outerGap;
  }

  public String getOuterGap() {
    return outerGap;
  }

  public void setOuterEnd(String outerEnd) {
    this.outerEnd = outerEnd;
  }

  public String getOuterEnd() {
    return outerEnd;
  }

  public void setOuterStart(String outerStart) {
    this.outerStart = outerStart;
  }

  public String getOuterStart() {
    return outerStart;
  }

  public void setOuterFieldCategory(SolrFieldCategory outerFieldCategory) {
    this.outerFieldCategory = outerFieldCategory;
  }

  public SolrFieldCategory getOuterFieldCategory() {
    return outerFieldCategory;
  }

  public void setType(AggregationType type) {
    this.type = type;
  }

  public AggregationType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "AggregationParas [collection=" + collection + ", query=" + query + ", filters="
        + filters + ", outerField=" + outerField + ", outerStart=" + outerStart + ", outerEnd="
        + outerEnd + ", gap=" + outerGap + ", outerFieldCategory=" + outerFieldCategory + "]";
  }

}
