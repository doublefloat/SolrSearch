package com.hypers.search.dao;

import com.hypers.common.exception.OutLimitException;
import com.hypers.search.CommonData;
import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.Count;
import com.hypers.search.model.ParasMediator;
import com.hypers.search.model.SolrField;
import com.hypers.search.model.FacetParas;
import com.hypers.search.utils.collection.InnerRangeFacetFinder;
import com.hypers.search.utils.collection.RangeFacetFinder;
import com.hypers.search.utils.collection.CollectionFinder;
import com.hypers.search.utils.collection.DynamicFieldFilter;
import com.hypers.search.utils.collection.FieldFacetFinder;
import com.hypers.search.utils.collection.FieldFinder;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.utils.date.SolrDateUtil;
import com.hypers.search.utils.date.TimeRange;
import com.hypers.search.utils.date.TimeTransfer;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-8-27
 */
public class CustomSolrRepositoryImplTest {

  @DataProvider(name = "customSolrRepository")
  public static Object[][] getCustomSolrRepository() throws MalformedURLException {
    CloudSolrServer server = CommonData.getSolrServer();
    CustomSolrRepositoryImpl customSolrRepository = new CustomSolrRepositoryImpl();
    customSolrRepository.setFieldFinder(new FieldFinder(server));
    customSolrRepository.setFacetFieldFinder(new FieldFacetFinder(server));
    customSolrRepository.setCollectionFinder(new CollectionFinder(server));
    customSolrRepository.setDynamicFieldFilter(new DynamicFieldFilter(server));
    RangeFacetFinder aggregationFinder = new RangeFacetFinder();
    aggregationFinder.setSolrServer(server);
    customSolrRepository.setRangeFacetFinder(aggregationFinder);
    customSolrRepository.setInnerRangeFacetFinder(new InnerRangeFacetFinder(server));

    return new Object[][]{{customSolrRepository}};
  }

  @Test(dataProvider = "customSolrRepository")
  public void tetGetFieldNames(CustomSolrRepository solrRepository) throws ParameterErrorException {
//    List<String> list = solrRepository.getFieldNames(CommonData.collection());
//    Assert.assertNotNull(list);
  }

  @Test(dataProvider = "customSolrRepository", expectedExceptions = {
      SecurityException.class, ParameterErrorException.class})
  public void testGetFieldNamesException(CustomSolrRepository repository)
      throws ParameterErrorException {
//    repository.getFieldNames("collection-missed");
  }

  @Test(dataProvider = "customSolrRepository", expectedExceptions = {
      ParameterErrorException.class, SecurityException.class})
  public void testGetFieldNamesWithFieldType(CustomSolrRepository repository)
      throws ParameterErrorException {
//    repository.getFieldNames(CommonData.collection(), "field-missed");
  }

  @Test(dataProvider = "customSolrRepository")
  public void testGetFacetFields(CustomSolrRepository repository) throws ParameterErrorException {
    Map<String, Long> result = repository
        .getFieldFacetInfo(CommonData.facetParas(), false);
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "customSolrRepository", expectedExceptions = ParameterErrorException.class)
  public void testGetFacetFieldsException(CustomSolrRepository repository)
      throws ParameterErrorException {
    FacetParas paras = CommonData.facetParas();
    paras.setCollection("collection-missed");
    repository.getFieldFacetInfo(paras, false);
  }

