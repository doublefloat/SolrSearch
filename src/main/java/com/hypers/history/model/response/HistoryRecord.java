package com.hypers.history.model.response;

/**
 * @author Long
 * @since 17-8-31
 */
public class HistoryRecord {
  private Long id;
  private String query;
  private String searchName;
  private String fields;
  private String filters;
  private String timeRange;
  private String timeType;
  private Integer page;
  private Integer pageSize;
  private Boolean defaultSearch;
  private String collection;
  private String aggregateField;

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public String getFields() {
    return fields;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getFilters() {
    return filters;
  }

  public void setDefaultSearch(Boolean defaultSearch) {
    this.defaultSearch = defaultSearch;
  }

  public Boolean isDefaultSearch() {
    return defaultSearch;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getPage() {
    return page;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public String getTimeRange() {
    return timeRange;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setTimeType(String timeType) {
    this.timeType = timeType;
  }

  public String getTimeType() {
    return timeType;
  }

  public void setAggregateField(String aggregateField) {
    this.aggregateField = aggregateField;
  }

  public String getAggregateField() {
    return aggregateField;
  }

  @Override
  public String toString() {
    return "HistoryRecord" + "[" + "id=" + id + "," + "query=" + query + "," + "fields=" + fields
        + "," + "filters=" + filters + "," + "timeRange=" + timeRange + "," + "page=" + page + ","
        + "pageSize=" + pageSize + "," + "defaultSearch=" + defaultSearch + "aggregateField="
        + aggregateField + "]";
  }
}
