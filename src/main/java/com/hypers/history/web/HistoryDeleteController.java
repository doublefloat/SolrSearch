package com.hypers.history.web;

import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.history.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author long
 * @since 17-9-6
 */
@RestController
@RequestMapping("/solr/history/delete")
public class HistoryDeleteController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistoryDeleteController.class);

  private SearchHistoryService service;

  @Autowired
  public void setService(SearchHistoryService service) {
    this.service = service;
  }

  @RequestMapping(method = RequestMethod.GET)
  public MessageWrapper delete(long id) {
    log.info("Trace: HistoryDeleteController | id: " + id);
    return service.delete(id);
  }

}
