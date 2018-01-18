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
@RequestMapping("/solr/history")
public class HistorySetDefaultController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistorySetDefaultController.class);

  private SearchHistoryService service;

  @Autowired
  public void setService(SearchHistoryService service) {
    this.service = service;
  }

  @RequestMapping(value = "/default", method = RequestMethod.GET)
  public MessageWrapper setDefaultSearch(Long id) {
    log.info("Trace: HistorySetDefaultController | id: " + id);
    return service.setDefaultSearch(id);
  }

}
