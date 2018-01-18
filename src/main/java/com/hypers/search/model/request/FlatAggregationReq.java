package com.hypers.search.model.request;

import com.hypers.search.enums.SolrFieldCategory;

/**
 * @author long
 * @since 17-11-15
 */
public class FlatAggregationReq extends AggregationRequest {

  private Integer limit;
  private String outerField;
  private SolrFieldCategory outerFieldCategory;
  private String outerStart;
  private String outerEnd;
  private String outerGap;
  private String tagField;

  public void setOuterField(String outerField) {
    this.outerField = outerField;
  }

  public String getOuterField() {
    return outerField;
  }

  public void setOuterFieldCategory(SolrFieldCategory outerFieldCategory) {
    this.outerFieldCategory = outerFieldCategory;
  }

  public SolrFieldCategory getOuterFieldCategory() {
    return outerFieldCategory;
  }


  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setOuterEnd(String outerEnd) {
    this.outerEnd = outerEnd;
  }

  public String getOuterEnd() {
    return outerEnd;
  }

  public void setOuterGap(String outerGap) {
    this.outerGap = outerGap;
  }

  public String getOuterGap() {
    return outerGap;
  }

  public void setOuterStart(String outerStart) {
    this.outerStart = outerStart;
  }

  public String getOuterStart() {
    return outerStart;
  }

  public void setTagField(String tagField) {
    this.tagField = tagField;
  }

  public String getTagField() {
    return tagField;
  }
}
