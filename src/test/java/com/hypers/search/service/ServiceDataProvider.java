package com.hypers.search.service;

import com.hypers.search.CommonData;
import com.hypers.search.dao.CustomSolrRepositoryImpl;
import com.hypers.search.dao.CustomSolrRepository;
import com.hypers.search.utils.collection.CollectionFinder;
import com.hypers.search.utils.collection.DynamicFieldFilter;
import com.hypers.search.utils.collection.FieldFacetFinder;
import com.hypers.search.utils.collection.FieldFinder;
import com.hypers.search.utils.collection.FieldTypeFinder;
import java.net.MalformedURLException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.testng.annotations.DataProvider;

/**
 * @author long
 * @since 17-8-28
 */
public class ServiceDataProvider {
  private static String zkHost= CommonData.zkHost();

  @DataProvider(name="solrSearchService")
  public static Object[][] getSolrSearchService()throws MalformedURLException{
    CustomSolrRepository customSolrRepository=(CustomSolrRepository)getCustomSolrRepository()[0][0];
    SolrSearchServiceImpl service=new SolrSearchServiceImpl();
    service.setCustomSolrRepository(customSolrRepository);

    return new Object[][]{{service}};
  }

  @DataProvider(name="collectionService")
  public static Object[][] getCollectionService()throws MalformedURLException {
    CustomSolrRepository customSolrRepository=(CustomSolrRepository)getCustomSolrRepository()[0][0];
    CollectionServiceImpl collectionService=new CollectionServiceImpl();
    collectionService.setCustomSolrRepository(customSolrRepository);

    return new Object[][]{{collectionService}};
  }

  @DataProvider(name="customSolrRepository")
  public static Object[][] getCustomSolrRepository()throws MalformedURLException{
    CloudSolrServer server=new CloudSolrServer(zkHost);
    CustomSolrRepositoryImpl customSolrRepository=new CustomSolrRepositoryImpl();
    customSolrRepository.setFieldFinder(new FieldFinder(server));
    customSolrRepository.setFacetFieldFinder(new FieldFacetFinder(server));
    customSolrRepository.setCollectionFinder(new CollectionFinder(server));
    customSolrRepository.setDynamicFieldFilter(new DynamicFieldFilter(server));
    customSolrRepository.setFieldTypeFinder(new FieldTypeFinder(server));

    return new Object[][]{{customSolrRepository}};
  }
}
