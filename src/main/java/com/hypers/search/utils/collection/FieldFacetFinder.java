package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.model.FacetParas;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;

/**
 * @author long
 * @since 17-8-23
 */
public class FieldFacetFinder {

  private CloudSolrServer server;

  public FieldFacetFinder(CloudSolrServer server) {
    this.server = server;
  }

  public FieldFacetFinder(String zkHost) throws MalformedURLException {
    this.server = new CloudSolrServer(zkHost);
  }

  SolrQuery assembleQuery(FacetParas paras) {
    SolrQuery query = new SolrQuery();
    query.set("collection", paras.getCollection());
    if(paras.getQuery()!=null){
      query.set("q",paras.getQuery());
    }else{
      query.set("q", "*:*");
    }

    if(paras.getFilter()!=null){
      query.set("fq",paras.getFilter());
    }

    query.set("facet", "true");
    query.set("facet.field", paras.getField());
    query.set("facet.limit", paras.getLimit());
    query.set("facet.mincount", paras.getCount());
    query.set("facet.sort", paras.getSort());
    query.set("rows", "0");

    return query;
  }

  public SolrServer getSolrServer(String collection) {
    // TODO should balance the request
    server.setDefaultCollection(collection);
    return server;
  }

  public QueryResponse execute(SolrServer server, SolrQuery query) throws ParameterErrorException {
    try {
      return server.query(query);
    } catch (SolrServerException e) {
      throw new ParameterErrorException(e);
    } catch (SolrException e) {
      throw new ParameterErrorException(e);
    }
  }

  public Map<String, Long> resolveResponse(String field, QueryResponse response) {
    FacetField facetField = response.getFacetField(field);
    Map<String, Long> result = new LinkedHashMap<>(facetField.getValueCount());

    for (Count count : facetField.getValues()) {
      result.put(count.getName(), count.getCount());
    }

    return result;
  }

  public Map<String, Long> getFacedFields(FacetParas paras)
      throws ParameterErrorException {
    SolrQuery query = assembleQuery(paras);
    SolrServer server = getSolrServer(paras.getCollection());

    return resolveResponse(paras.getField(), execute(server, query));
  }
}
