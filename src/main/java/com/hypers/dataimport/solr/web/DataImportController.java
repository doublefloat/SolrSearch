package com.hypers.dataimport.solr.web;

import com.hypers.dataimport.solr.service.SolrUpdateService;
import com.hypers.dataimport.solr.service.UpdaterParas;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author long
 * @since 17-10-24
 */
@RestController
public class DataImportController {

  private SolrUpdateService solrUpdateService;

  @Autowired
  public void setSolrUpdateService(SolrUpdateService solrUpdateService) {
    this.solrUpdateService = solrUpdateService;
  }

  public int update(
      @RequestParam String collection,
      @RequestParam(required = false) int docsPerUpdate,
      @RequestParam(required = false) int charsPerLine,
      @RequestParam(required = false) String pairSeparator,
      @RequestParam(required = false) String keyValueSeparator,
      @RequestParam MultipartFile logFile, HttpServletResponse response) {
    UpdaterParas paras = new UpdaterParas();
    paras.setCollection(collection);
    paras.setDocsPerUpdate(docsPerUpdate);
    paras.setCharsPerLine(charsPerLine);
    paras.setPairSeparator(pairSeparator);
    paras.setKeyValueSeparator(keyValueSeparator);

    return solrUpdateService.update(paras, logFile);
  }
}
