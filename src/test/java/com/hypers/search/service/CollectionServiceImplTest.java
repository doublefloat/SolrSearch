package com.hypers.search.service;

import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.CommonData;
import com.hypers.common.StatusCode;
import com.hypers.search.model.request.FacetInfoRequest;
import com.sun.org.apache.regexp.internal.RE;
import java.util.Collection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-8-25
 */
public class CollectionServiceImplTest {

  @Test( dataProvider = "collectionService", dataProviderClass = ServiceDataProvider.class)
  public void testGetFields(CollectionService service) {
    com.hypers.common.wrapper.ResultWrapper result = service
        .getFields(CommonData.collection(), true);
    Assert.assertNotNull(result.getCode());
  }

  @Test( dataProvider = "collectionService", dataProviderClass = ServiceDataProvider.class)
  public void testGetFieldsException(CollectionService service) {
    com.hypers.common.wrapper.MessageWrapper result = service.getFields("collection-missed", false);
    Assert.assertEquals(result.getCode(), StatusCode.ERROR);
    Assert.assertEquals(result.getMessage(),
        "Collection: collection-missed doesn't exist");
  }

  @Test(dataProvider="collectionService",dataProviderClass = ServiceDataProvider.class)
  public void testGetFacetFields(CollectionService service){
    ResponseWrapper result=service.getFacetFields(CommonData.facetInfoRequest());
    Assert.assertNotNull(result);
    Assert.assertEquals(((ResultWrapper)result).getCode(),StatusCode.SUCCESS);
  }

  @Test(dataProvider = "collectionService", dataProviderClass = ServiceDataProvider.class)
  public void testGetFacetFieldsException(CollectionService service) {
    FacetInfoRequest request = CommonData.facetInfoRequest();
    request.setCollection("collection-missed");
    MessageWrapper result = service.getFacetFields(request);
    Assert.assertNotNull(result);
    Assert.assertEquals(result.getCode(), StatusCode.ERROR);
  }

  @Test(dataProvider="collectionService",dataProviderClass = ServiceDataProvider.class)
  public void testGetCollections(CollectionService service) {
    ResultWrapper result = service.getCollections();
    Assert.assertNotNull(result);
  }

  @Test(dataProvider = "collectionService", dataProviderClass = ServiceDataProvider.class)
  public void testGetCategorizedFields(CollectionService service) {
    ResultWrapper resultWrapper = service.getCategorizedFields(CommonData.collection());
    Assert.assertNotNull(resultWrapper);
  }
}
