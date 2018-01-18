package com.hypers.history.dao.impl;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.history.dao.HistoryStore;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.model.response.HistoryRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author long
 * @since 17-9-1
 */
@Repository
public class HistoryStoreImpl implements HistoryStore {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistoryStoreImpl.class);

  private static final Integer DEFAULT_PAGE = 0;
  private static final Integer DEFAULT_PAGE_SIZE = 10;
  private static final Long DEFAULT_NUM = 20L;
  private static final EnumsRecordStatus DEFAULT_STATUS = EnumsRecordStatus.ACTIVE;

  private SqlSessionFactory sqlSessionFactory;
  private String namespace = "com.hypers.history.dao.HistoryStore";

  @Autowired
  public void setSqlSessionFactory(SqlSessionFactory factory) {
    sqlSessionFactory = factory;
  }

  public int save(RecordEntity entity) throws ParameterErrorException {
    assert entity != null;

    log.info("Trace: HistoryStoreImpl.save | RecordEntity: " + entity);

    // TODO put the stat in property file
    String stat = namespace + ".saveRecord";
    SqlSession session = null;
    try {
      verifyRecordEntity(entity);
      session = sqlSessionFactory.openSession();
      if (entity.isDefaultSearch()) {
        session.update(namespace + ".setIsDefaultFalse", entity.getUserId());
      }

      int num = session.insert(stat, entity);
      session.commit();

      return num;
    } finally {
      if (session != null)
        session.close();
    }
  }

  private void verifyRecordEntity(RecordEntity entity) throws ParameterErrorException {
    assert entity != null;

    log.info("Trace: HistoryStoreImpl.verifyRecordEntity | RecordEntity: " + entity);

    if (entity.getUserId() == null) {
      throw new ParameterErrorException("parameter:userId can't be null");
    }
    if (entity.getQuery() == null) {
      throw new ParameterErrorException("parameter:query can't be null");
    }
    if (entity.getCollection() == null) {
      throw new ParameterErrorException("parameter:collection cant't be null");
    }
    if (entity.getSearchName() == null) {
      throw new ParameterErrorException("parameter:searchName can't be null");
    }
    if (entity.getTimeRange() != null && entity.getTimeType() == null
        || entity.getTimeType() != null && entity.getTimeRange() == null) {
      throw new ParameterErrorException(
          "Parameter timeRange and timeType must be present at the same time");
    }
    // TODO complement default parameter
    if (entity.getPage() == null)
      entity.setPage(DEFAULT_PAGE);

    if (entity.getPageSize() == null)
      entity.setPageSize(DEFAULT_PAGE_SIZE);
  }

  public List<HistoryRecord> retrieve(RetrieveParas paras) throws ParameterErrorException {

    log.info("Trace: HistoryStoreImpl.retrieve | RetrieveParas: " + paras);

    String stat = namespace + ".selectRecords";
    complementParas(paras);
    SqlSession session = null;
    try {
      session = sqlSessionFactory.openSession();
      List<HistoryRecord> recordList = session.selectList(stat, paras);

      return recordList == null ? Collections.emptyList() : recordList;
    } finally {
      if (session != null)
        session.close();
    }
  }

  private void complementParas(RetrieveParas paras) throws ParameterErrorException {

    log.info("Trace: HistoryStoreImpl.complementParas | RetrieveParas: " + paras);
    if (paras.getNum() == null) {
      paras.setNum(DEFAULT_NUM);
    }
    if (paras.getStatus() == null) {
      paras.setStatus(DEFAULT_STATUS);
    }
    if (paras.getUserId() == null) {
      log.error("User id can't be null");
      throw new ParameterErrorException("User id can't be null");
    }

  }

  public int delete(Long id) {

    log.info("Trace: HistoryStoreImpl.delete | id: " + id);
    String stat = namespace + ".setDeleted";
    SqlSession session = null;
    try {
      session = sqlSessionFactory.openSession();
      int num = session.update(stat, id);
      session.commit();

      return num;
    } finally {
      if (session != null)
        session.close();
    }

  }

  public int update(RecordEntity entity) throws ParameterErrorException {

    log.info("Trace: HistoryStoreImpl.update | entity: " + entity);
    SqlSession session = null;
    try {
      verifyUpdateEntity(entity);
      session = sqlSessionFactory.openSession();
      int clms = session.update(namespace + ".updateRecord", entity);
      session.commit();

      return clms;
    } finally {
      if (session != null)
        session.close();
    }

  }

  private void verifyUpdateEntity(RecordEntity entity) throws ParameterErrorException {
    // TODO
  }

  @Transactional
  public int setDefaultSearch(Long id) {

    log.info("Trace: HistoryStoreImpl.setDefaultSearch | id: " + id);
    RetrieveParas paras = new RetrieveParas();
    paras.setId(id);
    SqlSession session = null;
    try {
      session = sqlSessionFactory.openSession();
      int rows = session.update(namespace + ".resetDefaultSearch", paras);
      session.update(namespace+".setDefault",id);
      session.commit();

      return rows;
    } finally {
      if (session != null)
        session.close();
    }

  }

  public int deleteById(Long id) {

    log.info("Trace: HistoryStoreImpl.deleteById | id: " + id);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      int rows = session.delete(namespace + ".deleteById", id);
      session.commit();
      return rows;
    }

  }

  public int deleteByStatus(EnumsRecordStatus status) {

    log.info("Trace: HistoryStoreImpl.deleteByStatus | status: " + status);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      int rows = session.delete(namespace + ".deleteByStatus", status);
      session.commit();
      return rows;
    }

  }

  public int deleteByUserId(Long userId) {

    log.info("Trace: HistoryStoreImpl.deleteByUserId | userId: " + userId);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      int rows = session.delete(namespace + ".deleteByUserId", userId);
      session.commit();
      return rows;
    }

  }

  public int test(int id) {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      String stat = namespace + ".test";
      return session.selectOne(stat, id);
    }
  }

  public int insert(int id) {

    log.info("Trace: HistoryStoreImpl.insert | id: " + id);
    String stat = namespace + ".insert";
    try (SqlSession session = sqlSessionFactory.openSession()) {
      int num = session.insert(stat, id);
      session.commit();
      return num;
    }

  }

}
