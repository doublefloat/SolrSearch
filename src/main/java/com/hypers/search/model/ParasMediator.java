package com.hypers.search.model;

import com.hypers.search.enums.SolrFieldCategory;

/**
 * @author long
 * @since 17-11-8
 */
public class ParasMediator extends AggregationParas {

  private String innerField;
  private SolrFieldCategory innerFieldCategory;
  private String aggregateField;
  private SolrFieldCategory aggregateFieldCategory;
  private String innerStart;
  private String innerEnd;
  private String innerGap;
  private Integer innerLimit;

  public void setAggregateField(String aggregateField) {
    this.aggregateField = aggregateField;
  }

  public String getAggregateField() {
    return aggregateField;
  }

  public void setAggregateFieldCategory(SolrFieldCategory aggregateFieldCategory) {
    this.aggregateFieldCategory = aggregateFieldCategory;
  }

  public SolrFieldCategory getAggregateFieldCategory() {
    return aggregateFieldCategory;
  }

  public void setInnerFieldCategory(SolrFieldCategory innerFieldCategory) {
    this.innerFieldCategory = innerFieldCategory;
  }

  public SolrFieldCategory getInnerFieldCategory() {
    return innerFieldCategory;
  }

  public String getInnerField() {
    return innerField;
  }

  public void setInnerField(String innerField) {
    this.innerField = innerField;
  }

  public void setInnerStart(String innerStart) {
    this.innerStart = innerStart;
  }

  public String getInnerStart() {
    return innerStart;
  }

  public void setInnerEnd(String innerEnd) {
    this.innerEnd = innerEnd;
  }

  public String getInnerEnd() {
    return innerEnd;
  }

  public void setInnerGap(String innerGap) {
    this.innerGap = innerGap;
  }

  public String getInnerGap() {
    return innerGap;
  }

  public void setInnerLimit(Integer innerLimit) {
    this.innerLimit = innerLimit;
  }

  public Integer getInnerLimit() {
    return innerLimit;
  }
}
