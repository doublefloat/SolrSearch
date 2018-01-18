package com.hypers.search.model.request;

import com.hypers.search.enums.SolrFieldCategory;

/**
 * @author long
 * @since 17-11-15
 */
public class NestedAggregationReq extends FlatAggregationReq {

  private String innerStart;
  private String innerEnd;
  private String innerGap;
  private String innerField;
  private SolrFieldCategory innerFieldCategory;

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

  public void setInnerStart(String innerStart) {
    this.innerStart = innerStart;
  }

  public String getInnerStart() {
    return innerStart;
  }

  public void setInnerField(String innerField) {
    this.innerField = innerField;
  }

  public String getInnerField() {
    return innerField;
  }

  public void setInnerFieldCategory(SolrFieldCategory innerFieldCategory) {
    this.innerFieldCategory = innerFieldCategory;
  }

  public SolrFieldCategory getInnerFieldCategory() {
    return innerFieldCategory;
  }

}
