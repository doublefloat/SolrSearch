package com.hypers.search.web;

import com.hypers.search.service.SolrSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.request.SolrDocRequest;

/**
 * @author long
 * @since 17-8-10
 */
@RestController
public class SolrSearchController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SolrSearchController.class);

  private SolrSearchService solrSearchServiceV2;

  @Autowired
  public void setSolrSearchServiceV2(SolrSearchService solrSearchServiceV2) {
    this.solrSearchServiceV2 = solrSearchServiceV2;
  }

  @RequestMapping("/solr/search/request")
  public ResponseWrapper search(@RequestParam String collection,
      @RequestParam(required = false, defaultValue = "*:*") String query,
      @RequestParam String timeRange,
      @RequestParam String aggregateField,
      @RequestParam(required = false) String orderColumn,
      @RequestParam(required = false) String orderType,
      @RequestParam(required = false) String filters,
      @RequestParam(required = false) String fields,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "30") Integer pageSize) {

    SolrDocRequest request = new SolrDocRequest();
    request.setCollection(collection);
    request.setQuery(query);
    request.setAggregateField(aggregateField);
    request.setTimeRange(timeRange);
    request.setFilters(filters);
    request.setFields(fields);
    request.setPage(page);
    request.setPageSize(pageSize);
    request.setDirection(orderType);
    request.setOrder(orderColumn);

    log.info("Trace: SolrSearchController; SolrDocRequest: " + request);

    return solrSearchServiceV2.query(request);
  }

}
