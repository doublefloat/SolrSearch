package com.hypers.search;

import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.FacetParas;
import com.hypers.search.model.ParasMediator;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.FacetInfoRequest;
import com.hypers.search.model.request.NestedAggregationReq;
import com.hypers.search.model.request.SolrDocRequest;
import com.hypers.search.utils.date.TimeRange;
import com.hypers.search.utils.date.TimeTransfer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

/**
 * @author long
 * @since 17-10-24
 */
public class CommonData {
  public static String zkHost(){
//    return "10.16.1.59:2121";
    return "localhost:11030";
  }

  public static CloudSolrServer getSolrServer() throws MalformedURLException {
    return new CloudSolrServer(zkHost());
  }

  public static Set<String> collections() {
    Set<String> cls = new HashSet<>();
    cls.add("log-production");
    return cls;
  }

  public static Set<String> activeCollections() {
    Set<String> cls = new HashSet<>();
    cls.add("log-production");
    return cls;
  }

  public static String collection() {
    return "log-production";
  }

  public static List<String> dynamicFields() {
    List<String> dfs = new ArrayList<>();
    dfs.add("*_dt");
    return dfs;
  }

  public static String aggregateField() {
    return "log_dt";
  }

  public static List<String> fields() {
    List<String> flds = new ArrayList<>();
    flds.add("MAC");
    flds.add("_id");
    flds.add("_message");
    flds.add("_root_");
    flds.add("_version_");
    flds.add("channelId");
    flds.add("costTimeA");
    flds.add("costTimeB");
    flds.add("department");
    flds.add("queue");
    flds.add("request");
    flds.add("thread");
    flds.add("transactionId");
    flds.add("type");
    flds.add("log_dt");
    flds.add("IP");

    return flds;
  }

  public static FacetParas facetParas() {
    FacetParas paras = new FacetParas();
    paras.setCollection(collection());
    paras.setField(aggregateField());
    paras.setCount(1);
    paras.setLimit(10);
    paras.setSort("count");
    return paras;
  }

  public static AggregationParas aggregationParas() {
    ParasMediator paras = new ParasMediator();
    paras.setCollection(collection());
    paras.setOuterField(aggregateField());
    paras.setQuery("*:*");
    paras.setFilters("*:*");
    paras.setType(AggregationType.DATE);
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    paras.setOuterStart("2016-01-01T00:00:00.001Z");
    paras.setOuterEnd("2018-01-01T00:00:00.001Z");
    paras.setOuterGap("+4MONTHS");
    return paras;
  }

  public static FacetInfoRequest facetInfoRequest() {
    FacetInfoRequest request = new FacetInfoRequest();
    request.setCollection(collection());
    request.setCount(1);
    request.setField(aggregateField());
    request.setLimit(100);
    request.setAsc(false);
    request.setSortType("count");
    return request;
  }

  public static String valueField() {
    return "department";
  }

  public static NestedAggregationReq nestedAggregationReq() {
    //type=group
    NestedAggregationReq request = new NestedAggregationReq();
    request.setCollection(collection());
    request.setAggregateFiled(aggregateField());
    request.setTimeRange("lastyear");
    request.setQuery("*:*");
    request.setFilters("IP:110.184.40.18");
    request.setLimit(5);
    request.setOuterField("log_dt");
    request.setOuterFieldCategory(SolrFieldCategory.DATE);
    request.setOuterFieldCategory(SolrFieldCategory.TEXT);
    request.setInnerField(aggregateField());
    request.setInnerFieldCategory(SolrFieldCategory.DATE);
    request.setInnerStart("2015-11-08 11:08:39");
    request.setInnerEnd("2017-11-22 11:08:43");
    request.setInnerGap("1 day");
    return request;
  }

  public static String numberField() {
    return "costTimeA";
  }

  public static TimeRange timeRange(){
    return TimeTransfer.generateTimeRange(new Date(),TimeTransfer.LASTYEAR);
  }

  public static SolrDocRequest solrDocRequest() {
    SolrDocRequest request = new SolrDocRequest();
    request.setCollection(collection());
    request.setTimeRange("lastyear");
    request.setAggregateField(aggregateField());
    request.setQuery("*:*");
    request.setPage(0);
    request.setPageSize(30);
    request.setIgnoreCase(true);
    request.setDirection("asc");
    request.setFilters("type:系统信息 (jsdgp1.dll)\"刷新退市股票信息\"线程成功退出!");
    return request;
  }

  public static String type(){
    return "date";
  }
}
