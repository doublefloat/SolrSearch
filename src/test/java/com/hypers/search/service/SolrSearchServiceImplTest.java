package com.hypers.search.service;

import com.hypers.common.StatusCode;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.CommonData;
import com.hypers.search.dao.CustomSolrRepository;
import com.hypers.search.dao.CustomSolrRepositoryImplTest;
import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.FlatAggregationReq;
import com.hypers.search.model.request.NestedAggregationReq;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 2017/9/14
 */
public class SolrSearchServiceImplTest {
  //this instance doesn't connect to solr, so you can't execute query through it
  private SolrSearchServiceImpl solrSearchServiceImpl=new SolrSearchServiceImpl();

  @Test
  public void testResolveFilters() throws ParameterErrorException{
    String  filters="f1:v1,v2;f2:v2,v3";
    List<String> fls=solrSearchServiceImpl.resolveFilters(filters);
    Assert.assertEquals(fls.size(),2);
    Assert.assertEquals(fls.get(0),"f1:(v1 OR v2)");
    Assert.assertEquals(fls.get(1),"f2:(v2 OR v3)");
  }

  @Test
  public void testResolveTimeRange()throws ParameterErrorException{
//    Assert.assertEquals("[* TO *]",solrSearchServiceImpl.resolveTimeRange(null));
    String utcYesterday=solrSearchServiceImpl.resolveTimeRange("yesterday");
    Assert.assertNotNull(utcYesterday);

    String utcOneDayToNow=solrSearchServiceImpl.resolveTimeRange("1 TO NOW day");
    System.out.println(utcOneDayToNow);
    Assert.assertNotNull(utcOneDayToNow);

    String utcRandowTimeRange=solrSearchServiceImpl.resolveTimeRange("2017-05-04 13:00:00 TO "+
        "2018-03-04 23:00:32");
    System.out.println(utcRandowTimeRange);
    Assert.assertNotNull(utcRandowTimeRange);
  }

//  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
//  public void testAggregate2(CustomSolrRepository repository) {
//    //type=group and rangeFiledCategory=NUMBER
//    solrSearchServiceImpl.setCustomSolrRepository(repository);
//    AggregationRequest request = CommonData.aggregationRequest();
//    request.setRangeField(CommonData.numberField());
//    request.setRangeFieldCategory(SolrFieldCategory.NUMBER);
//    request.setStart("0");
//    request.setEnd("9");
//    request.setGap("2");
//    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl
//        .aggregate(request);
//    Assert.assertTrue(result.getCode().equals(StatusCode.SUCCESS));
//  }

//  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
//  public void testAggregate3(CustomSolrRepository repository) {
//    //type=GROUP_VALUE and rangeFiledCategory=NUMBER
//    solrSearchServiceImpl.setCustomSolrRepository(repository);
//    AggregationRequest request = CommonData.aggregationRequest();
//    request.setType(AggregationType.GROUP_VALUE);
//    request.setField(CommonData.valueField());
//    request.setLimit(10);
//    request.setRangeField(CommonData.numberField());
//    request.setRangeFieldCategory(SolrFieldCategory.NUMBER);
//    request.setStart("0");
//    request.setEnd("9");
//    request.setGap("2");
//    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl
//        .aggregate(request);
//    Assert.assertTrue(result.getCode().equals(StatusCode.SUCCESS));
//  }

//  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
//  public void testAggregate4(CustomSolrRepository repository) {
//    //type=GROUP_VALUE and rangeFiledCategory=DATE
//    solrSearchServiceImpl.setCustomSolrRepository(repository);
//    AggregationRequest request = CommonData.aggregationRequest();
//    request.setType(AggregationType.GROUP_VALUE);
//    request.setField(CommonData.valueField());
//    request.setLimit(10);
//    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl
//        .aggregate(request);
//    Assert.assertTrue(result.getCode().equals(StatusCode.SUCCESS));
//  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void testAggregateByDate(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    AggregationRequest request = CommonData.nestedAggregationReq();
    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl.aggregateByDate(request);
    Assert.assertNotNull(result);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void testAggregateByValueGroup(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    NestedAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.VALUE_GROUP);
    ResponseWrapper result =  solrSearchServiceImpl.aggregateByValueGroup(request);
    Assert.assertNotNull(result);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void testAggregateByValueGroup2(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    NestedAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.VALUE_GROUP);
    request.setInnerField(CommonData.numberField());
    request.setInnerFieldCategory(SolrFieldCategory.NUMBER);
    request.setInnerStart("0");
    request.setInnerEnd("10");
    request.setInnerGap("2");
    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl.aggregateByValueGroup(request);
    Assert.assertNotNull(result);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void testAggregateByValue(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    NestedAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.VALUE);
    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl.aggregateByValue(request);
    Assert.assertNotNull(result);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void testAggregateByGroup(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    NestedAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.GROUP);
    request.setOuterField(CommonData.numberField());
    request.setOuterFieldCategory(SolrFieldCategory.NUMBER);
    request.setOuterStart("0");
    request.setOuterEnd("10");
    request.setOuterGap("3");
    ResultWrapper result = (ResultWrapper) solrSearchServiceImpl.aggregateByGroup(request);
    Assert.assertNotNull(result);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void bugFix(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    FlatAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.VALUE);
    request.setFilters("type:系统信息 (jsdgp1.dll)\"刷新退市股票信息\"线程成功退出!");
    solrSearchServiceImpl.aggregateByValue(request);
  }

  @Test(dataProviderClass = CustomSolrRepositoryImplTest.class, dataProvider = "customSolrRepository")
  public void bugFix2(CustomSolrRepository repository) {
    solrSearchServiceImpl.setCustomSolrRepository(repository);
    NestedAggregationReq request = CommonData.nestedAggregationReq();
    request.setType(AggregationType.VALUE_GROUP);
    request.setOuterField("department");
    ResponseWrapper result = solrSearchServiceImpl.aggregateByValueGroup(request);
    Assert.assertEquals(result.getCode(), StatusCode.SUCCESS);
  }
}
