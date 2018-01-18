package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;

/**
 * @author long
 * @since 17-10-10
 */
public class DynamicFieldFilter {

  private SolrServer solrServer;
  private int rows = 100;

  public DynamicFieldFilter(SolrServer solrServer) {
    this.solrServer = solrServer;
  }

  public void setSolrServer(SolrServer solrServer) {
    this.solrServer = solrServer;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  private Set<String> retrieveDynamicFields(String collection, String dynamicField)
      throws ParameterErrorException {
    List<String> flds = new ArrayList<>();
    flds.add(dynamicField);
    return retrieveDynamicFields(collection, flds);
  }

  public Set<String> retrieveDynamicFields(String collection, List<String> dynamicFields)
      throws ParameterErrorException {
    SolrQuery query = new SolrQuery();
    query.set("collection", collection);
    query.set("q", "*:*");
    query.set("rows", rows);
    query.set("fl", joinFields(dynamicFields));

    return execute(query);
  }

  private String joinFields(List<String> dynamicFields) {
    StringBuilder sb = new StringBuilder();
    for (String field : dynamicFields) {
      sb.append(field);
      sb.append(",");
    }
    sb.deleteCharAt(sb.length() - 1);

    return sb.toString();
  }

  private Set<String> execute(SolrQuery query) throws ParameterErrorException {
    SolrDocumentList list;
    Set<String> flds = new HashSet<>();
    try {
      list = solrServer.query(query).getResults();
    } catch (SolrServerException | SolrException e) {
      throw new ParameterErrorException(e.getMessage(), e);
    }

    for (SolrDocument solrDocument : list) {
      flds.addAll(solrDocument.getFieldNames());
    }
    return flds;
  }

}
