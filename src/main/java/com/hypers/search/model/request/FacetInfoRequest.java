package com.hypers.search.model.request;

/**
 * @author long
 * @since 17-11-7
 */
public class FacetInfoRequest {

  private String collection;
  private String field;
  private Integer count;
  private Integer limit;
  private Boolean asc;
  private String sortType;

  public void setField(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  public void setAsc(Boolean asc) {
    this.asc = asc;
  }

  public Boolean getAsc() {
    return asc;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  public String getSortType() {
    return sortType;
  }

  public String toString() {
    return "FacetInfoRequest:[collection:" + collection + ",field:" + field + ",count:" + count
        + ",sortType:" + sortType + ",limit:" + limit + ",asc:" + asc + "]";
  }
}
