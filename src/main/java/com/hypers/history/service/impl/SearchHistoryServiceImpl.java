package com.hypers.history.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypers.common.SimpleTransfer;
import com.hypers.common.StatusCode;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.history.dao.HistoryStore;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.model.response.HistoryRecord;
import com.hypers.history.service.SearchHistoryService;

/**
 * @author long
 * @since 17-9-6
 */
@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SearchHistoryServiceImpl.class);

  private HistoryStore historyStore;

  @Autowired
  public void setHistoryStore(HistoryStore store) {
    this.historyStore = store;
  }

  public MessageWrapper save(RecordEntity entity) {
    try {
      int num = historyStore.save(entity);

      if (num == 0) {
        log.info("Unknown error:insert is failure" + SimpleTransfer.toMap(entity));
        return new MessageWrapper().wrapMessage(StatusCode.ERROR, "Unknown error:insert is failure",
            SimpleTransfer.toMap(entity));
      } else {
        log.info("Success: " + num + " rows is inserted", SimpleTransfer.toMap(entity));
        return new MessageWrapper().wrapMessage(StatusCode.SUCCESS,
            "Success: " + num + " rows is inserted", SimpleTransfer.toMap(entity));
      }

    } catch (ParameterErrorException e) {
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(),
          SimpleTransfer.toMap(entity));
    }
  }

  public ResponseWrapper retrieve(RetrieveParas paras) {
    try {
      ResultWrapper result = new ResultWrapper();
      List<HistoryRecord> recordList = historyStore.retrieve(paras);

      result.wrapResult(StatusCode.SUCCESS, recordList, SimpleTransfer.toMap(paras));

      return result;
    } catch (ParameterErrorException e) {
      log.error("Error: Retrieve parameters is wrong", SimpleTransfer.toMap(paras));
      return new MessageWrapper().wrapMessage(StatusCode.ERROR,
          "Error: Retrieve parameters is wrong", SimpleTransfer.toMap(paras));
    }
  }

  public MessageWrapper delete(Long id) {

    int num = historyStore.delete(id);

    if (num == 0) {
      log.info("Unknown error: delete is failure" + SimpleTransfer.toMap("id", id));
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, "Unknown error: delete is failure",
          SimpleTransfer.toMap("id", id));
    } else {
      log.info("Success: " + num + " is deleted" + SimpleTransfer.toMap("id", id));
      return new MessageWrapper().wrapMessage(StatusCode.SUCCESS, "Success: " + num + " is deleted",
          SimpleTransfer.toMap("id", id));
    }

  }

  public MessageWrapper update(RecordEntity entity) {
    try {
      int clms = historyStore.update(entity);

      if (clms == 0) {
        log.info("Unknown error: update is failed" + SimpleTransfer.toMap(entity));
        return new MessageWrapper().wrapMessage(StatusCode.ERROR, "Unknown error: update is failed",
            SimpleTransfer.toMap(entity));
      } else {
        log.info("Success: " + clms + " columns is updated", SimpleTransfer.toMap(entity));
        return new MessageWrapper().wrapMessage(StatusCode.SUCCESS,
            "Success: " + clms + " columns is updated", SimpleTransfer.toMap(entity));
      }
    } catch (ParameterErrorException e) {
      log.error("Error: Retrieve parameters is wrong", SimpleTransfer.toMap(entity));
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(),
          SimpleTransfer.toMap(entity));
    }

  }

  public MessageWrapper setDefaultSearch(Long id) {
    MessageWrapper msg = new MessageWrapper();
    int rows = historyStore.setDefaultSearch(id);

    if (rows == 0) {
      log.info("Unknown error: update is failed" + SimpleTransfer.toMap("id", id));
      msg.wrapMessage(StatusCode.ERROR, "Unknown error: update is failed",
          SimpleTransfer.toMap("id", id));
    } else {
      log.info("Success: " + rows + " rows are updated" + SimpleTransfer.toMap("id", id));
      msg.wrapMessage(StatusCode.SUCCESS, "Success: " + rows + " rows are updated",
          SimpleTransfer.toMap("id", id));
    }

    return msg;
  }

  public int test(Integer id) {
    return historyStore.test(id);
  }

  public int insert(Integer id) {
    return historyStore.insert(id);
  }

}
