package com.hypers.search.model.request;

import com.hypers.search.enums.AggregationType;

/**
 * @author long
 * @since 2017/9/19
 */
public class AggregationRequest {

  private String collection;
  private String query;
  private String timeRange;
  private String filters;
  private String granularity;
  private String aggregateFiled;

  private AggregationType type;

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

  public void setGranularity(String granularity) {
    this.granularity = granularity;
  }

  public String getGranularity() {
    return granularity;
  }

  public void setTimeRange(String timeRange) {
    this.timeRange = timeRange;
  }

  public String getTimeRange() {
    return timeRange;
  }

  public void setAggregateFiled(String aggregateFiled) {
    this.aggregateFiled = aggregateFiled;
  }

  public String getAggregateFiled() {
    return aggregateFiled;
  }

  public void setType(AggregationType type) {
    this.type = type;
  }

  public AggregationType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "AggregationRequest [collection=" + collection + ", query=" + query + ", timeRange="
        + timeRange + ", filters=" + filters + ", granularity=" + granularity + ",aggregateField="
        + aggregateFiled + "]";
  }
}