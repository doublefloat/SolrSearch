package com.hypers.search.utils.collection;

import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.Count;
import com.hypers.search.model.ParasMediator;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * @author long
 * @since 17-11-14
 */
public class InnerRangeFacetFinder extends RangeFacetFinder {

  public InnerRangeFacetFinder(CloudSolrServer solrServer) {
    super(solrServer);
  }

  @Override
  protected void assembleDateRangeField(SolrQuery query, AggregationParas paras) {
    ParasMediator mediator = (ParasMediator) paras;
    query.set("facet", "true");
    query.set("facet.date", mediator.getInnerField());
    query.set("facet.date.start", mediator.getInnerStart());
    query.set("facet.date.end", mediator.getInnerEnd());
    query.set("facet.date.gap", mediator.getInnerGap());
  }

  @Override
  protected void assembleNumberRangeField(SolrQuery query, AggregationParas paras) {
    ParasMediator mediator = (ParasMediator) paras;
    query.set("facet", "true");
    query.set("facet.range", mediator.getInnerField());
    query.set("facet.range.start", mediator.getInnerStart());
    query.set("facet.range.end", mediator.getInnerEnd());
    query.set("facet.range.gap", mediator.getInnerGap());
  }

  @Override
  protected SolrQuery assembleSolrQuery(AggregationParas paras) {
    SolrQuery query = new SolrQuery();
    ParasMediator mediator = (ParasMediator) paras;
    switch (mediator.getInnerFieldCategory()) {
      case DATE:
        assembleDateRangeField(query, paras);
        break;
      case NUMBER:
        assembleNumberRangeField(query, paras);
      default:
        //never reach here
    }
    assembleCommonParas(query, paras);
    return query;
  }

  @Override
  protected List<Count> fetchResponse(QueryResponse response, AggregationParas paras) {
    ParasMediator mediator = (ParasMediator) paras;
    if (mediator.getInnerFieldCategory() == SolrFieldCategory.NUMBER) {
      return fetchFacetRanges(response);
    }
    return fetchFacetDate(response, mediator.getInnerField());
  }
}
