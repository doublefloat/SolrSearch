package com.hypers.search.service;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.search.model.request.NestedAggregationReq;
import com.hypers.search.model.request.FlatAggregationReq;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypers.common.SimpleTransfer;
import com.hypers.common.StatusCode;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.SolrDocRequest;

/**
 * @author Ivan
 *
 */
@Service("solrSearchServiceV2")
public class SolrSearchServiceImplV2 extends AbstractSolrSearchServiceSkeleton {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SolrSearchServiceImplV2.class);

  private CloudSolrServer solrServer;

  @Autowired
  public void setSolrServer(CloudSolrServer solrServer) {
    this.solrServer = solrServer;
  }

  private SolrQuery queryBuilder(String query) {
    return new SolrQuery(query);
  }

  private SolrQuery filterBuilder(String filters, String timeRange, String aggregateField,
      SolrQuery queryBuilder) throws ParameterErrorException {

    List<String> fqs = resolveFilters(filters);
    if (StringUtils.isNotBlank(timeRange) && StringUtils.isNotBlank(aggregateField)) {
      fqs.add(aggregateField + ":" + resolveTimeRange(timeRange));
    }

    queryBuilder.addFilterQuery(fqs.toArray(new String[fqs.size()]));
    return queryBuilder;

  }

  private SolrQuery paginationBuilder(int page, int pageSize, SolrQuery queryBuilder) {

    page = page == 0 ? 1 : page;
    queryBuilder.setStart((page - 1) * pageSize);
    queryBuilder.setRows(pageSize);
    return queryBuilder;

  }

  private SolrQuery sortBuilder(String order, String direction, SolrQuery queryBuilder) {

    if (StringUtils.isNotBlank(order)) {
      queryBuilder.addSort(order, ORDER.valueOf(direction));
    }

    return queryBuilder;
  }

  private List<Map<String, Object>> responseBuilder(SolrDocumentList docList) {

    List<Map<String, Object>> documentList = new LinkedList<Map<String, Object>>();
    for (SolrDocument doc : docList) {
      Map<String, Object> documentMap = new HashMap<String, Object>();
      for (String fieldName : doc.getFieldNames()) {
        Object fieldValue = doc.getFieldValue(fieldName);
        documentMap.put(fieldName, fieldValue);
      }
      documentList.add(documentMap);
    }

    return documentList;

  }

  @Override
  public ResponseWrapper query(SolrDocRequest request) {

    log.info("Trace: SolrSearchServiceImplV2.findWithFieldsAndFilters() | " + request.toString());

    implementParameters(request);
    solrServer.setDefaultCollection(request.getCollection());

    SolrQuery queryBuilder = queryBuilder(request.getQuery());
    paginationBuilder(request.getPage(), request.getPageSize(), queryBuilder);
    sortBuilder(request.getOrder(), request.getDirection(), queryBuilder);

    QueryResponse response;
    try {
      filterBuilder(request.getFilters(), request.getTimeRange(), request.getAggregateField(),
          queryBuilder);
      response = solrServer.query(queryBuilder);
    } catch (SolrException | SolrServerException | ParameterErrorException e) {
      log.error("SolrException: Solr error: " + e.getMessage());
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(),
          SimpleTransfer.toMap(request));
    }
    SolrDocumentList docList = response.getResults();
    if (docList == null) {
      return new ResultWrapper().wrapResult(StatusCode.SUCCESS, Collections.EMPTY_LIST,
          SimpleTransfer.toMap(request));
    }
    Map<String, Object> supplies = new HashMap<String, Object>();
    supplies.put(RESPONSE_KEY_TOTAL, response.getResults().getNumFound());

    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, responseBuilder(docList),
        SimpleTransfer.toMap(request), supplies);

  }

  @Override
  public ResponseWrapper aggregateByValueGroup(NestedAggregationReq request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseWrapper aggregateByDate(AggregationRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseWrapper aggregateByValue(FlatAggregationReq request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseWrapper aggregateByGroup(FlatAggregationReq request) {
    throw new UnsupportedOperationException();
  }
}
