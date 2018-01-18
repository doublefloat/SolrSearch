package com.hypers.history.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.history.model.RecordEntity;
import com.hypers.history.service.SearchHistoryService;

/**
 * @author long
 * @since 17-9-6
 */
@RestController
public class HistorySaveController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistorySaveController.class);

  private SearchHistoryService service;

  @Autowired
  public void setService(SearchHistoryService service) {
    this.service = service;
  }

  @RequestMapping(value = "solr/history/save", method = RequestMethod.POST)
  public MessageWrapper save(@RequestBody RecordEntity entity) {
    log.info("Trace: HistorySaveController | RecordEntity: " + entity.toString());
    return service.save(entity);
  }

}
