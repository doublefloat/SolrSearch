package com.hypers.search.web;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author long
 * @since 17-11-22
 */
@RestController
@RequestMapping("/solr/search/utility")
public class UtilityServiceController {

  private UtilityService utilityService;

  @Autowired
  public void setUtilityService(UtilityService utilityService) {
    this.utilityService = utilityService;
  }

  @RequestMapping(value = "/gap", method = RequestMethod.GET)
  public ResponseWrapper getGap(
      @RequestParam String start,
      @RequestParam String end) {
    return utilityService.calculateGap(start, end);
  }
}
