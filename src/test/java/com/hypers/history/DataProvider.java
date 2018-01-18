package com.hypers.history;

import com.hypers.history.dao.HistoryStore;
import com.hypers.history.dao.impl.HistoryStoreImpl;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.enums.EnumsTimeType;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.service.SearchHistoryService;
import com.hypers.history.service.impl.SearchHistoryServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author long
 * @since 17-9-12
 */
public class DataProvider {
  public static RetrieveParas retrieveParas(Long uid){
    return new RetrieveParas(uid,0L,10L, EnumsRecordStatus.ACTIVE);
  }
  public static RecordEntity recordEntity(Long id,Long uid){
    RecordEntity record=new RecordEntity();
    record.setId(id);
    record.setUserId(uid);
    record.setQuery("HistoryStoreTest");
    record.setTimeRange("test to test");
    record.setTimeType(EnumsTimeType.COMMON);
    record.setFilters("test:test");
    record.setFields("test");
    record.setPage(0);
    record.setPageSize(10);
    record.setCollection("collection-test");
    record.setSearchName("testName");

    return record;
  }

  public static SqlSessionFactory sqlSessionFactory() {
    String resource = "history/mybatis-config.xml";

    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream(resource);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new SqlSessionFactoryBuilder().build(inputStream);
  }

  public static HistoryStore historyStore(SqlSessionFactory factory){
    HistoryStoreImpl store= new HistoryStoreImpl();
    store.setSqlSessionFactory(factory);

    return store;
  }

  public static SearchHistoryService searchHistoryService(HistoryStore store){
    SearchHistoryServiceImpl service=new SearchHistoryServiceImpl();
    service.setHistoryStore(store);

    return service;
  }
}
