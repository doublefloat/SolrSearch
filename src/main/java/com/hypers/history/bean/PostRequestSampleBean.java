package com.hypers.history.bean;

/**
 * @author Ivan
 *
 */
public class PostRequestSampleBean {

  private long userId;
  private String query;
  private String timeRange;
  private String filters;
  private String fields;
  private int page;
  private int pageSize;
  private boolean defaultSearch;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public String getFilters() {
    return filters;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getFields() {
    return fields;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public boolean isDefaultSearch() {
    return defaultSearch;
  }

  public void setDefaultSearch(boolean defaultSearch) {
    this.defaultSearch = defaultSearch;
  }

  @Override
  public String toString() {
    return "PostRequestSampleBean [userId=" + userId + ", query=" + query + ", timeRange="
        + timeRange + ", filters=" + filters + ", fields=" + fields + ", page=" + page
        + ", pageSize=" + pageSize + ", defaultSearch=" + defaultSearch + "]";
  }

}