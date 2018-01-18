package com.hypers.search.web;

import com.hypers.common.SimpleTransfer;
import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.request.NestedAggregationReq;
import com.hypers.search.model.request.FlatAggregationReq;
import com.hypers.search.service.SolrSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.request.AggregationRequest;

/**
 * @author long
 * @since 2017/9/25
 */
@RestController
@RequestMapping("/solr/search/aggregate")
public class SolrAggregationController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SolrAggregationController.class);

  private SolrSearchService solrSearchService;

  @Autowired
  public void setSolrSearchService(SolrSearchService solrSearchService) {
    this.solrSearchService = solrSearchService;
  }

  @RequestMapping(value = "/valuegroup", method = RequestMethod.GET)
  public ResponseWrapper aggregate(
      @RequestParam String collection,
      @RequestParam String timeRange,
      @RequestParam String aggregateField,
      @RequestParam AggregationType type,
      @RequestParam String outerField,
      @RequestParam SolrFieldCategory outerFieldCategory,
      @RequestParam String innerField,
      @RequestParam SolrFieldCategory innerFieldCategory,
      @RequestParam Integer limit,
      @RequestParam String innerStart,
      @RequestParam String innerEnd,
      @RequestParam(required = false) String innerGap,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String filters) {
    NestedAggregationReq request = new NestedAggregationReq();
    request.setCollection(collection);
    request.setQuery(query);
    request.setFilters(filters);
    request.setOuterField(outerField);
    request.setOuterFieldCategory(outerFieldCategory);
    request.setType(type);
    request.setLimit(limit);
    request.setInnerStart(innerStart);
    request.setInnerEnd(innerEnd);
    request.setInnerGap(innerGap);
    request.setAggregateFiled(aggregateField);
    request.setTimeRange(timeRange);
    request.setInnerField(innerField);
    request.setInnerFieldCategory(innerFieldCategory);

    log.info("Trace: SolrAggregationController | AggregationRequest: " + request.toString());

    return solrSearchService.aggregateByValueGroup(request);
  }

  @RequestMapping(value = "/group", method = RequestMethod.GET)
  public ResponseWrapper aggregateByGroup(
      @RequestParam String collection,
      @RequestParam String aggregateField,
      @RequestParam String timeRange,
      @RequestParam AggregationType type,
      @RequestParam String outerField,
      @RequestParam Integer limit,
      @RequestParam SolrFieldCategory outerFieldCategory,
      @RequestParam String outerStart,
      @RequestParam String outerEnd,
      @RequestParam String outerGap,
      @RequestParam(required = false) String tagField,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String filters) {
    FlatAggregationReq request = new FlatAggregationReq();
    request.setCollection(collection);
    request.setQuery(query);
    request.setFilters(filters);
    request.setAggregateFiled(aggregateField);
    request.setTimeRange(timeRange);
    request.setOuterField(outerField);
    request.setOuterFieldCategory(outerFieldCategory);
    request.setOuterStart(outerStart);
    request.setOuterEnd(outerEnd);
    request.setOuterGap(outerGap);
    request.setLimit(limit);
    request.setType(type);
    request.setTagField(tagField);
    log.info("SolrAggregationController.aggregateByGroup,request={}", SimpleTransfer.toMap(request));
    return solrSearchService.aggregateByGroup(request);
  }

  @RequestMapping(value = "/value", method = RequestMethod.GET)
  public ResponseWrapper aggregateByValue(
      @RequestParam String collection,
      @RequestParam String aggregateField,
      @RequestParam String timeRange,
      @RequestParam AggregationType type,
      @RequestParam String outerField,
      @RequestParam Integer limit,
      @RequestParam SolrFieldCategory outerFieldCategory,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String filters) {
    FlatAggregationReq request = new FlatAggregationReq();
    request.setCollection(collection);
    request.setAggregateFiled(aggregateField);
    request.setTimeRange(timeRange);
    request.setOuterField(outerField);
    request.setLimit(limit);
    request.setOuterFieldCategory(outerFieldCategory);
    request.setType(type);
    request.setQuery(query);
    request.setFilters(filters);
    log.info("SolrAggregationController.aggregateByValue,request={}",
        SimpleTransfer.toMap(request));
    return solrSearchService.aggregateByValue(request);
  }

  @RequestMapping(value = "/date", method = RequestMethod.GET)
  public ResponseWrapper aggregateByDate(
      @RequestParam String collection,
      @RequestParam String aggregateField,
      @RequestParam String timeRange,
      @RequestParam(required = false) AggregationType type,
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String filters,
      @RequestParam(required = false) String granularity) {
    AggregationRequest request = new AggregationRequest();
    request.setCollection(collection);
    request.setAggregateFiled(aggregateField);
    request.setTimeRange(timeRange);
    request.setType(type);
    request.setQuery(query);
    request.setFilters(filters);
    request.setGranularity(granularity);
    log.info("SolrAggregationController.aggregateByValue,request={}",
        SimpleTransfer.toMap(request));
    return solrSearchService.aggregateByDate(request);
  }
}
