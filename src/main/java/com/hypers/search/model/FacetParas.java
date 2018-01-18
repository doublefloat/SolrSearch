package com.hypers.search.model;

/**
 * @author long
 * @since 17-10-31
 */
public class FacetParas {

  private String collection;
  private String field;
  private String query;
  private String filter;
  private String sort;
  private int count;
  private int limit;

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getSort() {
    return sort;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getLimit() {
    return limit;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public String getFilter() {
    return filter;
  }
}
