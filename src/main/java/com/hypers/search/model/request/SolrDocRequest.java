package com.hypers.search.model.request;

/**
 * @author long
 * @since 2017/9/25
 */
public class SolrDocRequest {

  private String collection;
  private String query;
  private String timeRange;
  private String aggregateField;
  private String filters;
  private String fields;
  private Integer page;
  private Integer pageSize;
  private String order;
  private String direction;
  private Boolean ignoreCase;

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getFilters() {
    return filters;
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

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public void setIgnoreCase(Boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
  }

  public Boolean getIgnoreCase() {
    return ignoreCase;
  }

  public String getAggregateField() {
    return aggregateField;
  }

  public void setAggregateField(String aggregateField) {
    this.aggregateField = aggregateField;
  }

  @Override
  public String toString() {
    return "SolrDocRequest [collection=" + collection + ", query=" + query + ", timeRange="
        + timeRange + ", aggregateField=" + aggregateField + ", filters=" + filters + ", fields="
        + fields + ", page=" + page + ", pageSize=" + pageSize + ", order=" + order + ", direction="
        + direction + ", ignoreCase=" + ignoreCase + "]";
  }

}
