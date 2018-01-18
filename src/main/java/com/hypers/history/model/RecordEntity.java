package com.hypers.history.model;

import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.enums.EnumsTimeType;

/**
 * @author long
 * @since 17-9-1
 */
public class RecordEntity {
  private Long id;
  private Long userId;
  private String query;
  private String timeRange;
  private String fields;
  private String filters;
  private Integer page;
  private Integer pageSize;
  private EnumsRecordStatus status;
  private Boolean defaultSearch;
  private String searchName;
  private EnumsTimeType timeType;
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

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public String getTimeRange() {
    return timeRange;
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

  public void setStatus(EnumsRecordStatus status) {
    this.status = status;
  }

  public EnumsRecordStatus getStatus() {
    return status;
  }

  public void setDefaultSearch(Boolean defaultSearch) {
    this.defaultSearch = defaultSearch;
  }

  public Boolean isDefaultSearch() {
    return defaultSearch == null ? false : defaultSearch;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setTimeType(EnumsTimeType timeType) {
    this.timeType = timeType;
  }

  public EnumsTimeType getTimeType() {
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
    return "RecordEntity" + "[" + "id=" + id + "," + "userId=" + userId + "," + "query=" + query
        + "," + "fields=" + fields + "," + "filters=" + filters + "," + "timeRange=" + timeRange
        + "," + "page=" + page + "," + "pageSize=" + pageSize + "," + "status=" + status + ","
        + "defaultSearch=" + defaultSearch + ",aggregateField=" + aggregateField + "]";
  }
}
