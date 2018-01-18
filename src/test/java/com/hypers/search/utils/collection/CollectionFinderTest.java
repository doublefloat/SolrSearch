package com.hypers.search.utils.collection;

import com.hypers.search.CommonData;
import java.net.MalformedURLException;
import java.util.Set;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-8-28
 */
public class CollectionFinderTest {
  @DataProvider(name="collectionFinder")
  public Object[][] getCollectionFinder()throws MalformedURLException{
    CloudSolrServer server= CommonData.getSolrServer();
    CollectionFinder finder=new CollectionFinder(server);

    return new Object[][]{{finder}};
  }

  @Test(groups="collectionFinder",dataProvider = "collectionFinder")
  public void testGetCollections(CollectionFinder finder){
    Set<String> cls=finder.getCollections();
    Assert.assertTrue(cls.containsAll(CommonData.collections()));
  }

   @Test(groups="collectionFinder",dataProvider = "collectionFinder")
  public void testGetActiveCollections(CollectionFinder finder){
    Set<String> cls=finder.getActiveCollections();
//    System.out.println(cls);
    Assert.assertTrue(cls.contains(CommonData.collection()));
  }


}
