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
@RequestMapping("/solr/history/update")
public class HistoryUpdateController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistoryUpdateController.class);

  private SearchHistoryService service;

  @Autowired
  public void setService(SearchHistoryService service) {
    this.service = service;
  }

  @RequestMapping(method = RequestMethod.PUT)
  public MessageWrapper update(@RequestBody RecordEntity entity) {
    log.info("Trace: HistoryUpdateController | RecordEntity: " + entity.toString());
    return service.update(entity);
  }

}
