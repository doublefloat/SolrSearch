package com.hypers.history.dao.impl;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.history.DataProvider;
import com.hypers.history.dao.HistoryStore;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.model.response.HistoryRecord;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author long
 * @since 17-9-12
 */
public class HistoryStoreImplTest {
  private Long uid=10101010L;
  private HistoryStore store= DataProvider.historyStore(DataProvider.sqlSessionFactory());

  @Test
  public void testSave()throws ParameterErrorException{
      int rows = store.save(DataProvider.recordEntity(null,uid));
      Assert.assertEquals(rows,1);
  }

  @Test
  public void testRetrieve()throws ParameterErrorException{
    testSave();
    List<HistoryRecord> list=retrieve(uid);
    Assert.assertNotEquals(list.isEmpty(),true);
  }

  List<HistoryRecord> retrieve(Long uid){
    try {
      return store.retrieve(DataProvider.retrieveParas(uid));
    }catch(ParameterErrorException e){
      e.printStackTrace();
    }
    return null;
  }

  Long fetchIdOfFirst(Long uid){
    return retrieve(uid).get(0).getId();
  }

  @Test
  public void testUpdate()throws ParameterErrorException {
    testSave();
    Long id=fetchIdOfFirst(uid);

    RecordEntity entity=DataProvider.recordEntity(id,uid);
    entity.setQuery("test is updated");

    store.update(entity);

    List<HistoryRecord> list=retrieve(uid);
    for(HistoryRecord record:list){
      if(record.getId().compareTo(id)==0)
        Assert.assertEquals(record.getQuery(),"test is updated");
    }
  }

  @Test
  public void testDelete() throws ParameterErrorException{
    testSave();
    Long id=fetchIdOfFirst(uid);

    store.delete(id);

    List<HistoryRecord> list=retrieve(uid);
    for(HistoryRecord record:list){
      if(record.getId().compareTo(id)==0)
        Assert.fail();
    }
  }

  @Test
  public void testSetDefaultSearch() throws ParameterErrorException{
    testSave();
    List<HistoryRecord> list=retrieve(uid);
    Long id=list.get(0).getId();
    Boolean defaultSearch=list.get(0).isDefaultSearch();

    Assert.assertFalse(defaultSearch);

    store.setDefaultSearch(id);

    list=retrieve(uid);
    for(HistoryRecord record:list){
      if(record.getId().compareTo(id)==0)
        Assert.assertTrue(record.isDefaultSearch());
    }
  }

  @Test
  public void testDeleteById()throws ParameterErrorException{
    testSave();
    Long id=fetchIdOfFirst(uid);
    store.deleteById(id);

    List<HistoryRecord> list=retrieve(uid);
    for(HistoryRecord record:list){
      if(record.getId().compareTo(id)==0)
        Assert.fail();
    }
  }

  @Test
  public void testDeleteByStatus()throws ParameterErrorException{
    testSave();
    store.deleteByStatus(EnumsRecordStatus.ACTIVE);

    Assert.assertTrue(retrieve(uid).isEmpty());
  }

  @Test
  public void testDeleteByUserId()throws ParameterErrorException{
    testSave();
    store.deleteByUserId(uid);
    List<HistoryRecord> list=retrieve(uid);

    Assert.assertTrue(list.isEmpty());
  }
}
