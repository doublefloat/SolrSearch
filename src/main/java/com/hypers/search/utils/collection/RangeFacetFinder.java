package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.Count;
import com.hypers.search.utils.date.SolrDateUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrException;

/**
 * @author long
 * @since 2017/9/19
 */
public class RangeFacetFinder {

  private CloudSolrServer solrServer;

  public RangeFacetFinder() {
  }

  public RangeFacetFinder(CloudSolrServer solrServer) {
    this.solrServer = solrServer;
  }

  public void setSolrServer(CloudSolrServer solrServer) {
    this.solrServer = solrServer;
  }

  protected void assembleDateRangeField(SolrQuery query, AggregationParas paras) {
    query.set("facet", "true");
    query.set("facet.date", paras.getOuterField());
    query.set("facet.date.start", paras.getOuterStart());
    query.set("facet.date.end", paras.getOuterEnd());
    query.set("facet.date.gap", paras.getOuterGap());
  }

  protected SolrQuery assembleSolrQuery(AggregationParas paras) {
    SolrQuery query = new SolrQuery();
    //TODO handle different aggregate type
    if (paras.getOuterFieldCategory() == SolrFieldCategory.DATE) {
      assembleDateRangeField(query, paras);
    } else if (paras.getOuterFieldCategory() == SolrFieldCategory.NUMBER) {
      assembleNumberRangeField(query, paras);
    }
    assembleCommonParas(query, paras);
    return query;
  }

  protected void assembleCommonParas(SolrQuery query, AggregationParas paras) {
    query.set("collection", paras.getCollection());
    query.set("rows", "0");
    query.set("q", paras.getQuery());
    query.set("fq", paras.getFilters());
  }

  private QueryResponse execute(SolrQuery query) throws SolrServerException {
    return solrServer.query(query);
  }

  protected List<Count> fetchFacetRanges(QueryResponse response) {
    List<Count> result;

    if (response.getFacetRanges().size() == 0) {
      return Collections.emptyList();
    }
    RangeFacet rangeFacet = response.getFacetRanges().get(0);
    result = new ArrayList<>(rangeFacet.getCounts().size());
    List<RangeFacet.Count> list = rangeFacet.getCounts();
    for (RangeFacet.Count count : list) {
      Count c = new Count();
      c.setName(count.getValue());
      c.setCount((long) count.getCount());
      result.add(c);
    }
    result.add(new Count(rangeFacet.getEnd().toString(), -1l));
    return result;
  }

  protected List<Count> fetchFacetDate(QueryResponse response, String field) {
    FacetField facetField = response.getFacetDate(field);
    List<Count> result = new ArrayList<>(facetField.getValueCount());
    for (FacetField.Count count : facetField.getValues()) {
      Count c = new Count();
      c.setCount(count.getCount());
      try {
        c.setName(Long.toString(SolrDateUtil
            .parseAsLocalDate(null, null, count.getName()).getTime()));
      } catch (ParseException e) {
      }
      result.add(c);
    }

    Count c = new Count();
    c.setName(Long.toString(SolrDateUtil
        .currentLocalTime(TimeZone.getDefault(), Locale.getDefault(), facetField.getEnd())
        .getTime()));
    c.setCount(-1l);
    result.add(c);

    return result;
  }

  protected List<Count> fetchResponse(QueryResponse response, AggregationParas paras) {
    if (paras.getOuterFieldCategory() == SolrFieldCategory.NUMBER) {
      return fetchFacetRanges(response);
    }
    //handle SolrFieldCategory.DATE
    return fetchFacetDate(response, paras.getOuterField());
  }

  protected void assembleNumberRangeField(SolrQuery query, AggregationParas paras) {
    query.set("facet","true");
    query.set("facet.range", paras.getOuterField());
    query.set("facet.range.start", paras.getOuterStart());
    query.set("facet.range.end", paras.getOuterEnd());
    query.set("facet.range.gap", paras.getOuterGap());
  }

  public List<Count> aggregate(AggregationParas paras) throws ParameterErrorException {
    QueryResponse response = null;
    try {
      response = execute(assembleSolrQuery(paras));
    } catch (SolrServerException e) {
      throw new ParameterErrorException(e.getMessage(), e);
    } catch (SolrException e) {
      throw new ParameterErrorException(e.getMessage(), e);
    }
    return fetchResponse(response, paras);
  }
}
