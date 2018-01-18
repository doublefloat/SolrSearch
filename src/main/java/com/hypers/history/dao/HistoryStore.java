package com.hypers.history.dao;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.model.response.HistoryRecord;
import java.util.List;

/**
 * @author long
 * @since 17-9-1
 */
public interface HistoryStore {
  int save(RecordEntity entity)throws ParameterErrorException;
  List<HistoryRecord> retrieve(RetrieveParas paras)throws ParameterErrorException;
  int update(RecordEntity entity)throws ParameterErrorException;
  int delete(Long id);
  int setDefaultSearch(Long id);
  int deleteById(Long id);
  int deleteByStatus(EnumsRecordStatus status);
  int deleteByUserId(Long userId);
  int test(int id);
  int insert(int id);
}
