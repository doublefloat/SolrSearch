package com.hypers.search.service;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.NestedAggregationReq;
import com.hypers.search.model.request.SolrDocRequest;
import com.hypers.search.model.request.FlatAggregationReq;

/**
 * @author long
 * @since 17-8-10
 */
public interface SolrSearchService {

  ResponseWrapper query(SolrDocRequest request);

  ResponseWrapper aggregateByValueGroup(NestedAggregationReq request);

  ResponseWrapper aggregateByDate(AggregationRequest request);

  ResponseWrapper aggregateByValue(FlatAggregationReq request);

  ResponseWrapper aggregateByGroup(FlatAggregationReq request);
}