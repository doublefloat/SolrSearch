package com.hypers.history.web;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.history.enums.EnumsRecordStatus;
import com.hypers.history.model.request.RetrieveParas;
import com.hypers.history.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author long
 * @since 17-9-6
 */
@RestController
@RequestMapping("/solr/history/retrieve")
public class HistoryRetrieveController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HistoryRetrieveController.class);

  @Autowired
  SearchHistoryService service;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseWrapper retrieve(@RequestParam Long userId,
      @RequestParam(defaultValue = "0") long offset, @RequestParam(defaultValue = "20") long num,
      @RequestParam(defaultValue = "ACTIVE") EnumsRecordStatus status) {

    RetrieveParas paras = new RetrieveParas(userId, offset, num, status);
    log.info("Trace: HistoryRetrieveController | RetrieveParas: " + paras.toString());

    return service.retrieve(paras);

  }

}
