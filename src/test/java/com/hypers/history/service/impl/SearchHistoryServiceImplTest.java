package com.hypers.history.service.impl;

import com.hypers.common.StatusCode;
import com.hypers.history.DataProvider;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.history.model.RecordEntity;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.model.response.HistoryRecord;
import com.hypers.history.service.SearchHistoryService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author long
 * @since 17-9-1
 */
@RunWith(Parameterized.class)
public class SearchHistoryServiceImplTest {

  //  private static String resource="history/mybatis-config.xml";
  @Parameter
  public SearchHistoryService service;
  private static Properties properties=new Properties();

//  static{
//    try{
//      FileReader reader=new FileReader("history/service-test.properties");
//      properties.load(reader);
//    }catch(FileNotFoundException e){
//      e.printStackTrace();
//    }catch(IOException e){
//      e.printStackTrace();
//    }
//
//  }

  @Parameters
  public static Collection<Object[]> data() {
    SearchHistoryService service=DataProvider.searchHistoryService(
        DataProvider.historyStore(DataProvider.sqlSessionFactory()));

    return Arrays.asList(new Object[][]{{service}});
  }

  @Test
  public void test() {
    Assert.assertEquals(service.test(3), 3);
  }

  @Test
  public void testInsert() {
    Assert.assertEquals(service.insert(5), 1);
  }

  private Long userId=10101010L;

  @Test
  public void testSave(){
    RecordEntity entity=new RecordEntity();
    entity.setUserId(userId);
    entity.setQuery("this is a test");
    entity.setFilters("junit test");
    entity.setPage(0);
    entity.setDefaultSearch(false);
    entity.setCollection("collection-test");
    entity.setSearchName("search-test");

    MessageWrapper msg=service.save(entity);

    Assert.assertEquals(msg.getCode(), StatusCode.SUCCESS);
  }

  @Test
  public void testRetrieve(){
    RetrieveParas paras=new RetrieveParas(userId,0L,20L, EnumsRecordStatus.valueOf("ACTIVE"));
    ResultWrapper result=(ResultWrapper)service.retrieve(paras);
    Assert.assertEquals(result.getCode(),StatusCode.SUCCESS);
    Assert.assertNotNull(result.getResult().getItems());
  }

  private ResultWrapper retrieve(long userId,long num,String status){
    RetrieveParas paras=new RetrieveParas(userId,0L,num, EnumsRecordStatus.valueOf(status));
    ResultWrapper result=(ResultWrapper)service.retrieve(paras);
    return result;
  }

  @Test
  public void testDelete(){
    ResultWrapper result=retrieve(userId,10,"ACTIVE");
    HistoryRecord record=(HistoryRecord)result.getResult().getItems().get(0);

    MessageWrapper msg = service.delete(record.getId());

    Assert.assertEquals(msg.getCode(), StatusCode.SUCCESS);

    result = retrieve(13453L,10,"ACTIVE");

    Assert.assertEquals(result.getResult().getItems().isEmpty(),true);
  }

  @Test
  public void testUpdate(){
    testSave();
    ResultWrapper result=retrieve(userId,10,"ACTIVE");
    long id=((HistoryRecord)result.getResult().getItems().get(0)).getId();

    RecordEntity entity=new RecordEntity();
    entity.setId(id);
    entity.setQuery("update here");

    service.update(entity);

    result=retrieve(userId,10,"ACTIVE");
    String query=((HistoryRecord)result.getResult().getItems().get(0)).getQuery();

    Assert.assertEquals(query,"update here");
    service.delete(id);
  }

  @Test
  public void testSetDefaultSearch(){
    testSave();
    ResultWrapper result=retrieve(userId,10,"ACTIVE");
    long id=((HistoryRecord)result.getResult().getItems().get(0)).getId();
    service.setDefaultSearch(id);

    result=retrieve(userId,10,"ACTIVE");
    boolean defaultSearch=((HistoryRecord)result.getResult().getItems().get(0)).isDefaultSearch();

    Assert.assertEquals(defaultSearch,true);
    service.delete(id);
  }
}



