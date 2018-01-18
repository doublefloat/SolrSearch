package com.hypers.history.service;

import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.history.model.RecordEntity;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.history.model.request.RetrieveParas;

/**
 * @author long
 * @since 17-9-6
 */
public interface SearchHistoryService {

  MessageWrapper save(RecordEntity entity);

  ResponseWrapper retrieve(RetrieveParas paras);

  MessageWrapper delete(Long id);

  MessageWrapper update(RecordEntity entity);

  MessageWrapper setDefaultSearch(Long id);

  int test(Integer id);

  int insert(Integer id);

}