  @Test(dataProvider = "customSolrRepository", expectedExceptions = ParameterErrorException.class)
  public void testGetFacetFieldsExceptionWithFieldMissed(CustomSolrRepository repository)
      throws ParameterErrorException {
    FacetParas paras = CommonData.facetParas();
    paras.setField("field-missed");
    repository.getFieldFacetInfo(paras, false);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testGetCollections(CustomSolrRepository repository) {
    List<String> collections = repository.getCollections();
    Assert.assertNotNull(collections);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByValue(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    //aggregateType=VALUE
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.VALUE);
    paras.setInnerField(CommonData.valueField());
    paras.setOuterLimit(40);
    List<Count> result = impl.aggregateByValue(paras);
    Assert.assertTrue(result.size() <= 40);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroup(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.GROUP);
    paras.setOuterLimit(20);
    paras.setInnerField(CommonData.valueField());
    //outerFieldCategory is 'DATE'
    paras.setOuterField("costTimeA");
    paras.setOuterFieldCategory(SolrFieldCategory.NUMBER);
    paras.setOuterStart("0");
    paras.setOuterEnd("10");
    paras.setOuterGap("2");

    List<Count> result = impl.aggregateByGroup(paras);
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroup2(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.GROUP);
    paras.setOuterLimit(20);
    paras.setInnerField(CommonData.valueField());
    //outerFieldCategory is 'DATE'
    paras.setOuterField("log_dt");
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    TimeRange timeRange = CommonData.timeRange();
    paras.setOuterStart(String.valueOf(timeRange.getStart().getTime()));
    paras.setOuterEnd(String.valueOf(timeRange.getEnd().getTime()));
    paras.setOuterGap(String.valueOf(2 * TimeTransfer.MONTH_MILI_30));

    List<Count> result = impl.aggregateByGroup(paras);
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testGetSolrFields(CustomSolrRepository repository) throws ParameterErrorException {
    List<SolrField> result = repository.getSolrFields(CommonData.collection());
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroupValue(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.GROUP_VALUE);
    paras.setOuterField("costTimeA");
    //outerFieldCategory is 'NUMBER'
    paras.setOuterFieldCategory(SolrFieldCategory.NUMBER);
    paras.setInnerField("department");
    paras.setOuterStart("0");
    paras.setOuterEnd("10");
    paras.setOuterGap("2");
    paras.setOuterLimit(10);
    paras.setInnerLimit(10);
    List<Count> countList = impl.aggregateByGroupValue(paras);
    Assert.assertNotNull(countList);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroupValue2(CustomSolrRepository repository)
      throws ParameterErrorException, ParseException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    //outerFieldCategory is 'DATE'
    paras.setOuterField("log_dt");
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    paras.setInnerField("department");
    paras.setType(AggregationType.GROUP_VALUE);
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);
    String utcString = "2016-05-01T00:00:00.000Z";
    paras.setOuterStart(
        String.valueOf(SolrDateUtil.parseAsLocalDate(null, null, utcString).getTime()));
    paras.setOuterEnd(String.valueOf(System.currentTimeMillis()));
    paras.setOuterGap(String.valueOf(3600 * 24 * 30 * 1000L));
    List<Count> result = impl.aggregateByGroupValue(paras);
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByValueGroup(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.VALUE_GROUP);
    paras.setOuterField("IP");
    paras.setInnerField("costTimeA");
    paras.setInnerFieldCategory(SolrFieldCategory.NUMBER);
    paras.setInnerEnd("10");
    paras.setInnerStart("0");
    paras.setInnerGap("2");
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);
    List<Count> countList = impl.aggregateByValueGroup(paras);
    Assert.assertNotNull(countList);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByValueGroup2(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.VALUE_GROUP);
    paras.setOuterField("IP");
    //innerFieldCategory is 'DATE'
    paras.setInnerField("log_dt");
    paras.setInnerFieldCategory(SolrFieldCategory.DATE);
    TimeRange timeRange = CommonData.timeRange();
    paras.setInnerStart(String.valueOf(timeRange.getStart().getTime()));
    paras.setInnerEnd(String.valueOf(timeRange.getEnd().getTime()));
    paras.setInnerGap(String.valueOf(2 * TimeTransfer.MONTH_MILI_30));
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);
    List<Count> countList = impl.aggregateByValueGroup(paras);
    Assert.assertNotNull(countList);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByValueValue(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.VALUE_VALUE);
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);
    paras.setOuterField("IP");
    paras.setInnerField("type");
    List<Count> countList = impl.aggregateByValueValue(paras);
    Assert.assertNotNull(countList);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroupGroup(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.GROUP_GROUP);
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);

    paras.setOuterField("log_dt");
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    TimeRange timeRange = CommonData.timeRange();
    paras.setOuterStart(String.valueOf(timeRange.getStart().getTime()));
    paras.setOuterEnd(String.valueOf(timeRange.getEnd().getTime()));
    paras.setOuterGap(String.valueOf(2 * TimeTransfer.MONTH_MILI_30));

    paras.setInnerField("costTimeA");
    paras.setInnerFieldCategory(SolrFieldCategory.NUMBER);
    paras.setInnerStart("0");
    paras.setInnerEnd("10");
    paras.setInnerGap("2");

    List<Count> countList = impl.aggregateByGroupGroup(paras);
    Assert.assertNotNull(countList);
  }

  @Test(dataProvider = "customSolrRepository")
  public void testAggregateByGroupGroup2(CustomSolrRepository repository)
      throws ParameterErrorException, OutLimitException {
    CustomSolrRepositoryImpl impl = (CustomSolrRepositoryImpl) repository;
    ParasMediator paras = (ParasMediator) CommonData.aggregationParas();
    paras.setType(AggregationType.GROUP_GROUP);
    paras.setOuterLimit(20);
    paras.setInnerLimit(20);

    paras.setOuterField("costTimeA");
    paras.setOuterFieldCategory(SolrFieldCategory.NUMBER);
    paras.setOuterStart("0");
    paras.setOuterEnd("10");
    paras.setOuterGap("2");

    TimeRange timeRange = CommonData.timeRange();
    paras.setInnerField("log_dt");
    paras.setInnerFieldCategory(SolrFieldCategory.DATE);
    paras.setInnerStart(String.valueOf(timeRange.getStart().getTime()));
    paras.setInnerEnd(String.valueOf(timeRange.getEnd().getTime()));
    paras.setInnerGap(String.valueOf(2 * TimeTransfer.MONTH_MILI_30));

    List<Count> countList = impl.aggregateByGroupGroup(paras);
    Assert.assertNotNull(countList);
  }
}
